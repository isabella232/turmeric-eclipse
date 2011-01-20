/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
/*******************************************************************************
 * Copyright (c) 2007, 2009 David Green and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     David Green - initial API and implementation
 *******************************************************************************/

package org.ebayopensource.mylyn.wikitext.confluence.core.tasks;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.imageio.ImageIO;

import org.apache.tools.ant.BuildException;
import org.eclipse.mylyn.internal.wikitext.core.parser.builder.DefaultSplittingStrategy;
import org.eclipse.mylyn.internal.wikitext.core.parser.builder.NoSplittingStrategy;
import org.eclipse.mylyn.internal.wikitext.core.parser.builder.SplitOutlineItem;
import org.eclipse.mylyn.internal.wikitext.core.parser.builder.SplittingHtmlDocumentBuilder;
import org.eclipse.mylyn.internal.wikitext.core.parser.builder.SplittingOutlineParser;
import org.eclipse.mylyn.internal.wikitext.core.parser.builder.SplittingStrategy;
import org.eclipse.mylyn.wikitext.confluence.core.ConfluenceLanguage;
import org.eclipse.mylyn.wikitext.core.parser.MarkupParser;
import org.eclipse.mylyn.wikitext.core.parser.markup.MarkupLanguage;
import org.eclipse.mylyn.wikitext.core.parser.outline.OutlineItem;
import org.eclipse.mylyn.wikitext.core.util.DefaultXmlStreamWriter;
import org.eclipse.mylyn.wikitext.core.util.FormattingXMLStreamWriter;
import org.eclipse.mylyn.wikitext.core.util.XmlStreamWriter;
import org.eclipse.mylyn.wikitext.core.util.anttask.MarkupTask;
import org.eclipse.mylyn.wikitext.core.util.anttask.MarkupToHtmlTask.Stylesheet;

import com.atlassian.confluence.rpc.soap.beans.RemoteAttachment;
import com.atlassian.confluence.rpc.soap.beans.RemotePage;
import com.atlassian.confluence.rpc.soap.beans.RemotePageSummary;
import com.atlassian.confluence.rpc.soap.beans.RemoteSpaceSummary;

public class WikiToDocTask extends MarkupTask {
	protected String htmlFilenameFormat = "$1.html"; //$NON-NLS-1$

	private String wikiBaseUrl;

	private List<Space> spaces = new ArrayList<Space>();

	private File dest;

	private PageAppendum pageAppendum;

	private final List<Stylesheet> stylesheets = new ArrayList<Stylesheet>();

	protected String linkRel;

	protected boolean multipleOutputFiles = false;

	protected boolean formatOutput = true;

	protected boolean navigationImages = true;

	protected String prependImagePrefix = "images"; //$NON-NLS-1$

	private final boolean useInlineCssStyles = true;

	private final boolean suppressBuiltInCssStyles = false;

	private String defaultAbsoluteLinkTarget;

	private final boolean xhtmlStrict = false;

	private final boolean emitDoctype = true;

	private final String htmlDoctype = null;

	private String helpPrefix;

	private boolean fetchImages = true;

	private File tocFile;

	private String title;

	private boolean generateUnifiedToc = true;

	private String templateExcludes;

	private ConfluenceLanguage markupLanguage;
	
	private String userId = "";
	
	private String password = "";

	public WikiToDocTask() {
	}

	@Override
	public void execute() throws ConfigurationException {
		initialize();
		createEclipseHelp();
	}

	private void initialize() {
		checkForRequiredAttributes();
		calculateInternalLink();

		Set<String> spaceNames = setupSpaces();
		markupLanguage = (ConfluenceLanguage) createMarkupLanguage();

		validateStylesheetSettings();
		checkForDestDir();
		initializeTocXML();
	}

	private void initializeTocXML() {
		if (tocFile == null) {
			tocFile = new File(dest, "toc.xml"); //$NON-NLS-1$
		}
	}

	private void checkForDestDir() {
		if (!dest.exists()) {
			if (!dest.mkdirs()) {
				throw new BuildException(
						MessageFormat
								.format("Cannot create dest folder: {0}", dest.getAbsolutePath())); //$NON-NLS-1$
			}
		}
	}

