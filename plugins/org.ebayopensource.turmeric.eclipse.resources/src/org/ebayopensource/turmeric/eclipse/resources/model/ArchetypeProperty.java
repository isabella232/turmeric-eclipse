package org.ebayopensource.turmeric.eclipse.resources.model;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;


public class ArchetypeProperty {
	private String key;
	private String value;
	private String defaultValue;
	private List<String> validators;
	private String description;
	private boolean visible=true;
	private List<String> versionList;
	private int maxlength=0;

	public ArchetypeProperty(){
		this.validators = new ArrayList<String>();
	}

	public int getMaxLength() {
		return maxlength;
	}

	public void setMaxLength(int length) {
		this.maxlength = length;
	}

	public List<String> getVersionList() {
		return versionList;
	}

	public void setRaptorPlatformVersionList(List<String> versionList) {
		this.versionList = versionList;
	}

	public boolean isVisible(){
		return visible;
	}
	public void setVisible(boolean value){
		this.visible = value;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public List<String> getValidators() {
		return validators;
	}
	public void setValidators(String validators) {
		StringTokenizer tokenizer = new StringTokenizer(
				StringUtils.lowerCase(validators), ","); //$NON-NLS-1$
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			this.validators.add(StringUtils.trim(token));
		}
	}
	public String getDescription() {
		return description==null?"":description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}