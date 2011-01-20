/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.mylyn.wikitext.confluence.core.tasks;

public  class Space {
	private String title;

	private String name;

	private boolean generateToc = false;

	private boolean includeInUnifiedToc = true;

	private String tocParentName;
	
	private String helpDir;
		
	private boolean generateContextHelp = false;
	
	public boolean isGenerateContextHelp() {
		return generateContextHelp;
	}

	public void setGenerateContextHelp(boolean generateContextHelp) {
		this.generateContextHelp = generateContextHelp;
	}


	public String getHelpDir() {
		return helpDir;
	}

	public void setHelpDir(String helpDir) {
		this.helpDir = helpDir;
	}

	private String pageName;
	
	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isGenerateToc() {
		return generateToc;
	}

	public void setGenerateToc(boolean generateToc) {
		this.generateToc = generateToc;
	}

	public boolean isIncludeInUnifiedToc() {
		return includeInUnifiedToc;
	}

	public void setIncludeInUnifiedToc(boolean includeInUnifiedToc) {
		this.includeInUnifiedToc = includeInUnifiedToc;
	}

	public String getTocParentName() {
		return tocParentName;
	}

	public void setTocParentName(String tocParentName) {
		this.tocParentName = tocParentName;
	}

	@Override
	public String toString() {
		String s = name;
		if (tocParentName != null) {
			s = tocParentName + '/' + s;
		}
		return s;
	}
}
