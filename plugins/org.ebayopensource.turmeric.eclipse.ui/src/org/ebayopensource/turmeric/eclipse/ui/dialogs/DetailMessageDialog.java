package org.ebayopensource.turmeric.eclipse.ui.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * The Class DetailMessageDialog.
 */
public class DetailMessageDialog extends TrayDialog {

	private String title;

	private String message;

	/**
	 * Instantiates a new detail message dialog.
	 *
	 * @param shell the shell
	 * @param title the title
	 * @param message the message
	 * @param block the block
	 */
	public DetailMessageDialog(Shell shell, String title, String message,
			boolean block) {
		super(shell);
		this.title = title;
		this.message = message;
		this.setBlockOnOpen(block);
		this.setHelpAvailable(false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Point getInitialSize() {
		Point p = super.getInitialSize();
		p.x = 500;
		p.y = 300;
		return p;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, true);
		composite.setLayout(layout);

		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		composite.setLayoutData(gd);

		gd = new GridData(GridData.FILL_BOTH);
		gd.grabExcessHorizontalSpace = false;
		gd.grabExcessVerticalSpace = false;
		Label label = new Label(composite, SWT.NONE);
		label.setText(title);
		label.setLayoutData(gd);

		gd = new GridData(GridData.FILL_BOTH);
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;

		Text detail = new Text(composite, SWT.MULTI | SWT.BORDER | SWT.WRAP
				| SWT.V_SCROLL);

		detail.setEditable(false);

		detail.setLayoutData(gd);
		detail.setText(message);

		return parent;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.TrayDialog#createButtonBar(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createButtonBar(Composite parent) {
		Control control = super.createButtonBar(parent);
		Button okBtn = this.getButton(OK);
		okBtn.setVisible(false);
		Button cancelBtn = this.getButton(CANCEL);
		cancelBtn.setText(IDialogConstants.OK_LABEL);
		return control;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean isResizable() {
		return true;
	}

}
