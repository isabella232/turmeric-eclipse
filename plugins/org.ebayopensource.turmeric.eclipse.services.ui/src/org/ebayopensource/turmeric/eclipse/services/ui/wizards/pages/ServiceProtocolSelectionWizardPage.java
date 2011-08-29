package org.ebayopensource.turmeric.eclipse.services.ui.wizards.pages;

import java.util.ArrayList;
import java.util.List;

import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.ui.wizards.pages.AbstractNewServiceFromWSDLWizardPage;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

// TODO: Auto-generated Javadoc
/**
 * The Class ServiceProtocolSelectionWizardPage.
 */
public class ServiceProtocolSelectionWizardPage extends WizardPage implements
		IWizardPage {

	/** The non xsd protocols. */
	private List<Button> nonXSDProtocols = new ArrayList<Button>();

	/** The proto buf. */
	private Button protoBuf;

	/** The wizard page. */
	private AbstractNewServiceFromWSDLWizardPage wizardPage;

	/**
	 * Instantiates a new service protocol selection wizard page.
	 */
	public ServiceProtocolSelectionWizardPage() {
		super("newSOAServiceProjectServiceProtocolWizardPage");
		this.setTitle("Service Protocol Selection Page");
		this.setDescription("Please select the protocols to be used by this service.");
	}

	/**
	 * Gets the non xsd protocol string.
	 *
	 * @return the non xsd protocol string
	 */
	public String getNonXSDProtocolString() {
		boolean typeFolding = wizardPage.getTypeFolding();
		if (typeFolding == false) {
			return "";
		}
		StringBuilder procos = new StringBuilder();
		for (Button button : nonXSDProtocols) {
			if (button.getSelection() == true) {
				procos.append(button.getData());
				procos.append(",");
			}
		}
		if (procos.length() > 0) {
			procos.delete(procos.length() - 1, procos.length());
		}
		return procos.toString();
	}

	/**
	 * Sets the wizard page.
	 *
	 * @param wizardPage the new wizard page
	 */
	public void setWizardPage(AbstractNewServiceFromWSDLWizardPage wizardPage) {
		this.wizardPage = wizardPage;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.DialogPage#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		boolean typeFolding = wizardPage.getTypeFolding();
		protoBuf.setSelection(typeFolding);
		protoBuf.setEnabled(typeFolding);
		super.setVisible(visible);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, true));

		Group protocolGroup = new Group(container, SWT.NONE);
		protocolGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		protocolGroup.setText("Protocol:");

		protocolGroup.setLayout(new GridLayout(2, false));

		protoBuf = createProtocalLine(protocolGroup,
				"Protocol Buffers (Namespace Folding needs to be enabled for Protobuf)",
				SOAProjectConstants.SVC_PROTOCOL_BUF, true, true);
		nonXSDProtocols.add(protoBuf);
		createProtocalLine(protocolGroup, "XML", "xml", true, false);
		createProtocalLine(protocolGroup, "JSON", "json", true, false);
		createProtocalLine(protocolGroup, "NV", "nv", true, false);
		createProtocalLine(protocolGroup, "FAST_INFOSET", "fast_infoset", true,
				false);

		setControl(container);
	}

	/**
	 * Creates the protocal line.
	 *
	 * @param parent the parent
	 * @param text the text
	 * @param value the value
	 * @param selected the selected
	 * @param enabled the enabled
	 * @return the button
	 */
	private Button createProtocalLine(Composite parent, String text,
			String value, boolean selected, boolean enabled) {
		Button button = new Button(parent, SWT.CHECK);

		button.setSelection(selected);
		button.setEnabled(enabled);
		Label label = new Label(parent, SWT.NONE);
		button.setEnabled(enabled);
		label.setText(text);
		button.setData(value);
		return button;
	}

}
