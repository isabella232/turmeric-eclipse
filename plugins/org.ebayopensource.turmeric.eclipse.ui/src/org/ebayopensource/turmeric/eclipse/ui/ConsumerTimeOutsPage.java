package org.ebayopensource.turmeric.eclipse.ui;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class ConsumerTimeOutsPage extends WizardPage {
	private Text text1;
	  private Composite container;
	public ConsumerTimeOutsPage(String pageName) {
		super(pageName);
	}
	public ConsumerTimeOutsPage(String pageName, String title,
			String description) {
		//super(pageName,title,description);
		super(pageName);
		setTitle("eh" +pageName);
		setDescription("uhoh");
		
	}

	@Override
	public void createControl(Composite parent) {
		// TODO Auto-generated method stub
//		 
		container = new Composite(parent, SWT.NULL);
	    GridLayout layout = new GridLayout();
	    container.setLayout(layout);
	    layout.numColumns = 2;
	    Label label1 = new Label(container, SWT.NULL);
	    label1.setText("Say hello to Fred");

	    text1 = new Text(container, SWT.BORDER | SWT.SINGLE);
	    text1.setText("");
	    text1.addKeyListener(new KeyListener() {

	      @Override
	      public void keyPressed(KeyEvent e) {
	        // TODO Auto-generated method stub

	      }

	      @Override
	      public void keyReleased(KeyEvent e) {
	        if (!text1.getText().isEmpty()) {
	          setPageComplete(true);
	        }
	      }

	    });
	    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
	    text1.setLayoutData(gd);
	    Label labelCheck = new Label(container, SWT.NONE);
	    labelCheck.setText("This is a check");
	    Button check = new Button(container, SWT.CHECK);
	    check.setSelection(true);
	    // Required to avoid an error in the system
	    setControl(container);
	    setPageComplete(false);
	}

	//@Override
//	public String getDefaultValue(Text text) {
//		if (text == text1) {
//			return "woohooo";
//		}
//		return "ohooo";
//	}

}
