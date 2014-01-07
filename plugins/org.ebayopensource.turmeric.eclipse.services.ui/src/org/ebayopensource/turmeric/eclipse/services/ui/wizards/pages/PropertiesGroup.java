package org.ebayopensource.turmeric.eclipse.services.ui.wizards.pages;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import java.util.Set;

import org.ebayopensource.turmeric.eclipse.resources.model.ArchetypeProperty;
import org.ebayopensource.turmeric.eclipse.resources.model.RaptorArchetype;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.FocusCellOwnerDrawHighlighter;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.jface.viewers.TableViewerFocusCellManager;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;


public class PropertiesGroup extends Observable implements Observer{
	private WebProjectLinkingPage container;
	private Table propertiesTable;
	private TableViewer propertiesViewer;
	public boolean appModified;
	public Table getPropertiesViewer() {
		return propertiesTable;
	}
	

	protected Set<String> requiredProperties; //it's only for properties table show.
	protected String[] RaptorPlatformVersionList; //it's only for properties table show.

	private Properties userInputProperties;

	final public static String KEY_PROPERTY = "key"; //$NON-NLS-1$
	final public static int KEY_INDEX = 0;
	final public static String VALUE_PROPERTY = "value"; //$NON-NLS-1$
	final public static int VALUE_INDEX = 1;
	final public static int DESC_INDEX = 2;

	public PropertiesGroup(WebProjectLinkingPage container){
		this.container = container;
		requiredProperties = new HashSet<String>();
	}

	public Control createControl(Composite composite) {
		Group fGroup= new Group(composite, SWT.NONE);
		fGroup.setFont(composite.getFont());
		GridLayout gt = new GridLayout(4, false);
	//	gt.horizontalSpacing
		fGroup.setLayout(gt);
		fGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		fGroup.setText("GroupTitle");

	    propertiesViewer = new TableViewer(fGroup, SWT.BORDER | SWT.FULL_SELECTION);
	    propertiesTable = propertiesViewer.getTable();
	    propertiesTable.setLinesVisible(true);
	    propertiesTable.setHeaderVisible(true);

	    GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 2);
	    gd_table.heightHint = 100;
	    propertiesTable.setLayoutData(gd_table);
	    propertiesTable.setTopIndex(0);

	    TableColumn propertiesTableNameColumn = new TableColumn(propertiesTable, SWT.NONE);
	   propertiesTableNameColumn.setWidth(130);
	    propertiesTableNameColumn.setText("Key");

	    TableColumn propertiesTableValueColumn = new TableColumn(propertiesTable, SWT.NONE);
	    propertiesTableValueColumn.setWidth(130);
	    propertiesTableValueColumn.setText("Value");

	    TableColumn propertiesTableDescColumn = new TableColumn(propertiesTable, SWT.NONE);
	    propertiesTableDescColumn.setWidth(260);
	    propertiesTableDescColumn.setText("Description");

	    propertiesViewer.setColumnProperties(new String[] {"key", "value"}); //$NON-NLS-1$ //$NON-NLS-2$

	    final CellEditor[] cellEditors= new CellEditor[] {new TextCellEditor(propertiesTable, SWT.NONE),
		        new TextCellEditor(propertiesTable, SWT.NONE), new TextCellEditor(propertiesTable, SWT.NONE)};

	    propertiesViewer.setCellEditors(cellEditors);
	    propertiesViewer.setCellModifier(new ICellModifier() {
	      public boolean canModify(Object element, String property) {
	    	  if (!"value".equals(property)){
	    		  return false;
	    	  }

	    	  if (((TableItem)element).getText().equals("RaptorPlatformVersion")){
	    		  updatePropertyEditors((TableItem)element);
	    	  }else{
	    		  cellEditors[VALUE_INDEX].dispose();
	    		  cellEditors[VALUE_INDEX] = new TextComboBoxCellEditor(propertiesTable,
							SWT.FLAT);
	    	  }

	    	  return true;
	      }

	      public void modify(Object element, String property, Object value) {
	        if(element instanceof TableItem) {
	        	if(((TableItem) element).getText().equals("appName"))
		          {
		        	  appModified=true;
		          }
	          ((TableItem) element).setText(getTextIndex(property), String.valueOf(value));
	          //container.set(container.getSelectedArchetype());
	          
	          container.setArchetypeUIProperties(getProperties());

	          userInputProperties = getProperties();
	          
	          //fire event to validate properties
	          container.dialogChanged();
	        }
	      }

		public Object getValue(Object element, String property) {
	        if(element instanceof TableItem) {
	          return ((TableItem) element).getText(getTextIndex(property));
	        }
	        return null;
	      }
	    });

	    //let table support key navigation
	    TableViewerFocusCellManager focusCellManager = new TableViewerFocusCellManager(propertiesViewer, new FocusCellOwnerDrawHighlighter(propertiesViewer));
		ColumnViewerEditorActivationStrategy actSupport = new ColumnViewerEditorActivationStrategy(propertiesViewer) {
			protected boolean isEditorActivationEvent(
					ColumnViewerEditorActivationEvent event) {
				return event.eventType == ColumnViewerEditorActivationEvent.TRAVERSAL
						|| event.eventType == ColumnViewerEditorActivationEvent.MOUSE_DOUBLE_CLICK_SELECTION
						|| (event.eventType == ColumnViewerEditorActivationEvent.KEY_PRESSED && event.keyCode == SWT.CR)
						|| event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC;
			}
		};

