package org.ebayopensource.turmeric.eclipse.maven.core.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.ebayopensource.turmeric.eclipse.config.repo.SOAConfigExtensionFactory;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.utils.io.IOUtil;
import org.ebayopensource.turmeric.eclipse.utils.xml.FreeMarkerUtil;
import org.ebayopensource.turmeric.eclipse.utils.xml.JDOMUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.filter.ElementFilter;
import org.jdom.input.SAXBuilder;

public class RaptorAppParser {
	public static String RAPTOR_APP_XML_TEMPLATE_NAME="webService_Template";
	private static final String ADMIN_NAME_KEY = "adminName";
	private static final String ARTIFACT_NAME_KEY = "artifactId";
	public static final String raptorAppXmlFilePath="/src/main/webapp/META-INF/raptor_app.xml";

	public static Document getSourceDocument(String adminName,
			String artifactId,String description) throws Exception {
		
		final String org = GlobalRepositorySystem.instanceOf()
				.getActiveRepositorySystem().getActiveOrganizationProvider()
				.getName();

		URL templateURL = null;
		templateURL=SOAConfigExtensionFactory.getXMLTemplate(org,
				RAPTOR_APP_XML_TEMPLATE_NAME);
		
		
		if (templateURL == null) {
			throw new IllegalArgumentException(
					"Can not find raptorApp.xml template for organization->" + org);
		}
		
		StringWriter writer = new StringWriter();
		Map<String, String> data = new HashMap<String, String>();

		data.put(ADMIN_NAME_KEY, adminName);
		data.put(ARTIFACT_NAME_KEY, artifactId);

		FreeMarkerUtil.generate(data, templateURL, "webxml", writer);

		String sourceString = writer.toString();

		Document sourceDoc = JDOMUtil.readXML(IOUtils
				.toInputStream(sourceString));

		return sourceDoc;
	}
	
	
	public static void addWebServiceElementsToRaptorAppXML(Document sourceDoc,
			InputStream target, IFile targetFile,String description) throws CoreException, IOException, JDOMException {
		try {
			Document targetDoc = addWebServiceElements(sourceDoc,target,description);
			IOUtil.writeTo(JDOMUtil.convertXMLToString(targetDoc), targetFile,
					new NullProgressMonitor());

		} finally {
			IOUtils.closeQuietly(target);
		}
		
		
	}
	public static String getAppNameFromRaptorApp(IProject targetWebProject) throws IOException, JDOMException {
		IFile targetWebFile = targetWebProject.getFile(raptorAppXmlFilePath);
		InputStream target;
		try {
			target = targetWebFile.getContents();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		Document targetDoc = null;
		String appName= "";
		try {
			targetDoc = JDOMUtil.readXML(target);
			IOUtils.closeQuietly(target);

			
			Element targetRoot = targetDoc.getRootElement();
			Element nameElement =targetRoot.getChild("name");
			appName=nameElement.getText();
		} finally {
			IOUtils.closeQuietly(target);
		}
		if(appName==null)appName="";
		return appName;
	}

	private static Document addWebServiceElements(Document sourceDoc,
			InputStream target,String description) throws IOException, JDOMException {
		Document targetDoc = null;
		try {
			targetDoc = JDOMUtil.readXML(target);
			IOUtils.closeQuietly(target);

			Element sourceRoot = sourceDoc.getRootElement();
			Element targetRoot = targetDoc.getRootElement();

			addContentToTargetDocument(sourceRoot, targetRoot, "webService");
			//addContentToTargetDocument(description, targetRoot, "description");
		} finally {
			IOUtils.closeQuietly(target);
		}
		return targetDoc;
	}
	private static boolean addContentToTargetDocument(String description,
			Element targetRoot, String nodeName) {
		Namespace targetNamespace = targetRoot.getNamespace();
		Element targetEle = targetRoot.getChild(nodeName);
		targetEle.setText(description);
		return true;
	}
	private static boolean addContentToTargetDocument(Element sourceRoot,
			Element targetRoot, String nodeName) {
		Namespace sourceNamespace = sourceRoot.getNamespace();
		

		Element sourceEle = sourceRoot.getChild(nodeName, sourceNamespace);
		if (sourceEle == null) {
			return false;
		}

		Namespace targetNamespace = targetRoot.getNamespace();
		Element targetEle = targetRoot.getChild("webService-List");
		if(targetEle==null){
			//add it here
			targetEle=new Element("webService-List");
		}
		int index = getNextNodeIndex(targetEle, nodeName);
		if (index == -1) {
			targetEle.addContent(copyElement(sourceEle, targetNamespace));
		} else {
			targetEle.addContent(index + 1, copyElement(sourceEle,
					targetNamespace));
		}

		return true;
		
	}
	private static Element copyElement(Element source, Namespace namespace) {
		Element current = new Element(source.getName());
		current.setText(source.getText());
		current.setNamespace(namespace);
		for (Object elementObj : source.getChildren()) {
			if (elementObj instanceof Element) {
				Element child = copyElement((Element) elementObj, namespace);
				current.addContent(child);
			}
		}
		return current;
	}
	private static int getNextNodeIndex(Element root, String nodeName) {
		List<Object> child = root.getChildren(nodeName);
		int index = -1;
		if (child == null) {
			return index;
		}
		for (Object objEle : child) {
			if (objEle instanceof Element == false) {
				continue;
			}
			int currIdx = root.indexOf((Content) objEle);
			if (index < currIdx) {
				index = currIdx;
			}
		}
		return index;
	}

}
