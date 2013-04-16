package org.ebayopensource.turmeric.eclipse.services.ui.wizards.pages;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.exception.validation.ValidationInterruptedException;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOARepositorySystem;
import org.ebayopensource.turmeric.eclipse.resources.model.RaptorArchetype;
import org.ebayopensource.turmeric.eclipse.services.buildsystem.WebProjectCreator;
import org.ebayopensource.turmeric.eclipse.services.ui.SOAMessages;
import org.ebayopensource.turmeric.eclipse.services.ui.wizards.ServiceFromWSDLWizard;
import org.ebayopensource.turmeric.eclipse.ui.SOABasePage;
import org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.ebayopensource.turmeric.eclipse.validator.core.ErrorMessage;
import org.ebayopensource.turmeric.eclipse.validator.core.ISOAValidator;
import org.ebayopensource.turmeric.eclipse.validator.core.InputObject;
import org.ebayopensource.turmeric.eclipse.validator.utils.common.NameValidator;
import org.ebayopensource.turmeric.eclipse.validator.utils.common.RegExConstants;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class WebProjectLinkingPage extends SOABasePage {
	private static String WITHOUT_CORE_EXPLN = "*Only listing soa web projects from your workspace which do not support core domain dependencies";
	private static String WITH_CORE_EXPLN = "*Only listing soa web projects from your workspace which support core domain dependencies";
	private Button reuseButton;
	private Composite container;
	private Text webProjectName;
	protected CCombo availableDUCombo;
	private ModifyListener availableDUListModifyListener;
	private SelectionListener createListener;
	private SelectionListener reuseListener;
	private SelectionListener coreDependeneciesListener;
	private Label availableDUexpln;
	private Boolean reuse = Boolean.FALSE;
	private String selectedProject;
	private boolean core;
	public boolean getPreferredCoreNature() {
		return core;
	}

	public void setPreferredCoreNature(boolean preferredNature) {
		this.core = preferredNature;
	}

	public String getSelectedProject() {
		return selectedProject;
	}

	public void setSelectedProject(String selectedProject) {
		this.selectedProject = selectedProject;
	}

	public Boolean getReuse() {
		return reuse;
	}

	public void setReuse(Boolean reuse) {
		this.reuse = reuse;
	}

	private Properties archetypeUIProperties;
	PropertiesGroup propertiesGroup;
	public Properties getArchetypeUIProperties() {
		return archetypeUIProperties;
	}
	@Override
	public boolean dialogChanged() {	
		
		//Handle reuse case with only validation on picking a web project form the worskpace
		if(getReuse()){
			if(availableDUCombo.getItemCount()==0){
				String errorMsg = "";
				if(getPreferredCoreNature()){
					errorMsg = "No Web Projects with core domain dependencies available in the workspace";
				}else{
					errorMsg = "No Web Projects without core domain dependencies available in the workspace";
				}
				updateStatus(availableDUCombo, errorMsg);
				return false;
			}
			if ((getSelectedProject()==null)||(getSelectedProject().trim().equals(""))){
				String errorMsg = "No Web Project Selected";
				updateStatus(availableDUCombo, errorMsg);
				return false;
			}
			return super.dialogChanged();
		}
		
		//1. WebProj not null
		if (((!getReuse())&&(getWebProjectName()==null))
		||((getReuse())&&
				((getSelectedProject()==null)||(getSelectedProject().trim().equals(""))))){				
			String errorMsg = "Web Project Name cannot be empty";
			updateStatus(webProjectName, errorMsg);
			return false;
		}	
		//Validate Web project valid windows folder name
		
		if(validateName(webProjectName,getWebProjectName(), 				
				RegExConstants.PROJECT_NAME_EXP,
				"Enter a valid Web Project name") == false){
			return false;
				}
		//Validate webproject is not dupliacted in the file system
		String parentFolderName="";
		parentFolderName=((ServiceFromWSDLWizard)this.getWizard()).getWorkspaceRootDirectory();
		if(parentFolderName==null) return false;
		IStatus validationModel = validateProject(null, parentFolderName, getWebProjectName());
		//Validtaing Web project
		if (checkValidationResult(validationModel, 
				webProjectName) == false)
			return false;
		IStatus validationModel2 = validateProject(null, parentFolderName, getWebProjectName()+".eba");
		//Validating eba project
		if (checkValidationResult(validationModel2, 
				webProjectName) == false)
			return false;
		//Validating web project's parent project
		
		IStatus validationModel3 = validateProject(null, parentFolderName, getWebProjectName()+"Parent");
		//Validating eba project
		if (checkValidationResult(validationModel3, 
				webProjectName) == false)
			return false;
		
		//Validate group id valid
		if(getArchetypeUIProperties().getProperty("groupId")==null){
			updateStatus("Enter a valid group id");
			return false;
		}
		if(checkValidationResult(validatePackageName( 
				getArchetypeUIProperties().getProperty("groupId")),propertiesGroup.getPropertiesViewer())==false){
			return false;
		}
		
		if(!propertiesGroup.appModified){
			getArchetypeUIProperties().setProperty("appName", getWebProjectName().toLowerCase());
			propertiesGroup.update(null, null);
		}
		//Set app Name
		String appName =getArchetypeUIProperties().getProperty("appName"); 
		if((appName==null)||(appName.equals(""))){
			String errorMsg = "App Name cannot be empty";
			updateStatus(errorMsg);
			return false;
		}
		
		
		//2.App Name more than 9 chars
		appName =getArchetypeUIProperties().getProperty("appName"); 
		if((appName!=null)&&(appName.length()>9)){
			String errorMsg = "App Name cannot exceed 9 characters";
			updateStatus(errorMsg);
			return false;
		}
		//appName should be valid
		if(validateName(propertiesGroup.getPropertiesViewer(),appName, 				
				RegExConstants.APP_NAME_EXP,
				"Enter a valid App id") == false){
			return false;
				}
		//Version should be present and valid
		if(getPreferredCoreNature()){
			//Should not be null
			String domainVersion =getArchetypeUIProperties().getProperty("domainParentVersion"); 
			if((domainVersion==null)||(domainVersion.equals(""))){
				String errorMsg = "Enter a Domain Parent Version";
				updateStatus(errorMsg);
				return false;
			}//Should be valid
			if(validateName(propertiesGroup.getPropertiesViewer(),domainVersion, 				
					RegExConstants.MAVEN_VERSION_EXP,
					"Invalid Domain Parent Version") == false){
				return false;
					}
		}
		return super.dialogChanged();
		
	}
	private static IStatus validatePackageName(String packageName) {
		IStatus status = JavaConventions.validatePackageName(packageName, JavaCore.VERSION_1_3, JavaCore.VERSION_1_3);

		if (status.getSeverity() == IStatus.ERROR) {
			return EclipseMessageUtils.createErrorStatus("Group id is not valid. "+status.getMessage());
		} else if (status.getSeverity() == IStatus.WARNING) {
			return EclipseMessageUtils.createErrorStatus("This group id is discouraged. "+status.getMessage());			
		}
		return status;
	}
	protected boolean validateName(Control control, 
			String name, String pattern, String errorMessage) {
		final InputObject inputObject = new InputObject(name,
				pattern, errorMessage);

		try {
			IStatus validationModel = NameValidator.getInstance().validate(
					inputObject);
			if (checkValidationResult(control, validationModel) == false)
				return false;
		} catch (ValidationInterruptedException e) {
			processException(e);
		}
		return true;
	}
	public void setArchetypeUIProperties(Properties archetypeUIProperties) {
		this.archetypeUIProperties = archetypeUIProperties;
	}
	protected IStatus validateProject(
			ISOARepositorySystem activeRepositorySystem,
			final String parentFolderName, String... projectNames) {
		IStatus validationModel = null;
		if (activeRepositorySystem == null)
			activeRepositorySystem = GlobalRepositorySystem.instanceOf()
					.getActiveRepositorySystem();
		for (String projectName : projectNames) {
			if (StringUtils.isBlank(projectName))
				continue;
			
			String projectFullPath = WorkspaceUtil.getProjectDirPath(
					parentFolderName, projectName);

			ISOAValidator validator = activeRepositorySystem
					.getProjectFileSystemValidator();
			try {
				validationModel = validator.validate(projectFullPath);
			} catch (ValidationInterruptedException e1) {
				processException(e1);
			}
			if (validationModel.getSeverity() == IStatus.ERROR) {
				return validationModel;
			}

			validator = activeRepositorySystem.getProjectWorkspaceValidator();
			try {
				validationModel = validator.validate(projectName);
			} catch (ValidationInterruptedException e) {
				processException(e);
			}
			if (validationModel.getSeverity() == IStatus.ERROR) {
				return validationModel;
			}

			validationModel = ResourcesPlugin.getWorkspace().validateName(
					projectName, IResource.PROJECT);
			if (validationModel.getSeverity() == IStatus.ERROR)
				return validationModel;
		}
		return Status.OK_STATUS;
	}
	/*
	 * web project archetype details;
	 */
	private String webProjectArchetypeGid;
	private String webProjectArchetypeArtId;
	private String webProjectArchetypeVsn;
	public String getWebProjectArchetypeGid() {
		return webProjectArchetypeGid;
	}

	public void setWebProjectArchetypeGid(String webProjectArchetypeGid) {
		this.webProjectArchetypeGid = webProjectArchetypeGid;
	}

	public String getWebProjectArchetypeArtId() {
		return webProjectArchetypeArtId;
	}

	public void setWebProjectArchetypeArtId(String webProjectArchetypeArtId) {
		this.webProjectArchetypeArtId = webProjectArchetypeArtId;
	}

	public String getWebProjectArchetypeVsn() {
		return webProjectArchetypeVsn;
	}

	public void setWebProjectArchetypeVsn(String webProjectArchetypeVsn) {
		this.webProjectArchetypeVsn = webProjectArchetypeVsn;
	}

	public String getWebProjectName() {
		return getTextValue(webProjectName);
	}

	public WebProjectLinkingPage(String pageName) {
		super(pageName);
	}

	public WebProjectLinkingPage(String pageName, String title,
			String description) {
		// super(pageName,title,description);
		super(pageName);
		setTitle( pageName);
		setDescription("Links the service to a new/existing web project that may/may not support core domain dependencies");

	}

	private Button createProtocalLine(Composite parent, String text,
			String value, boolean selected, boolean enabled) {
		Button button = new Button(parent, SWT.RADIO);

		button.setSelection(selected);
		button.setEnabled(enabled);
		Label label = new Label(parent, SWT.NONE);
		button.setEnabled(enabled);
		label.setText(text);
		button.setData(value);
		return button;
	}

	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 1;
		addCoreDomainOrNotGroup();
		selectSoaRaptorWeb();
		Group createOrReuseGroup = new Group(container, SWT.NONE);
		createOrReuseGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		createOrReuseGroup.setText("Web Project");
		createOrReuseGroup.setLayout(new GridLayout(3, false));
		createProtocalLine(createOrReuseGroup,
				"Create new web project with name", "yes", true, true);
		addWebProjectNameText(createOrReuseGroup);
		reuseButton = createProtocalLine(createOrReuseGroup,
				"Reuse existing web project from the workspace", "no",
				false, true);
		
		addReuseListener(reuseButton);
		addAvlDUsCombo(createOrReuseGroup);
		/*
		 * setting properties here for now
		 */
		
		setArchetypeUIProperties(getDefaultPropertiesForNonCore());
		addPropertiesTable();
		setControl(container);
		setPageComplete(false);
	}
	private Properties getDefaultPropertiesForNonCore(){
		Properties properties = new Properties();
		properties.put("appName","$");
		properties.put("groupId","com.ebay.app.raptor");
		properties.put("webProjectDescription","[description]");
		return properties;
	}
	private Properties getDefaultPropertiesForCore(){
		Properties properties = new Properties();
		properties.put("appName","$");
		properties.put("groupId","com.ebay.app.raptor");
		properties.put("webProjectDescription","[description]");
		properties.put("domainParentVersion","");
		return properties;
	}
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		propertiesGroup.update(null, null);
	}
