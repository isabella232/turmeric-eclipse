/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.functional.test.ft.typelib;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.core.model.typelibrary.ImportTypeModel;
import org.ebayopensource.turmeric.eclipse.core.model.typelibrary.TypeParamModel;
import org.ebayopensource.turmeric.eclipse.functional.test.AbstractTestCase;
import org.ebayopensource.turmeric.eclipse.functional.test.SoaTestConstants;
import org.ebayopensource.turmeric.eclipse.test.util.DialogMonitor;
import org.ebayopensource.turmeric.eclipse.test.util.FunctionalTestHelper;
import org.ebayopensource.turmeric.eclipse.test.util.ZipExtractor;
import org.ebayopensource.turmeric.eclipse.test.utils.TLUtil;
import org.ebayopensource.turmeric.eclipse.test.utils.WsdlUtilTest;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.TypeLibraryUtil;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.XSDUtils;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.wsdl.WSDLUtil;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author fjoy
 *
 */
public class ImportExportTypesTest extends AbstractTestCase {
	
	/*
	 * Create a type library with name = TYPELIBRARY_NAME1 and import types from sample xsd and wsdl file
	 * 
	 */
	
	String wsdlLocDir = WSDLUtil.getPluginOSPath("org.ebayopensource.turmeric.eclipse.functional.test","data/extractedData");
	String sourceXSDFile = wsdlLocDir + "/xsd/EmployeeTypeImport.xsd";
	String sourceWSDLFile = wsdlLocDir + "/JunitEndTestImport.wsdl";
	protected static final SOALogger logger = SOALogger.getLogger();
	static DialogMonitor monitor;
	
	static String dataDirectory = WsdlUtilTest.getPluginOSPath(
			SoaTestConstants.PLUGIN_ID,"data");
	@BeforeClass
	public static void setUpBefore(){
		
		ZipExtractor zip = new ZipExtractor();
		zip.extract(dataDirectory+"/xsd.zip",dataDirectory +"/extractedData");
		
	}
	
	@Before
	public void setUp() throws Exception {
		
		monitor = new DialogMonitor();
		monitor.startMonitoring();
		createTypeLibrary();
		FunctionalTestHelper.ensureM2EcipseBeingInited();
	}
	
	@After
	public void tearDown() throws Exception {
		super.cleanupWorkspace();
		monitor.stopMonitoring();
		monitor = null;
	}
	
	public void createTypeLibrary() throws Exception {
		FunctionalTestHelper.ensureM2EcipseBeingInited();
		
		TypeLibSetUp.setup();
		Assert.assertTrue(TypeLibSetUp.TYPELIBRARY_NAME1
				+ " -- TypeLibrary Creation failed",
				TLUtil.createTypeLibrary(TypeLibSetUp.TYPELIBRARY_NAME1, "1.0.0", "COMMON",TypeLibSetUp.TYPELIB_LOCATION));
	}
	
	@Test
	public void dummyTest() throws Exception {
		
	}
	
	@Test
	@Ignore
	public void testImportTypeFromXSD() throws Exception {
		String type = "EmployeeType";
		
		System.out.println("** testImportTypeFromXSD **");		 
		List<ImportTypeModel> types = new ArrayList<ImportTypeModel>();
		types = XSDUtils.getInstance().extractTypeDefinitionFromFile(sourceXSDFile);
		Assert.assertNotNull("No types in the input XSD file", types);
		String tlNamespace = TLUtil.getTargetNamespace(TLUtil.functionDomain);
		for (ImportTypeModel  model : types) {
			model.getTypeModel().setNamespace(tlNamespace);
		}
		TypeLibraryUtil.importTypesToTypeLibrary(types,TypeLibSetUp.TYPELIBRARY_NAME1,ProgressUtil.getDefaultMonitor(null));
		String typeinfoFile = TypeLibSetUp.TYPELIB_LOCATION + File.separator + 
		TypeLibSetUp.TYPELIBRARY_NAME1 + File.separator + "gen-meta-src" + File.separator +
		"META-INF" + File.separator + TypeLibSetUp.TYPELIBRARY_NAME1+File.separator+"TypeInformation.xml";
		File f = new File(typeinfoFile);
//		Verify TI.xml		
		String s = FileUtils.readFileToString(f);
		
		
		Assert.assertTrue("TypeInformation.xml is not updated with new version of TL", 
				s.contains(type));
		String xsdFileName = TypeLibSetUp.TYPELIB_LOCATION + File.separator + 
		TypeLibSetUp.TYPELIBRARY_NAME1 + File.separator + "meta-src" + File.separator +
		"types"+ File.separator + TypeLibSetUp.TYPELIBRARY_NAME1 + File.separator + "EmployeeType" + ".xsd";
		File xsdFile = new File(xsdFileName);
		Assert.assertTrue(".xsd of the type   " +type+" is not created", xsdFile.exists());
		
		
		
	}
	
	@Test
	@Ignore
	public void testImportTypeFromWSDL() throws Exception {
		System.out.println("** testImportTypeFromXSD **");		 
		List<ImportTypeModel> types = new ArrayList<ImportTypeModel>();
		types = XSDUtils.getInstance().extractTypeDefinitionFromFile(sourceWSDLFile);
		Assert.assertFalse("No types in the input XSD file", types.isEmpty());
		String tlNamespace = TLUtil.getTargetNamespace(TLUtil.functionDomain);
		for (ImportTypeModel model : types) {
			model.getTypeModel().setNamespace(tlNamespace);
		}
		TypeLibraryUtil.importTypesToTypeLibrary(types,TypeLibSetUp.TYPELIBRARY_NAME1,ProgressUtil.getDefaultMonitor(null));
		String typeinfoFile = TypeLibSetUp.TYPELIB_LOCATION + File.separator + 
		TypeLibSetUp.TYPELIBRARY_NAME1 + File.separator + "gen-meta-src" + File.separator +
		"META-INF" + File.separator + TypeLibSetUp.TYPELIBRARY_NAME1+File.separator+"TypeInformation.xml";
		File f = new File(typeinfoFile);
//		Verify TI.xml		
		String s = FileUtils.readFileToString(f);
		
		for (ImportTypeModel model : types) {
		String typeName = model.getTypeModel().getTypeName();
		String xsdFileName = TypeLibSetUp.TYPELIB_LOCATION + File.separator + 
		TypeLibSetUp.TYPELIBRARY_NAME1 + File.separator + "meta-src" + File.separator +
		"types"+ File.separator + TypeLibSetUp.TYPELIBRARY_NAME1 + File.separator + typeName + ".xsd";
		File xsdFile = new File(xsdFileName);
		Assert.assertTrue(".xsd of the type " + typeName + "is not created", xsdFile.exists());
		}
		
		
	}
	
	public List<TypeParamModel> getSelectedType(List<ImportTypeModel> types) {
		List<TypeParamModel> selectedTypeList = new ArrayList<TypeParamModel>();
		for (ImportTypeModel type : types) {
			if (type.isSelected()== true && type.isUnSupported()==true && type.isError()==true) {
				String remtype = type.getTypeModel().getTypeName();
				types.remove(type);
			}			
		}
		for (ImportTypeModel type : types) {
			selectedTypeList.add(type.getTypeModel());
		}
		return selectedTypeList;
	}
	
	@AfterClass
	public static void deInit(){
		
		ensureClean(dataDirectory +"/extractedData");
	}
	
}
