/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.core.model.typelibrary;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.namespace.QName;

import org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil;


/**
 * The Class ImportTypeModel.
 */
public class ImportTypeModel {
	private TypeParamModel typeModel;
	private boolean selected;
	private boolean error;
	private boolean unSupported;
	private String unSupportedReason;
	private Map<QName, ImportTypeModel> dependencies = new ConcurrentHashMap<QName, ImportTypeModel>();
	
	/** The UNSUPPORTE d_ nod e_ an y_ i n_ type. */
	public static String UNSUPPORTED_NODE_ANY_IN_TYPE = "Type \"{0}\" contains an unsupported xs:any node. Please remove it from the type.";
	
	/** The UNSUPPORTE d_ att r_ an y_ i n_ type. */
	public static String UNSUPPORTED_ATTR_ANY_IN_TYPE = "Type \"{0}\" contains an unsupported anyAttribute attribute. Please remove it from type.";
	
	

	/**
	 * Instantiates a new import type model.
	 *
	 * @param type the type
	 */
	public ImportTypeModel(TypeParamModel type) {
		this.selected = true;
		this.error = false;
		this.typeModel = type;
		this.unSupported = false;
		this.unSupportedReason = "";
	}
	
	/**
	 * Adds the dependency.
	 *
	 * @param qName the q name
	 * @param model the model
	 */
	public void addDependency(QName qName, ImportTypeModel model){
		dependencies.put(qName, model);
	}

	/**
	 * Gets the dependencies.
	 *
	 * @return the dependencies
	 */
	public Map<QName, ImportTypeModel> getDependencies() {
		return dependencies;
	}
	
	/**
	 * Checks if is un supported.
	 *
	 * @return true, if is un supported
	 */
	public boolean isUnSupported() {
		return unSupported;
	}

	/**
	 * Sets the un supported.
	 *
	 * @param unSupported the new un supported
	 */
	public void setUnSupported(boolean unSupported) {
		this.unSupported = unSupported;
	}

	/**
	 * Gets the un supported reason.
	 *
	 * @return the un supported reason
	 */
	public String getUnSupportedReason() {
		String content = StringUtil.formatString(unSupportedReason, typeModel
				.getTypeName());
		return content;
	}

	/**
	 * Sets the un supported reason.
	 *
	 * @param unSupportedReason the new un supported reason
	 */
	public void setUnSupportedReason(String unSupportedReason) {
		this.unSupportedReason = unSupportedReason;
	}

	/**
	 * Gets the type model.
	 *
	 * @return the type model
	 */
	public TypeParamModel getTypeModel() {
		return typeModel;
	}

	/**
	 * Sets the type model.
	 *
	 * @param typeModel the new type model
	 */
	public void setTypeModel(TypeParamModel typeModel) {
		this.typeModel = typeModel;
	}

	/**
	 * Checks if is selected.
	 *
	 * @return true, if is selected
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * Sets the selected.
	 *
	 * @param selected the new selected
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * Checks if is error.
	 *
	 * @return true, if is error
	 */
	public boolean isError() {
		return (error == true) || (unSupported == true);
	}

	/**
	 * Sets the error.
	 *
	 * @param isError the new error
	 */
	public void setError(boolean isError) {
		this.error = isError;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return typeModel.getTypeName();
	}

	/**
	 * Gets the namespace.
	 *
	 * @return the namespace
	 */
	public String getNamespace() {
		return typeModel.getNamespace();
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return typeModel.getDescription();
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		typeModel.setTypeName(name);
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		typeModel.setDescription(description);
	}

}