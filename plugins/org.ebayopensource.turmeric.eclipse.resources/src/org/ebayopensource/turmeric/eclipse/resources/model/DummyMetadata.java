package org.ebayopensource.turmeric.eclipse.resources.model;

public class DummyMetadata extends AbstractSOAMetadata {
	private String superVersion;
	@Override
	public String getMetadataFileName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSuperVersion() {
		// TODO Auto-generated method stub
		return superVersion;
	}
	public void setSuperVersion(String string) {
		// TODO Auto-generated method stub
		 superVersion=string;
	}

}
