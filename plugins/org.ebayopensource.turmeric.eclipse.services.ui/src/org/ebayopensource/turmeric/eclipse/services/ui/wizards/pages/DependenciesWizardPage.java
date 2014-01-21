/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.services.ui.wizards.pages;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.maven.core.model.MavenAssetInfo;
import org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenEclipseUtil;
import org.ebayopensource.turmeric.eclipse.registry.models.ClientAssetModel;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAHelpProvider;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.SOAGlobalRegistryAdapter;
import org.ebayopensource.turmeric.eclipse.repositorysystem.utils.RepositoryUtils;
import org.ebayopensource.turmeric.eclipse.resources.model.AssetInfo;
import org.ebayopensource.turmeric.eclipse.resources.model.ProjectInfo;
import org.ebayopensource.turmeric.eclipse.resources.util.MarkerUtil;
import org.ebayopensource.turmeric.eclipse.ui.components.DependencyListEditor;
import org.ebayopensource.turmeric.eclipse.ui.components.DependencyListEditor.IDependencyLazyLoader;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.ProjectUtils;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * The Class DependenciesWizardPage.
 *
 * @author smathew
 * 
 * The project and library dependency page This page was used for both project
 * and library dependency, on the raptor branch it was used on RIDE 2.0.0 to 
 * show type library dependencies and suggestions, from later versions of RIDE it
 * will not be  apart of the consumer creation process.
 * 
 * 
 */
public class DependenciesWizardPage extends WizardPage implements IWizardPage {
	private DependencyListEditor projectsEditor;
	
	private DependencyListEditor libraryEditor;
	Map<String,Set<String>> packageLibMap = new HashMap<String,Set<String>>();
	private TableViewer viewer;
	private Button copyToCLipboard;
	