		TableViewerEditor.create(propertiesViewer, focusCellManager, actSupport, 
				ColumnViewerEditor.KEYBOARD_ACTIVATION);

		return fGroup;
	}
    
	protected void fireEvent() {
		setChanged();
		notifyObservers();
	}

	public Properties getProperties() {
		if (propertiesViewer.isCellEditorActive()) {
			propertiesTable.setFocus();
		}
		Properties properties = new Properties();
		for (int i = 0; i < propertiesTable.getItemCount(); i++) {
			TableItem item = propertiesTable.getItem(i);
			properties.put(item.getText(KEY_INDEX), item.getText(VALUE_INDEX));
		}
		return properties;
	}

	public int getTextIndex(String property) {
		return KEY_PROPERTY.equals(property) ? KEY_INDEX : VALUE_INDEX;
	}

	@Override
	public void update(Observable observable, Object data) {

//		if (!(observable instanceof NameGroup)){
//			userInputProperties = null;
//		}

		RaptorArchetype selectedArchetype = this.container.getSelectedArchetype();
		if (selectedArchetype==null){
			return;
		}

		if (propertiesViewer==null){
			return;
		}

		propertiesTable.removeAll();
		requiredProperties.clear();
	Properties properties = this.container.getArchetypeUIProperties();
		if (properties != null) {
			
			for (Object keyObject : properties.keySet()) {
				String key = (String)keyObject;
				String value = null;
				if(properties.get(key)!=null)
				 value = (String) properties.get(key);
				

				if ("artifactId".equals(key)){
					continue;
				}

				//by default, is visible.
				//if (property.isVisible()){
					if (isRaptorPlatformVersion(key)){

						List<String> list = null;
								//property.getVersionList();

						if (list==null || list.size()==0){
							list = new ArrayList<String>();

							list.add(value==null?"":value);
						}
						RaptorPlatformVersionList = list.toArray(new String[list.size()]);

						if (RaptorPlatformVersionList!=null && RaptorPlatformVersionList.length>0){
							value = RaptorPlatformVersionList[0];
						}
					}

					if (userInputProperties!=null 
							&& !appModified && !key.equals("appName"))
							{
						value = userInputProperties.getProperty(key);
					}
					//if there is placeholder for property, don't show the placeholder.
					if ((value!=null)&&(value.startsWith("$"))){
						String name = container.getWebProjectName();
						value =  getShowableWebProjectName(name);
					}

					addTableItem(key, value, getDescription(key));
					requiredProperties.add(key);
				//}
			}
		}

       // container.getProjectModel().setArchetype(selectedArchetype);
        container.setArchetypeUIProperties(getProperties());
        fireEvent();
	}

	private String getDescription(String key) {
		// TODO Auto-generated method stub
		if(key.equalsIgnoreCase("webProjectDescription")) return "This field is mandatory for registering your application and deploying to QA/ Production";
		if(key.equalsIgnoreCase("appName")) return "This field will be used as consumer name when registering your application and if not specified, will default to lowercase ArtifactId";
		if(key.equalsIgnoreCase("RaptorPlatformVersion")) return "This field will be used as the raptor parent version. Ensure it uses the same raptor parent as the soa projects (From raptorSoa.properties in <users home directory>\\<RIDE version>)";
	return "";
		
	}

	private boolean isRaptorPlatformVersion(String key){

		if (!"RaptorPlatformVersion".equals(key)){
			return false;
		}

		return true;
	}

	private String getShowableWebProjectName(String name) {
		if ("".equals(name)){
			return "";
		}
		return name==null?"":name.toLowerCase();
	}

	TableItem addTableItem(String key, String value, String description) {
		TableItem item = new TableItem(propertiesTable, SWT.NONE);
		item.setData(item);
		item.setText(KEY_INDEX, key);
		item.setText(VALUE_INDEX, value == null ? "" : value); //$NON-NLS-1$
		item.setText(DESC_INDEX, description);

		return item;
	}

	public void updatePropertyEditors(TableItem item) {
		if (propertiesViewer==null){
			return;
		}

		CellEditor[] ce = propertiesViewer.getCellEditors();

		if (requiredProperties.size() == 0) {
			if (ce[VALUE_INDEX] instanceof TextComboBoxCellEditor) {
				ce[VALUE_INDEX].dispose();
				ce[VALUE_INDEX] = new TextCellEditor(propertiesTable, SWT.FLAT);
			}
		} else {
			TextComboBoxCellEditor comboEditor = null;
			if (ce[VALUE_INDEX] instanceof TextComboBoxCellEditor) {
				comboEditor = (TextComboBoxCellEditor) ce[VALUE_INDEX];
			} else {
				ce[VALUE_INDEX].dispose();
				comboEditor = new TextComboBoxCellEditor(propertiesTable,
						SWT.FLAT);
				ce[VALUE_INDEX] = comboEditor;
			}

			if (RaptorPlatformVersionList==null || RaptorPlatformVersionList.length==0){

				return;
			}

			comboEditor.setItems(RaptorPlatformVersionList);
			item.setText(VALUE_INDEX, RaptorPlatformVersionList[0]);
		}
	}
}