	private void validateStylesheetSettings() {
		for (Stylesheet stylesheet : stylesheets) {
			if (stylesheet.getUrl() == null && stylesheet.getFile() == null) {
				throw new BuildException(
						Messages.getString("WikiToDocTask_stylesheet_file_or_url")); //$NON-NLS-1$
			}
			if (stylesheet.getUrl() != null && stylesheet.getUrl() != null) {
				throw new BuildException(
						Messages.getString("WikiToDocTask_stylesheet_not_both")); //$NON-NLS-1$
			}
			if (stylesheet.getUrl() != null) {
				if (!stylesheet.getFile().exists()) {
					throw new BuildException(
							MessageFormat.format(
									Messages.getString("WikiToDocTask_stylesheet_file_not_exist"), //$NON-NLS-1$
									stylesheet.getFile()));
				}
				if (!stylesheet.getFile().isFile()) {
					throw new BuildException(
							MessageFormat.format(
									Messages.getString("WikiToDocTask_stylesheet_file_not_file"), //$NON-NLS-1$
									stylesheet.getFile()));
				}
				if (!stylesheet.getFile().canRead()) {
					throw new BuildException(
							MessageFormat.format(
									Messages.getString("WikiToDocTask_stylesheet_file_cannot_read"), stylesheet.getFile())); //$NON-NLS-1$
				}
			}
		}
	}

	private Set<String> setupSpaces() {
		Set<String> spaceNames = new HashSet<String>();
		for (Space space : spaces) {
			if (space.getName() == null) {
				throw new ConfigurationException(
						Messages.getString("WikiToDocTask_path_must_have_name")); //$NON-NLS-1$
			}

			if (!spaceNames.add(space.getName())) {
				throw new ConfigurationException(
						MessageFormat.format(
								Messages.getString("WikiToDocTask_path_name_must_be_unique"), space.getName())); //$NON-NLS-1$
			}

			if (space.getPageName() == null) {
				throw new ConfigurationException("Missing required page name");
			}
		}
		return spaceNames;
	}

	private void calculateInternalLink() {
		if (getInternalLinkPattern() == null) {
			setInternalLinkPattern(computeDefaultInternalLinkPattern());
		}
	}

	private void checkForRequiredAttributes() {
		if (dest == null) {
			throw new ConfigurationException(
					Messages.getString("WikiToDocTask_specify_dest")); //$NON-NLS-1$
		}
		if (wikiBaseUrl == null) {
			throw new ConfigurationException(
					Messages.getString("WikiToDocTask_specify_wikiBaseUrl")); //$NON-NLS-1$
		}
		if (spaces.isEmpty()) {
			throw new ConfigurationException(
					Messages.getString("WikiToDocTask_specify_paths")); //$NON-NLS-1$
		}
	}

	private void createEclipseHelp() {
		for (Space space : spaces) {
			ConfluenceRPCHelper confluenceRPC = new ConfluenceRPCHelper(
					space.getName(), space.getPageName(), getUserId(), getPassword() );
			try {
				confluenceRPC.login();
			} catch (Exception ex) {
				throw new BuildException("Could not login to confluence");
			}

			RemotePage page = confluenceRPC.getTopPage();
			if (page == null) {
				throw new BuildException("Could not retrieve "
						+ space.getPageName() + " from " + space.getName());
			}

			generatePage(confluenceRPC, page);

			if (space.isGenerateToc()) {
				generateToc(confluenceRPC, page, space.getHelpDir());
			}

			if (space.isGenerateContextHelp()) {
				generateContextHelp(confluenceRPC, page, space.getHelpDir());
			}

			confluenceRPC.logout();
		}

	}

	private void generatePage(ConfluenceRPCHelper confluenceRPC, RemotePage page) {

		System.out.println("Generating Page for " + page.getTitle()
				+ " residing in space " + page.getSpace() + ".");
		File htmlOutputFile = computeHtmlFile(dest, page.getTitle());

		Writer writer;
		try {
			writer = new OutputStreamWriter(new BufferedOutputStream(
					new FileOutputStream(htmlOutputFile)), "utf-8"); //$NON-NLS-1$
		} catch (Exception e) {
			throw new BuildException(
					"Unable to create output stream for " + page.getTitle(), e); //$NON-NLS-1$
		}
		ConfluenceHtmlDocumentBuilder builder = new ConfluenceHtmlDocumentBuilder(
				writer, formatOutput);
		builder.setEmitAsDocument(true);
		builder.setTitle(page.getTitle());
		builder.setPrependImagePrefix(prependImagePrefix);
		builder.setLinkRel(linkRel);
		builder.setDefaultAbsoluteLinkTarget(defaultAbsoluteLinkTarget);

		SplittingStrategy splittingStrategy = multipleOutputFiles ? new DefaultSplittingStrategy()
				: new NoSplittingStrategy();
		SplittingOutlineParser outlineParser = new SplittingOutlineParser();
		outlineParser.setMarkupLanguage(markupLanguage.clone());
		outlineParser.setSplittingStrategy(splittingStrategy);

		SplitOutlineItem item = outlineParser.parse(page.getContent());
		item.setSplitTarget(htmlOutputFile.getName());

		SplittingHtmlDocumentBuilder splittingBuilder = new SplittingHtmlDocumentBuilder();
		splittingBuilder.setRootBuilder(builder);
		splittingBuilder.setOutline(item);
		splittingBuilder.setRootFile(htmlOutputFile);
		splittingBuilder.setNavigationImages(navigationImages);
		splittingBuilder.setFormatting(formatOutput);

		MarkupParser markupParser = new MarkupParser();
		markupParser.setMarkupLanguage(markupLanguage);
		markupParser.setBuilder(splittingBuilder);
		markupParser.parse(page.getContent());

		downloadImages(confluenceRPC, page);
		generateChildrenPages(confluenceRPC, page);

	}