	/**
	 * Instantiates a new dependencies wizard page.O
	 *
	 * @param projectType the project type
	 */
	public DependenciesWizardPage(final String projectType) {
		super("newSOAServiceProjectDependenciesWizardPage");
		setTitle("Library Dependencies");
		setDescription(	"This wizard lets you add all library dependencies for your interface project. Your wsdl is associated with some type libraries."+
				"Please read the recommended action and act accordingly.");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
//		if(visible){
//			ServiceFromExistingWSDLWizardPage page = (ServiceFromExistingWSDLWizardPage) this.getWizard().getPage("newSOAServiceProjectFromWSDLWizardPage");
//		resolveAllCandidateLibraries(getTypeLibArtifactsToAdd(page.typeLibNameAndPackage, page.getAdminName()));
//
//
//
//		final String newLine = System.getProperty("line.separator");
//
//		StringBuilder guidlinesText = new StringBuilder();
//		ModelProvider.INSTANCE.clearSplitPackageDetails();
//		for (String typeLibrary: page.typeLibNameAndPackage.keySet()){
//		Set<String> librarySet =packageLibMap.get(page.typeLibNameAndPackage.get(typeLibrary));
//		guidlinesText.append("Type Library: "+typeLibrary);
//		String packageGen = page.typeLibNameAndPackage.get(typeLibrary);
//		guidlinesText.append(newLine);
//		guidlinesText.append("Package: "+packageGen);
//		String action ="";
//		if((librarySet==null)||(librarySet.size()==0)){
//			action=typeLibrary+" is not yet uploaded on ebaycentral repo. Please do so, and add this dependency in your interface to avoid regeneration of types in your bundle.";
//			String action2="For more details follow http:\\\\short\\raptorTL";
//			guidlinesText.append(newLine);
//			guidlinesText.append("Action: "+action +action2);
//			ModelProvider.INSTANCE.addSplitPackageDetails(typeLibrary,packageGen,action);
//			ModelProvider.INSTANCE.addSplitPackageDetails("","",action2);
//		}
//		else{
//			action=typeLibrary+ " should be added as a dependency to your interface project.";
//			String action2 = "Please check and follow the steps at http:\\\\short\\addTypeLib. ";
//			String action3= "One of these bundles could contain your type library. " ;
//
//			guidlinesText.append(newLine);
//			guidlinesText.append("Action: "+action+action2+action3);
//			ModelProvider.INSTANCE.addSplitPackageDetails(typeLibrary,packageGen,action);
//			ModelProvider.INSTANCE.addSplitPackageDetails("","",action2);
//			ModelProvider.INSTANCE.addSplitPackageDetails("","",action3);
//			for (String libDetail:librarySet){			
//				ModelProvider.INSTANCE.addSplitPackageDetails("","","     "+libDetail);
//				guidlinesText.append(newLine);
//				guidlinesText.append("       >>"+libDetail);
//
//			}
//		}
//		guidlinesText.append(newLine);
//		ModelProvider.INSTANCE.addSplitPackageDetails("","","");
//		}
//		ModelProvider.INSTANCE.setContentsForClipBoard(guidlinesText.toString());
//		 viewer.setInput(ModelProvider.INSTANCE.getSplitPackageDetailss());
//		}

	}
	private void resolveAllCandidateLibraries(Set<String> candidates){
		for (String candidate:candidates){
			try{
			MavenEclipseUtil.resolveFromArtifactData(candidate);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
//	private Set<String> getTypeLibArtifactsToAdd(Map<String,String> typeLibNameAndPackage, String adminName){
//		//preparing the inputs to split package service
//		Set<String> allKnownTls=null;
//		try {
//			allKnownTls = SOAGlobalRegistryAdapter.getInstance().getGlobalRegistry().getAllTypeLibrariesNames();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		Set<String> packageList = new HashSet<String>();
//		StringBuilder toBeAppended=new StringBuilder();
//		for (String typeLibrary: typeLibNameAndPackage.keySet()){
//			{
//				//unKnown, so need to call split package service on this
//				packageList.add(typeLibNameAndPackage.get(typeLibrary));
//			}
//		}
//		//Project is not yet created so assum bundle name  to be default
//		String bundleName = "com.ebay.soa.interface."+adminName;
//		//If no new packages, then no need to call split package service and find the bundles.
//		JSONObject response = null;
//		try {
//			response = ProjectUtils.callSplitPackageService(bundleName, packageList,true);
//		} catch (HttpException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (JSONException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//
//
//		if(response!=null)
//		{
//			Iterator k =response.keys();
//
//		while (k.hasNext()){
//			 StringBuilder errorMessage=new StringBuilder();
//			Map<String,String> bundleVersion = new HashMap<String, String>();
//			String ErrorPackageName = (String) k.next();
//			JSONArray bundlesList = null;
//			//Removing same bundle conflicts
//			try {
//				bundlesList = response.getJSONArray(ErrorPackageName);
//				for (int i = 0; i < bundlesList.length(); i++) {
//					JSONObject object = bundlesList.getJSONObject(i);
//					String conflictBundleName = object.getString("bundleName");
//					String conflictingVersion = object.getString("version");
//					if(conflictBundleName.matches(bundleName+"\\-(\\d+\\.)?(\\d+\\.)?(\\*|\\d+)$")){							
//						continue;
//						//Ignoring Same Bundles
//					}
//					bundleVersion.put(conflictBundleName, conflictingVersion);
//				}
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//
//			//If valid conflicts were found for this package
//			if(bundleVersion.size()>0){
//				//display errors if any, for each erroneous bundle
//				errorMessage.append("Split package errors detected for package "+ErrorPackageName
//						+ " with the following bundles:");
//				for(String bundle:bundleVersion.keySet()){
//
//					Set<String> orig = packageLibMap.get(ErrorPackageName);
//					if(orig==null){orig = new HashSet();}
//					Pattern p = Pattern.compile("^(.*)\\-(\\d+\\.)?(\\d+\\.)?(\\*|\\d+)$");
//					Matcher m = p.matcher(bundle);
//					String versionLessBundle = bundle;
//					if(m.find()){
//						//Version available, strip it and use the rest as groupid:artifactid
//						versionLessBundle = m.group(1);
//					}
//					int index =versionLessBundle.lastIndexOf(".");
//					if(index!=-1){
//						//WATCHOUT
//						//Invalid group name artifact name other wise, so ignore
//					String groupid =versionLessBundle.substring(0, index);
//					String artifactID = versionLessBundle.substring(index+1);
//					orig.add(groupid+":"+artifactID+":"+bundleVersion.get(bundle));
//					packageLibMap.put(ErrorPackageName, orig);
//					}
//					else{
//						System.out.println("Invalid artifact");
//					}
//				}
//
//			//End of valid conflicts processing
//			}
//		//End of Each PAckage Processing
//		}
//		//End of  Not null response processing
//		}
//		Set<String> toWrite = new HashSet<String>();
//		for (String typeLibrary: typeLibNameAndPackage.keySet()){
//			Set <String> toWriteLet =packageLibMap.get(typeLibNameAndPackage.get(typeLibrary));
//			if(toWriteLet!=null)
//			for (String entry: toWriteLet){
//				if((entry!=null)&&(!entry.isEmpty()))
//				toWrite.add(entry+":"+typeLibrary);
//			}
//
//		}
//		return toWrite;
//	}
//	public void finished(){
//		Set<String> libraries = getLibraries();
//		if(libraries.size()==0)return;
//		Set<String> filteredTypeLibraries = new HashSet<String>();
//		if (!(this.getWizard().getPreviousPage(this) instanceof ServiceFromExistingWSDLWizardPage) ){
//			return;
//		}
//		ServiceFromExistingWSDLWizardPage page = (ServiceFromExistingWSDLWizardPage) this.getWizard().getPreviousPage(this);
//		for (String typeLibrary: page.typeLibNameAndPackage.keySet()){
//			Set <String> toWriteLet =packageLibMap.get(page.typeLibNameAndPackage.get(typeLibrary));
//			if(toWriteLet!=null)
//			for (String entry: toWriteLet){
//				if((entry!=null)&&(!entry.isEmpty())){
//						String [] typeDetails =entry.split(":");
//						for (String selLib:libraries){
//							String [] libDetails = selLib.split(":");
//							if((typeDetails.length!=3)||(libDetails.length!=4))
//							continue;
//							if((typeDetails[1].equalsIgnoreCase(libDetails[1])) //art id
//									&&(typeDetails[0].equalsIgnoreCase(libDetails[0]))//group id
//									&&(typeDetails[2].equalsIgnoreCase(libDetails[3]))) //version
//							{
//								filteredTypeLibraries.add(entry+":"+typeLibrary);
//							}
//						}
//				}
//
//			}
//
//		}
//		//updat raptor file with filtered libraries
//		updateRaptorProperties(filteredTypeLibraries);
//		//Invalidate registry
//		SOAGlobalRegistryAdapter.getInstance().invalidateRegistry();
//		try {
//			SOAGlobalRegistryAdapter.getInstance().getGlobalRegistry();
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
//
//
//	}
//	private void updateRaptorProperties(Set<String> toWrite) {
//		String propertiesFileLocation= WorkspaceUtil.getRaptorSoaPropertiesLocation();
//		String updatedTipLibArtifacts=null;
//		Properties properties = new Properties();
//		File home = new File(propertiesFileLocation);
//		if(!home.exists()){
//				home.mkdir();
//		}
//		File RaptorPlatformFile = new File(home,"raptorSoa.properties");
//		if(RaptorPlatformFile.exists()){
//			FileInputStream in=null;
//			try {
//				in = new FileInputStream(RaptorPlatformFile);
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			}
//			try {if(in!=null)
//				properties.load(in);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}finally{
//				try {
//					in.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//			updatedTipLibArtifacts=properties.getProperty("TypeLibArtifacts");
//		}
//		if(updatedTipLibArtifacts==null)updatedTipLibArtifacts="";
//		for(String entry:toWrite){
//			if(!updatedTipLibArtifacts.contains(entry)){
//				//make the netry
//				if( !updatedTipLibArtifacts.isEmpty())updatedTipLibArtifacts+=",";
//				updatedTipLibArtifacts+=entry;
//			}
//		}
//		properties.setProperty("TypeLibArtifacts", updatedTipLibArtifacts);
//		OutputStream o =null;
//		try {
//			o=new FileOutputStream(RaptorPlatformFile);
//			properties.store(o, "Properties for soa on RIDE");
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}finally{
//			if (o!=null){
//				try {
//					o.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//
//	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createControl(final Composite parent) {

		Set<? extends AssetInfo> projects = new TreeSet<ProjectInfo>();
		try {
			projects = RepositoryUtils.getProjectInfoProjectsFromWorkSpace();
		} catch (Exception e) {
			SOALogger.getLogger().error(e);
			throw new RuntimeException(e);
		}

		final Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, true));

