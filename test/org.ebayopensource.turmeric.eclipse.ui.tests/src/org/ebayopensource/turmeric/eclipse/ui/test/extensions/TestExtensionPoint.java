package org.ebayopensource.turmeric.eclipse.ui.test.extensions;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestExtensionPoint {

	@Test
	public void testProjectWizardProviderExistsInRegistry() {
		IExtensionPoint extension = lookupProjectWizardProviderExtension();
		assertNotNull("Unable to find the projectWizardProvider extension point.", extension);
	}

	private IExtensionPoint lookupProjectWizardProviderExtension() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint extension  = registry.getExtensionPoint("org.ebayopensource.turmeric.eclipse.ui.projectWizardProvider");
		return extension;
	}
	
	@Test
	public void testRetrieveContributedExtensions() {
		IExtension[] extension = getallExtensions();
		assertTrue(extension.length > 0);
	}
	
	@Test
	public void testContributedExtensionId() {
		IExtension[] extension = getallExtensions();
		IExtension ext = extension[0];
		assertEquals("testProjectProvider", ext.getSimpleIdentifier());
	}

	private IExtension[] getallExtensions() {
		IExtensionPoint extensionPoint = lookupProjectWizardProviderExtension();
		IExtension extension[] = extensionPoint.getExtensions();
		return extension;
	}
	
	@Test
	public void testConfigurationElements() {
		IExtension[] extension = getallExtensions();
		IExtension ext = extension[0];
		IConfigurationElement[] elms = ext.getConfigurationElements();
		assertTrue(elms.length > 0);
	}
	
	@Test
	public void testProviderElement() {
		IConfigurationElement celm = firstConfigElement();
		String elemName = celm.getName();
		assertEquals("provider", elemName);
	}

	private IConfigurationElement firstConfigElement() {
		IExtension[] extension = getallExtensions();
		IExtension ext = extension[0];
		IConfigurationElement[] elms = ext.getConfigurationElements();
		IConfigurationElement celm = elms[0];
		return celm;
	}
	
	@Test
	public void testProviderElementNameAttribute() {
		IConfigurationElement element = firstConfigElement();
		String name = element.getAttribute("name");
		assertNotNull("Could not find attribute", name);
	}
	
	@Test
	public void testProviderElementIdAttribute() {
		IConfigurationElement element = firstConfigElement();
		String id = element.getAttribute("id");
		assertNotNull("Could not find attribute", id);
	}
	
	@Test
	public void testProviderElementHasChildren() {
		IConfigurationElement element = firstConfigElement();
		assertTrue(element.getChildren().length > 0);
	}
	
	@Test
	public void testWizardElement() {
		IConfigurationElement wizard = getWizard();
		assertNotNull("Unable to find child element, wizard", wizard);
	}
	
	private IConfigurationElement locateElement(String elName, IConfigurationElement[] children) {
	    for(IConfigurationElement elm : children) {
	    	if (elName.equals(elm.getName())) {
	    		return elm;
	    	}
	    }
	    return null;
	}
	
	@Test
	public void testWizardType() {
		IConfigurationElement wizard = getWizard();
		String type  = wizard.getAttribute("type");
		assertEquals("Wrong type.", "Service", type);
	}

	private IConfigurationElement getWizard() {
		IConfigurationElement element = firstConfigElement();
		IConfigurationElement children[] = element.getChildren();
		IConfigurationElement wizard = locateElement("wizard", children);
		return wizard;
	}
	
	@Test
	public void testWizardChildren() {
		IConfigurationElement wizard = getWizard();
		IConfigurationElement[] children = wizard.getChildren();
		assertTrue(children.length > 0);
	}
	
	
	@Test
	public void testPageExists() {
		IConfigurationElement wizard = getWizard();
		IConfigurationElement[] children = wizard.getChildren();
		IConfigurationElement page = locateElement("page", children);
		assertNotNull(page);
	}
	
	@Test
	public void testPageClass() {
		IConfigurationElement wizard = getWizard();
		IConfigurationElement[] children = wizard.getChildren();
		IConfigurationElement page = locateElement("page", children);
		assertNotNull(page);
		String classId = page.getAttribute("class");
		assertEquals("org.ebayopensource.turmeric.eclipse.ui.test.extensions.MockServicePage", classId);
	}
	
	@Test
	public void testPageId() {
		IConfigurationElement wizard = getWizard();
		IConfigurationElement[] children = wizard.getChildren();
		IConfigurationElement page = locateElement("page", children);
		assertNotNull(page);
		String id = page.getAttribute("id");
		assertEquals("testProjectPage1", id);
	}

}