	private void generateChildrenPages(ConfluenceRPCHelper confluenceRPC,
			RemotePage page) {
		RemotePageSummary[] rmSum = confluenceRPC.getChildrenPages(page);
		if (rmSum != null) {
			for (RemotePageSummary rpSum : rmSum) {
				RemotePage childPage = confluenceRPC.getPage(rpSum.getTitle());
				if (childPage != null) {
					generatePage(confluenceRPC, childPage);
				}
			}
		}
	}

	protected File computeHtmlFile(final File source, String name) {
		return new File(source, htmlFilenameFormat.replace("$1", name)); //$NON-NLS-1$
	}

	private void downloadImages(ConfluenceRPCHelper confluenceRPC,
			RemotePage page) {
		try {
			RemoteAttachment[] attachments = confluenceRPC.getAttachments(page);
			if (attachments == null || attachments.length == 0) {
				return;
			}

			File imageDir = new File(dest, this.prependImagePrefix);
			if (!imageDir.exists()) {
				imageDir.mkdir();
			}

			for (RemoteAttachment attachment : attachments) {
				try {
					URL attachurl = new URL(attachment.getUrl());
					BufferedImage image = ImageIO.read(attachurl);
					File imageOut = new File(imageDir, attachment.getFileName());
					String formatType = "jpg";
					if (attachment.getFileName().contains(".jpg")
							|| attachment.getFileName().contains("JPG")) {
						formatType = "jpg";
					} else if (attachment.getFileName().contains(".png")
							|| attachment.getFileName().contains(".PNG")) {
						formatType = "png";
					} else if (attachment.getFileName().contains(".gif")
							|| attachment.getFileName().contains(".GIF")) {
						formatType = "gif";
					}
					ImageIO.write(image, formatType, imageOut);

				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void generateToc(ConfluenceRPCHelper confluenceRPC,
			RemotePage page, String helpDir) {
		System.out.println("Generating Table of Contents.");
		System.out.println("Writing TOC for " + page.getTitle());
		StringWriter out = new StringWriter(8096);

		XmlStreamWriter writer = createXmlStreamWriter(out);

		writer.writeStartDocument("utf-8", "1.0"); //$NON-NLS-1$ //$NON-NLS-2$

		writer.writeStartElement("toc"); //$NON-NLS-1$
		writer.writeAttribute(
				"topic", helpDir + "/" + page.getTitle() + ".html"); //$NON-NLS-1$
		writer.writeAttribute("label", page.getTitle()); //$NON-NLS-1$

		RemotePageSummary[] rpsum = confluenceRPC.getChildrenPages(page);

		emitToc(writer, rpsum, confluenceRPC, helpDir);

		writer.writeEndElement(); // toc

		writer.writeEndDocument();
		writer.close();

		String tocContents = out.toString();

		File tocFile = new File(dest, page.getTitle() + "-toc.xml"); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$

		try {
			Writer tocwriter = new OutputStreamWriter(new BufferedOutputStream(
					new FileOutputStream(tocFile)), "UTF-8"); //$NON-NLS-1$
			try {
				tocwriter.write(tocContents);
			} finally {
				tocwriter.close();
			}
		} catch (IOException e) {
			String message = "Cannot write " + page.getTitle() + " tocFile"; //$NON-NLS-1$
			throw new BuildException(message, e);
		}
	}

	protected XmlStreamWriter createXmlStreamWriter(Writer out) {
		XmlStreamWriter writer = new DefaultXmlStreamWriter(out);
		return new FormattingXMLStreamWriter(writer);
	}

	private void emitToc(XmlStreamWriter writer, RemotePageSummary[] children,
			ConfluenceRPCHelper confluenceRPC, String helpDir) {
		for (RemotePageSummary item : children) {
			System.out.println("Writing TOC for " + item.getTitle());
			writer.writeStartElement("topic"); //$NON-NLS-1$

			writer.writeAttribute(
					"href", helpDir + "/" + item.getTitle() + ".html"); //$NON-NLS-1$ 
			writer.writeAttribute("label", item.getTitle()); //$NON-NLS-1$

			RemotePage page = confluenceRPC.getPage(item.getTitle());
			RemotePageSummary[] itemChildren = confluenceRPC
					.getChildrenPages(page);

			if (itemChildren.length != 0) {
				emitToc(writer, itemChildren, confluenceRPC, helpDir);
			}

			writer.writeEndElement(); // topic
		}
	}

	private String computeDefaultInternalLinkPattern() {
		String internalLinkPattern = wikiBaseUrl;
		if (!internalLinkPattern.endsWith("/")) { //$NON-NLS-1$
			internalLinkPattern += "/display/"; //$NON-NLS-1$
		}
		internalLinkPattern += "{0}"; //$NON-NLS-1$
		return internalLinkPattern;
	}

	protected String computeTitle(Space path) {
		return path.getTitle() == null ? path.getName() : path.getTitle();
	}

	private void generateContextHelp(ConfluenceRPCHelper confluenceRPC,
			RemotePage page, String helpDir) {
		System.out.println("Generating Context Help.");
		System.out.println("Writing context.xml for " + page.getTitle());
		StringWriter out = new StringWriter(8096);

		XmlStreamWriter writer = createXmlStreamWriter(out);

		writer.writeStartDocument("utf-8", "1.0"); //$NON-NLS-1$ //$NON-NLS-2$

		writer.writeStartElement("contexts"); //$NON-NLS-1$
		writer.writeAttribute("id", generateId(page.getTitle())); //$NON-NLS-1$

		RemotePageSummary[] rpsum = confluenceRPC.getChildrenPages(page);

		emitContext(writer, rpsum, confluenceRPC, helpDir);

		writer.writeEndElement(); // toc

		writer.writeEndDocument();
		writer.close();

		String tocContents = out.toString();

		File tocFile = new File(dest, page.getTitle() + "-context.xml"); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$

		try {
			Writer tocwriter = new OutputStreamWriter(new BufferedOutputStream(
					new FileOutputStream(tocFile)), "UTF-8"); //$NON-NLS-1$
			try {
				tocwriter.write(tocContents);
			} finally {
				tocwriter.close();
			}
		} catch (IOException e) {
			String message = "Cannot write " + page.getTitle() + " tocFile"; //$NON-NLS-1$
			throw new BuildException(message, e);
		}
	}

	private void emitContext(XmlStreamWriter writer,
			RemotePageSummary[] children, ConfluenceRPCHelper confluenceRPC,
			String helpDir) {
		for (RemotePageSummary item : children) {
			System.out.println("Writing Context for " + item.getTitle());
			writer.writeStartElement("context"); //$NON-NLS-1$

			writer.writeAttribute("id", generateId(item.getTitle()));
			writer.writeAttribute("title", item.getTitle()); //$NON-NLS-1$

			writer.writeStartElement("topic");
			writer.writeAttribute("href", helpDir + "/" + item.getTitle()
					+ ".html");
			writer.writeAttribute("label", item.getTitle());
			writer.writeEndElement(); // topic

			writer.writeEndElement(); // context

			RemotePage page = confluenceRPC.getPage(item.getTitle());
			RemotePageSummary[] itemChildren = confluenceRPC
					.getChildrenPages(page);

			if (itemChildren.length != 0) {
				emitContext(writer, itemChildren, confluenceRPC, helpDir);
			}

		}
	}

	private String generateId(String title) {
		final String ILLEGAL_CHARS = "-.(){}\\/?,!@#$%`~&*+=[]:;'\"";
		String id = title;

		id = id.replaceAll(" ", "_");
		StringBuffer buf = new StringBuffer(id.length());
		for (int i = 0; i < id.length() - 1; i++) {
			if ((ILLEGAL_CHARS.indexOf(String.valueOf(id.charAt(i))) >= 0)) {
				continue;
			}

			buf.append(id.charAt(i));
		}
		if (ILLEGAL_CHARS.indexOf(String.valueOf(id.charAt(id.length() - 1))) == -1) {
			buf.append(id.charAt(id.length() - 1));
		}
		id = buf.toString().replaceAll("__", "_");

		return id;
	}

	public String getWikiBaseUrl() {
		return wikiBaseUrl;
	}

	public void setWikiBaseUrl(String wikiBaseUrl) {
		this.wikiBaseUrl = wikiBaseUrl;
	}

	public List<Space> getPaths() {
		return spaces;
	}

	public void setPaths(List<Space> paths) {
		this.spaces = paths;
	}

	public File getDest() {
		return dest;
	}

	public void setDest(File dest) {
		this.dest = dest;
	}

	public String getLinkRel() {
		return linkRel;
	}

	public void setLinkRel(String linkRel) {
		this.linkRel = linkRel;
	}

	public boolean isMultipleOutputFiles() {
		return multipleOutputFiles;
	}

	public void setMultipleOutputFiles(boolean multipleOutputFiles) {
		this.multipleOutputFiles = multipleOutputFiles;
	}

	public boolean isFormatOutput() {
		return formatOutput;
	}

	public void setFormatOutput(boolean formatOutput) {
		this.formatOutput = formatOutput;
	}

	public boolean isNavigationImages() {
		return navigationImages;
	}

	public void setNavigationImages(boolean navigationImages) {
		this.navigationImages = navigationImages;
	}

	public String getPrependImagePrefix() {
		return prependImagePrefix;
	}

	public void setPrependImagePrefix(String prependImagePrefix) {
		this.prependImagePrefix = prependImagePrefix;
	}

	public String getDefaultAbsoluteLinkTarget() {
		return defaultAbsoluteLinkTarget;
	}

	public void setDefaultAbsoluteLinkTarget(String defaultAbsoluteLinkTarget) {
		this.defaultAbsoluteLinkTarget = defaultAbsoluteLinkTarget;
	}

	public List<Stylesheet> getStylesheets() {
		return stylesheets;
	}

	public boolean isUseInlineCssStyles() {
		return useInlineCssStyles;
	}

	public boolean isSuppressBuiltInCssStyles() {
		return suppressBuiltInCssStyles;
	}

	public boolean isXhtmlStrict() {
		return xhtmlStrict;
	}

	public boolean isEmitDoctype() {
		return emitDoctype;
	}

	public String getHtmlDoctype() {
		return htmlDoctype;
	}

	public String getHelpPrefix() {
		return helpPrefix;
	}

	public void setHelpPrefix(String helpPrefix) {
		this.helpPrefix = helpPrefix;
	}

	public void addStylesheet(Stylesheet stylesheet) {
		if (stylesheet == null) {
			throw new IllegalArgumentException();
		}
		stylesheets.add(stylesheet);
	}

	public void addSpace(Space space) {
		if (space == null) {
			throw new IllegalArgumentException();
		}
		spaces.add(space);
	}

	@Override
	protected MarkupLanguage createMarkupLanguage() throws BuildException {
		if (getMarkupLanguage() == null) {
			MarkupLanguage markupLanguage = new ConfluenceLanguage();
			if (getInternalLinkPattern() != null) {
				markupLanguage.setInternalLinkPattern(getInternalLinkPattern());
			}
			if (getMarkupLanguageConfiguration() != null) {
				markupLanguage.configure(getMarkupLanguageConfiguration());
			}
			return markupLanguage;
		}
		return super.createMarkupLanguage();
	}

	public String getHtmlFilenameFormat() {
		return htmlFilenameFormat;
	}

	public void setHtmlFilenameFormat(String htmlFilenameFormat) {
		this.htmlFilenameFormat = htmlFilenameFormat;
	}

	public boolean isFetchImages() {
		return fetchImages;
	}

	public void setFetchImages(boolean fetchImages) {
		this.fetchImages = fetchImages;
	}

	public void setPageAppendum(PageAppendum pageAppendum) {
		this.pageAppendum = pageAppendum;
	}

	public PageAppendum getPageAppendum() {
		return pageAppendum;
	}

	public void addPageAppendum(PageAppendum pageAppendum) {
		if (pageAppendum == null) {
			throw new IllegalArgumentException();
		}
		if (this.pageAppendum != null) {
			throw new BuildException(
					Messages.getString("WikiToDocTask_only_one_page_appendum")); //$NON-NLS-1$
		}
		this.pageAppendum = pageAppendum;
	}

	public File getTocFile() {
		return tocFile;
	}

	public void setTocFile(File tocFile) {
		this.tocFile = tocFile;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isGenerateUnifiedToc() {
		return generateUnifiedToc;
	}

	public void setGenerateUnifiedToc(boolean generateUnifiedToc) {
		this.generateUnifiedToc = generateUnifiedToc;
	}

	public String getTemplateExcludes() {
		return templateExcludes;
	}

	public void setTemplateExcludes(String templateExcludes) {
		this.templateExcludes = templateExcludes;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId; 
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}