		IDependencyLazyLoader dependencyLazyLoader = new IDependencyLazyLoader() {		
			@Override
			public Set<? extends AssetInfo> getAlreadyAddedLibraries() {
				return null;
			}

			@Override
			public Set<? extends AssetInfo> getLibs() {

				try {
					return GlobalRepositorySystem.instanceOf()
							.getActiveRepositorySystem().getAssetRegistry()
							.getAllLibraries();
				} catch (Exception e) {
					SOALogger.getLogger().error(e);
					UIUtil.showErrorDialog(null, e);
				}
				return null;
			}
		};
		final Composite libraryComposite = new Composite(container, SWT.NULL);
		libraryComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		libraryEditor = new DependencyListEditor("Library Dependencies:",
				libraryComposite,dependencyLazyLoader, null, DependencyListEditor.DEPENDENCY_TYPE_LIBRARY);
		setControl(container);

		Label seperator = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		seperator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Label GuideLinesHeader = new Label(container,SWT.LEFT|SWT.WRAP);
		GuideLinesHeader.setText("Split Package Validation for packages contributed by type libraries:");
		createViewer(container);
		createCopyToClipBoardButton(container);
		

		

		

		UIUtil.getHelpSystem().setHelp(container,
				GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
				.getHelpProvider().getHelpContextID(ISOAHelpProvider.PAGE_SERVICE_DEPENDENCIES));

	}
	private void createCopyToClipBoardButton(Composite container){
		copyToCLipboard = new Button(container, SWT.PUSH|SWT.LEFT);
		copyToCLipboard.setText("Copy to Clipboard");
		copyToCLipboard.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					String toTransfer =ModelProvider.INSTANCE.getContentsForClipBoard();
					Clipboard cb = new Clipboard(Display.getDefault());
					 TextTransfer textTransfer = TextTransfer.getInstance();
					    cb.setContents(new Object[] { toTransfer },
					        new Transfer[] { textTransfer });

				} catch (Exception e1) {
					SOALogger.getLogger().error(e1);
					UIUtil.showErrorDialog(e1);
				}
			}
		});
	}
	private void createViewer(Composite container) {
		// TODO Auto-generated method stub
		viewer = new TableViewer(container, SWT.MULTI | SWT.H_SCROLL
		        | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER );
		    createColumns(container, viewer);
		    final Table table = viewer.getTable();
		    table.setHeaderVisible(true);
		    table.setLinesVisible(true);

		    viewer.setContentProvider(new ArrayContentProvider());
		    viewer.setInput(ModelProvider.INSTANCE.getSplitPackageDetailss());

		    // Layout the viewer
		    GridData gridData = new GridData();
		    gridData.verticalAlignment = GridData.FILL;
		    gridData.horizontalSpan = 2;
		    gridData.grabExcessHorizontalSpace = true;
		    gridData.grabExcessVerticalSpace = true;
		    gridData.horizontalAlignment = GridData.FILL;
		    gridData.heightHint=200;

		    viewer.getControl().setLayoutData(gridData);
	}

	private void createColumns(Composite container, TableViewer viewer2) {
		String[] titles = { "Package","Type Library","Action" };
	    int[] bounds = { 250, 150, 300 };

	    // First column is for the Package
	    TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
	    col.setLabelProvider(new ColumnLabelProvider() {
	      @Override
	      public String getText(Object element) {
	        SplitPackageDetails p = (SplitPackageDetails) element;
	        return p.getPackageGen();
	      }
	    });

	    // Second column is for the Type Library
	     col = createTableViewerColumn(titles[1], bounds[1], 1);
	    col.setLabelProvider(new ColumnLabelProvider() {
	      @Override
	      public String getText(Object element) {
	        SplitPackageDetails p = (SplitPackageDetails) element;
	        return p.getTypeLibrary();
	      }
	    });


	    // Now the Action
	    col = createTableViewerColumn(titles[2], bounds[2], 2);
	    col.setLabelProvider(new ColumnLabelProvider() {
	      @Override
	      public String getText(Object element) {
	        SplitPackageDetails p = (SplitPackageDetails) element;
	        return p.getAction();
	      }
	    });
	}

	private TableViewerColumn createTableViewerColumn(String title, int bound,
			int j) {
		 final TableViewerColumn viewerColumn = new TableViewerColumn(viewer,
			        SWT.NONE);
			    final TableColumn column = viewerColumn.getColumn();
			    column.setText(title);
			    column.setWidth(bound);
			    column.setResizable(true);
			    column.setMoveable(true);
			    return viewerColumn;
	}

	/**
	 * Gets the libraries.
	 *
	 * @return the libraries
	 */
	public Set<String> getLibraries() {
		Set<String> result = new HashSet<String>();
		for (final AssetInfo info : libraryEditor.getItems()) {
			result.add(info.getUniqueID());
		}
		return result;
	}

	/**
	 * Gets the projects.
	 *
	 * @return the projects
	 */
	public Set<String> getProjects() {
		Set<String> result = new HashSet<String>();
		for (final AssetInfo info : projectsEditor.getItems()) {
			result.add(info.getUniqueID());
		}
		return result;
	}
}
class SplitPackageDetails{
	private String typeLibrary;
	private String packageGen;
	private String action;

