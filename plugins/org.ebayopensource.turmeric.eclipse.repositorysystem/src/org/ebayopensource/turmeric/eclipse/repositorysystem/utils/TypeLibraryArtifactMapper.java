package org.ebayopensource.turmeric.eclipse.repositorysystem.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TypeLibraryArtifactMapper {
	/*
	 * Global in memory map that keeps track of each type library and its corresponding artifact and group id
	 * The type libraries that are maintained in this map are the ones that are populated via the raptorSoa.properties
	 * TL's of the form "com.ebay.soa.typelib" and TL's present in teh workspace will not require this mapping
	 */
	private static final TypeLibraryArtifactMapper typeLibraryArtifactMapper = new TypeLibraryArtifactMapper();
	private HashMap<String, String> tLToArtifactMap = new HashMap<String, String>();

	private TypeLibraryArtifactMapper() {
	}

	public static TypeLibraryArtifactMapper getInstance() {
		return typeLibraryArtifactMapper;
	}

	public boolean addTLEntry(String typeLibrary, String artifact) {
		if ((!tLToArtifactMap.containsKey(typeLibrary)) && (typeLibrary != null)
				&& (artifact != null)) {
			//Details will be valid, no need to check for structure again.
			//Assumes that only valid details are added to the mapper
			tLToArtifactMap.put(typeLibrary, artifact);
			return true;
		}
		return false;
	}
	public List<String> getAllTypeLibraries(){
		List<String> allTypeLibraries = new ArrayList<String>();
		for(String typeLibraryFromSet:tLToArtifactMap.keySet()){
			allTypeLibraries.add(typeLibraryFromSet);
		}
		return allTypeLibraries;
		
	}

	public String getTLartifact(String typeLibrary) {
		if(tLToArtifactMap.containsKey(typeLibrary)){			
		 return (String) tLToArtifactMap.get(typeLibrary).split(":")[1];
		}
		return null;
	}
	public String getTLgroup(String typeLibrary) {
		if(tLToArtifactMap.containsKey(typeLibrary)){			
			 return (String) tLToArtifactMap.get(typeLibrary).split(":")[0];
			}
			return null;
	}
	public String getTLVersion(String typeLibrary) {
		if(tLToArtifactMap.containsKey(typeLibrary)){			
			 return (String) tLToArtifactMap.get(typeLibrary).split(":")[2];
			}
			return null;
	}

}
