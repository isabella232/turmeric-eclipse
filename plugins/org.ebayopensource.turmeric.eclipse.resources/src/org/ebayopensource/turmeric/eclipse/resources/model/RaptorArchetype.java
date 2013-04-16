package org.ebayopensource.turmeric.eclipse.resources.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

public class RaptorArchetype {
	private String groupId;
	private String artifactId;
	private String description;
	private String version;
	private boolean successToLoad=true;
	private List<ArchetypeProperty> requiredProperties;

	public boolean getSuccessToLoad() {
		return successToLoad;
	}
	public void setSuccessToLoad(boolean successToLoad) {
		this.successToLoad = successToLoad;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getArtifactId() {
		return artifactId;
	}
	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public List<ArchetypeProperty> getArchetypePropertiesForUI() {
		return requiredProperties;
	}
	public void setRequiredProperties(Properties requiredProperties) {
		if (this.requiredProperties==null){
			initializeProperties(requiredProperties);
		}else if(this.requiredProperties.size()>0){
			//check whether related properties are already existed. if exists, then put its additional info into it.
			addAdditionalInfoForArchetypeProperties(requiredProperties);
		}
	}

	/**
	 * it will check whether related properties are already existed. 
	 * if exists, then put its additional info(validators & descriptions) into it.
	 * 
	 * @param properties : comes from M2eService.loadAdditionalProperties(RaptorArchetype), include validators & descriptions
	 */
	private void addAdditionalInfoForArchetypeProperties(Properties properties) {
		if (properties != null) {
			for (Iterator<Map.Entry<Object, Object>> it = properties.entrySet()
					.iterator(); it.hasNext();) {
				Map.Entry<?, ?> e = it.next();

				String key = (String)e.getKey();
				String propertyId = StringUtils.substringBetween(key, "", ".");

				addIfExisted(propertyId, e);
			}
		}
	}

	private void addIfExisted(String id, Entry<?, ?> e) {
		for (ArchetypeProperty property: this.requiredProperties){
			if (id.equals(property.getKey())){
				String key = (String)e.getKey();
				String value = (String)e.getValue();

				if (key.endsWith(".validator")&&!property.getValidators().contains(value)){
					property.setValidators(value);
				}

				if (key.endsWith(".description")){
					property.setDescription(value);
				}

				if (key.endsWith(".default")){
					property.setValue(value);
				}

				if (key.endsWith(".visible")){
					property.setVisible(Boolean.parseBoolean(value));
				}

				if (key.endsWith(".maxlength")){
					property.setMaxLength(Integer.parseInt(value));
				}
			}
		}

		if ("artifactId".equals(id)){
			ArchetypeProperty property = new ArchetypeProperty();
			property.setKey(id);
			property.setValue((String)e.getValue());

			this.requiredProperties.add(property);
		}
	}

	/**
	 * it will initialize archetype properties with properties' key and value, 
	 * should without validators & descriptions.
	 * 
	 * @param requiredProperties: it comes from M2eService.loadArchetypes(..), only have key and value.
	 */
	private void initializeProperties(Properties properties) {
		this.requiredProperties = new ArrayList<ArchetypeProperty>();
		if (properties != null) {
			for (Iterator<Map.Entry<Object, Object>> it = properties.entrySet()
					.iterator(); it.hasNext();) {
				Map.Entry<?, ?> e = it.next();
				ArchetypeProperty archetypeProperty = new ArchetypeProperty();
				archetypeProperty.setKey((String) e.getKey());
				archetypeProperty.setValue((String)e.getValue());

				this.requiredProperties.add(archetypeProperty);
			}
		}
	}

}