	public SplitPackageDetails(String typeLibrary, String packageGen,
			String action) {
		this.typeLibrary = typeLibrary;
		this.packageGen = packageGen;
		this.action = action;
	}
	public String getTypeLibrary() {
		return typeLibrary;
	}
	public void setTypeLibrary(String typeLibrary) {
		this.typeLibrary = typeLibrary;
	}
	public String getPackageGen() {
		return packageGen;
	}
	public void setPackageGen(String packageGen) {
		this.packageGen = packageGen;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}

}
enum ModelProvider {
	  INSTANCE;

	  private List<SplitPackageDetails> splitPackageDetailsList;
	  private String contentsForClipBoard;

	  public String getContentsForClipBoard() {
		return contentsForClipBoard;
	}
	public void setContentsForClipBoard(String contentsForClipBoard) {
		this.contentsForClipBoard = contentsForClipBoard;
	}
	private ModelProvider() {
		  splitPackageDetailsList = new ArrayList<SplitPackageDetails>();
	  }
	  public void clearSplitPackageDetails(){
		  splitPackageDetailsList.clear();
	  }
	  public void addSplitPackageDetails(String typeLibrary,String packageGen,String action ){
		  splitPackageDetailsList.add(new SplitPackageDetails(typeLibrary, packageGen, action));

	  }
	  public List<SplitPackageDetails> getSplitPackageDetailss() {
		  return splitPackageDetailsList;
	}

} 


