package org.ebayopensource.turmeric.eclipse.repositorysystem.test.prefs;

import org.ebayopensource.turmeric.eclipse.repositorysystem.RepositorySystemActivator;
import org.ebayopensource.turmeric.eclipse.repositorysystem.preferences.core.PreferenceConstants;
import org.ebayopensource.turmeric.eclipse.repositorysystem.preferences.core.PreferenceInitializer;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PreferenceInitializerTest {

	PreferenceInitializer prefs = null;
	
	@Before
    public void setUp() {
    	prefs = new PreferenceInitializer();
    }
    
	@After
	public void tearDown() {
		prefs = null;
	}
	
	@Test
	public void testInitializedDefaultPreferences() throws Exception {
		prefs.initializeDefaultPreferences();
		IEclipsePreferences prefs = RepositorySystemActivator.getDefault().getPreferences();
		String serviceLayers = prefs.get(PreferenceConstants.PREF_SERVICE_LAYERS, "");
		assertEquals("Preference store was not initialized", PreferenceConstants.getDefaultServiceLayers(), serviceLayers);
	}
}