private void addPropertiesTable(){
	propertiesGroup = new PropertiesGroup(this);
	propertiesGroup.createControl(this.container);
}
	private void addCoreDomainOrNotGroup() {
		Group coreDomainDepGroup = new Group(container, SWT.NONE);
		coreDomainDepGroup
				.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		coreDomainDepGroup
				.setText("Will your service contain core domain dependencies?:");

		coreDomainDepGroup.setLayout(new GridLayout(2, false));
		coreDependeneciesListener = new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Boolean sel = ((Button) (arg0.widget)).getSelection();
				if (sel) {
					availableDUexpln.setText(WITHOUT_CORE_EXPLN);
					selectedProject=null;
					//switch properties
					setPreferredCoreNature(false);
					String[] items = WebProjectCreator.getAllWebProjects(getPreferredCoreNature()).toArray(
							new String[0]);
					availableDUCombo.setItems(items);
					setArchetypeUIProperties(getDefaultPropertiesForNonCore());
					selectSoaRaptorWeb();
					propertiesGroup.update(null, null);
					dialogChanged();
				} else {
					availableDUexpln.setText(WITH_CORE_EXPLN);
					//switch properties
					selectedProject=null;
					setPreferredCoreNature(true);
					String[] items = WebProjectCreator.getAllWebProjects(getPreferredCoreNature()).toArray(
							new String[0]);
					availableDUCombo.setItems(items);
					setArchetypeUIProperties(getDefaultPropertiesForCore());
					selectSoaRaptorDomainWeb();
					propertiesGroup.update(null, null);
					dialogChanged();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {

			}
		};
		createProtocalLine(coreDomainDepGroup, "YES", "yes", false, true);
		Button noCoreDep = createProtocalLine(coreDomainDepGroup, "NO", "no",
				true, true);
		noCoreDep.addSelectionListener(coreDependeneciesListener);
	}

	private void addWebProjectNameText(Composite createOrReuseGroup) {
		webProjectName = new Text(createOrReuseGroup, SWT.BORDER | SWT.SINGLE);
		webProjectName.setText("");
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		webProjectName.setLayoutData(gd);
		webProjectName.addModifyListener(modifyListener);
	}

	private void addReuseListener(Button reuseButton) {
		reuseListener = new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				reuse = ((Button) (arg0.widget)).getSelection();
				setReuse(reuse);
				if (reuse) {
					disableCreateTextAndTableAndEnableDU();
					dialogChanged();
					selectedProject=null;
				} else {
					disableDuListAndEnableText();
					dialogChanged();
					selectedProject=null;
				}
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {

			}
		};
		reuseButton.addSelectionListener(reuseListener);
	}

	private void addAvlDUsCombo(Composite createOrReuseGroup) {
		availableDUCombo = new CCombo(createOrReuseGroup, SWT.BORDER
				| SWT.DROP_DOWN | SWT.READ_ONLY);
		availableDUCombo.setEnabled(false);
		String[] items = WebProjectCreator.getAllWebProjects(getPreferredCoreNature()).toArray(
				new String[0]);
		availableDUCombo.setItems(items);
		availableDUCombo.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				true, false, 1, 1));
		availableDUCombo.setBackground(UIUtil.display().getSystemColor(
				SWT.COLOR_LIST_BACKGROUND));
		UIUtil.decorateControl(this, availableDUCombo,
				"the deployable units available in your workspace");
		availableDUCombo.addSelectionListener(new SelectionAdapter() {
		      public void widgetSelected(SelectionEvent e) {
		        selectedProject = availableDUCombo.getText();	
		        dialogChanged();
		      }
		      });	
		availableDUexpln = new Label(createOrReuseGroup, SWT.LEFT);
		availableDUexpln.setText(WITHOUT_CORE_EXPLN);
		GridData gridData = new GridData();
		gridData.horizontalSpan = 3;
		availableDUexpln.setLayoutData(gridData);
	}

	private void disableCreateTextAndTableAndEnableDU() {
		this.webProjectName.setEnabled(false);
		this.availableDUCombo.setEnabled(true);
		this.propertiesGroup.getPropertiesViewer().setEnabled(false);
	}

	private void disableDuListAndEnableText() {
		this.availableDUCombo.setItems(new String[0]);
		this.availableDUCombo.setEnabled(false);
		this.webProjectName.setEnabled(true);
		this.propertiesGroup.getPropertiesViewer().setEnabled(true);
	}
	//Need to make sure validation happens for this not being null on finish
	public String getSelectedDU(){
		return selectedProject;
	}

	@Override
	public String getDefaultValue(Text text) {
		// TODO Auto-generated method stub
		return null;
	}

	public RaptorArchetype getSelectedArchetype() {
		RaptorArchetype archetype = new RaptorArchetype();
		archetype.setArtifactId(getWebProjectArchetypeArtId());
		archetype.setGroupId(getWebProjectArchetypeGid());
		archetype.setVersion(getWebProjectArchetypeVsn());
		return archetype;
	}
	private void selectSoaRaptorWeb(){
		setWebProjectArchetypeGid("com.ebay.raptor.tools.mvn.archetypes.soa");
		setWebProjectArchetypeArtId("raptor-soa-web");
		setWebProjectArchetypeVsn("1.0.0-RELEASE");
	}
	private void selectSoaRaptorDomainWeb(){
		setWebProjectArchetypeGid("com.ebay.raptor.tools.mvn.archetypes.soa");
		setWebProjectArchetypeArtId("raptor-soa-legacy-web");
		setWebProjectArchetypeVsn("1.0.0-RELEASE");
	}

}
