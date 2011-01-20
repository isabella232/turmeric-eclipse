/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.ui.model;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.namespace.QName;

import org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil;


public class ImportTypeModel {
	private TypeParamModel typeModel;
	private boolean selected;
	private boolean error;
	private boolean unSupported;
	private String unSupportedReason;
	private Map<QName, ImportTypeModel> dependencies = new ConcurrentHashMap<QName, ImportTypeModel>();
	
	public static String UNSUPPORTED_NODE_ANY_IN_TYPE = "Type \"{0}\" contains an unsupported xs:any node. Please remove it from the type.";
	public static String UNSUPPORTED_ATTR_ANY_IN_TYPE = "Type \"{0}\" contains an unsupported anyAttribute attribute. Please remove it from type.";
	
	

	public ImportTypeModel(TypeParamModel type) {
		this.selected = true;
		this.error = false;
		this.typeModel = type;
		this.unSupported = false;
		this.unSupportedReason = "";
	}
	
	public void addDependency(QName qName, ImportTypeModel model){
		dependencies.put(qName, model);
	}

	public Map<QName, ImportTypeModel> getDependencies() {
		return dependencies;
	}
	public boolean isUnSupported() {
		return unSupported;
	}

	public void setUnSupported(boolean unSupported) {
		this.unSupported = unSupported;
	}

	public String getUnSupportedReason() {
		String content = StringUtil.formatString(unSupportedReason, typeModel
				.getTypeName());
		return content;
	}

	public void setUnSupportedReason(String unSupportedReason) {
		this.unSupportedReason = unSupportedReason;
	}

	public TypeParamModel getTypeModel() {
		return typeModel;
	}

	public void setTypeModel(TypeParamModel typeModel) {
		this.typeModel = typeModel;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isError() {
		return (error == true) || (unSupported == true);
	}

	public void setError(boolean isError) {
		this.error = isError;
	}

	public String getName() {
		return typeModel.getTypeName();
	}

	public String getNamespace() {
		return typeModel.getNamespace();
	}

	public String getDescription() {
		return typeModel.getDescription();
	}

	public void setName(String name) {
		typeModel.setTypeName(name);
	}

	public void setDescription(String description) {
		typeModel.setDescription(description);
	}

}