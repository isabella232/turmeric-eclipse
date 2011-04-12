package org.ebayopensource.turmeric.eclipse.ui.test.extensions;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * A Mock service page contributed through the project wizard page provider.
 * 
 * @author dcarver
 *
 */
public class MockServicePage implements IWizardPage {

	public MockServicePage() {
	}

	@Override
	public void createControl(Composite parent) {

	}

	@Override
	public void dispose() {

	}

	@Override
	public Control getControl() {
		return null;
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public String getErrorMessage() {
		return null;
	}

	@Override
	public Image getImage() {
		return null;
	}

	@Override
	public String getMessage() {
		return null;
	}

	@Override
	public String getTitle() {
		return null;
	}

	@Override
	public void performHelp() {

	}

	@Override
	public void setDescription(String description) {

	}

	@Override
	public void setImageDescriptor(ImageDescriptor image) {

	}

	@Override
	public void setTitle(String title) {

	}

	@Override
	public void setVisible(boolean visible) {

	}

	@Override
	public boolean canFlipToNextPage() {
		return false;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public IWizardPage getNextPage() {
		return null;
	}

	@Override
	public IWizardPage getPreviousPage() {
		return null;
	}

	@Override
	public IWizard getWizard() {
		return null;
	}

	@Override
	public boolean isPageComplete() {
		return false;
	}

	@Override
	public void setPreviousPage(IWizardPage page) {

	}

	@Override
	public void setWizard(IWizard newWizard) {

	}

}
