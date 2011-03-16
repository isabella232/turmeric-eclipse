/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.soatools.configtool;

import java.io.IOException;
import java.io.InputStream;

import org.ebayopensource.turmeric.eclipse.utils.xml.JDOMUtil;
import org.eclipse.core.resources.IFile;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;

/**
 * @author yualiu
 *
 */
public class XMLHelper {
	private InputStream input;
	private Document document;

	public XMLHelper(InputStream input) throws XMLOperationException{
		this.input = input;
		load();
	}
	
	private void load() throws XMLOperationException {
		try {
			document = JDOMUtil.readXML(input);
		} catch (IOException e) {
			throw new XMLOperationException(e);
		} catch (JDOMException e) {
			throw new XMLOperationException(e);
		}
	}

	public void replace(String value, InvokeUnit iu) throws XMLOperationException {
		Element ele = getElement(iu);
		if(null == ele){
			if(!iu.isAddIfNotExist()){
				throw new XMLOperationException("element " + iu.getXPath() + " not found");
			}
			ele = addElement(iu);
		}
		if(null == iu.getAttr()){
			ele.setText(value);
		}
		else{
			ele.setAttribute(iu.getAttr(), value);
		}
	}

	public void replace(Element value, InvokeUnit iu) throws XMLOperationException {
		Element parent = getElement(iu);
		if(null == parent){
			throw new XMLOperationException("element " + iu.getXPath() + " not found");
		}
		Element root = document.getRootElement();
		Namespace namespace = root.getNamespace();
		boolean removed = parent.removeChild(iu.getLeafName(), namespace);
		if(!removed && !iu.isAddIfNotExist()){
			throw new XMLOperationException("element " + iu.getXPath() + " not found");			
		}
		parent.addContent(value);

	}

	public boolean remove(InvokeUnit iu) throws XMLOperationException {
		Element parent = getElement(iu);
		if(null == parent){
			throw new XMLOperationException("element " + iu.getXPath() + " not found");
		}
		Element root = document.getRootElement();
		Namespace namespace = root.getNamespace();
		boolean removed = parent.removeChild(iu.getLeafName(), namespace);
		return removed;
	}

	public Element getElement(InvokeUnit iu) {
		Element root = document.getRootElement();
		Namespace namespace = root.getNamespace();
		String[] pathSplit = iu.getPathSplit();
		if(null == pathSplit){
			return root;
		}
		Element ele = root;
		for(String name : pathSplit){
			if(name.length() <= 0){
				continue;
			}
			Element child = ele.getChild(name, namespace);
			if(null == child){
				return null;
			}
			ele = child;
		}
		return ele;
	}

	private Element addElement(InvokeUnit iu) throws XMLOperationException {
		Element root = document.getRootElement();
		Namespace namespace = root.getNamespace();
		Element ele = root;
		for(String name : iu.getPathSplit()){
			if(name.length() <= 0){
				continue;
			}
			Element child = ele.getChild(name, namespace);
			if(null == child){
				child = new Element(name, namespace);
				ele.addContent(child);
				return child;
			}
			ele = child;
		}
		//shouldn't reach here
		throw new XMLOperationException("Element " + iu.getXPath() + " is eixsting");
	}

	public void save(IFile file) throws XMLOperationException {
		try {
			JDOMUtil.outputDocument(document, file);
		} catch (Exception e) {
			throw new XMLOperationException(e);
		}
	}

}
