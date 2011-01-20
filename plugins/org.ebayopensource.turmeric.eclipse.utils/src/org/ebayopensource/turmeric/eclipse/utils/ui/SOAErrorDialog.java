/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.utils.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IconAndMessageDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

/**
 * 
 * code to show error message for soa plug-ins
 * 
 */
public class SOAErrorDialog extends IconAndMessageDialog {

    private static final int RESERVE_LIST_ITEM_COUNT = 9;

    private static final String NESTING_INDENT = "  ";

    private Button detailsButton;

    private String errorDialogTitle;

    private List messageList;

    private boolean isListCreated = false;

    private int filterMask = 0xFFFF;

    private IStatus status;

    private Clipboard clipboard;

    private boolean hasLineBreak = false;

    private boolean hasLineTitle = false;

    private boolean hasCancelButton = false;

    private boolean buttonLabelChange = false;
    

    /**
     * create an error dialog instance
     * 
     * @param parent
     * @param title
     * @param message
     * @param status
     * @param mask
     */
    public SOAErrorDialog(Shell parent, String title, String message,
            IStatus status, int mask) {
        this(parent, title, message, status, mask, false, false, false, false);

    }

    public SOAErrorDialog(Shell parent, String title, String message,
            IStatus status, int mask, boolean needLineBreak,
            boolean needLineTitle, boolean needCancelButton,
            boolean changeButtonLabel) {
        super(parent);
        errorDialogTitle = title;
        if (errorDialogTitle == null) {
            errorDialogTitle = JFaceResources.getString("Problem_Occurred");
        }

        this.message = message == null ? status.getMessage()
                : JFaceResources
                        .format(
                                "Reason", new Object[] { message, status.getMessage() }); //$NON-NLS-1$
        this.status = status;
        this.filterMask = mask;
        setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);// #added by wecai
        this.hasLineBreak = needLineBreak;
        this.hasLineTitle = needLineTitle;
        this.hasCancelButton = needCancelButton;
        this.buttonLabelChange = changeButtonLabel;
    }

    /**
     * Handle "Details" button action.show details if it is hidden. Hide details
     * if it is shown.
     */
    protected void buttonPressed(int id) {
        if (id == IDialogConstants.DETAILS_ID) {
            showDetialsArea();
        } else {
            super.buttonPressed(id);
        }
    }

    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText(errorDialogTitle);
    }

    protected void createButtonsForButtonBar(Composite parent) {
        // create OK and Details buttons
        String okLabel = IDialogConstants.OK_LABEL;
        String cancelLabel = IDialogConstants.CANCEL_LABEL;
        if (buttonLabelChange == true) {
            okLabel = "Ignore and Continue";
            cancelLabel = "Abort";
        }
        createButton(parent, IDialogConstants.OK_ID, okLabel, true);
        if (hasCancelButton == true) {
            createButton(parent, IDialogConstants.CANCEL_ID, cancelLabel, true);
        }
        if (shouldShowDetailsButton()) {
            detailsButton = createButton(parent, IDialogConstants.DETAILS_ID,
                    IDialogConstants.SHOW_DETAILS_LABEL, false);
        }
    }

    /**
     * create the main part of the dialog
     */
    protected Control createDialogArea(Composite parent) {
        createMessageArea(parent);
        // create content area
        Composite composite = new Composite(parent, SWT.NONE);
        GridData childData = new GridData(GridData.FILL_BOTH);
        childData.horizontalSpan = 2;
        composite.setLayoutData(childData);
        composite.setFont(parent.getFont());

        GridLayout layout = new GridLayout();
        layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
        layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
        layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
        layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
        layout.numColumns = 2;
        composite.setLayout(layout);

        return composite;
    }

    protected void createDialogAndButtonArea(Composite parent) {
        super.createDialogAndButtonArea(parent);
        if (this.dialogArea instanceof Composite) {
            Composite dialogComposite = (Composite) dialogArea;
            if (dialogComposite.getChildren().length == 0) {
                new Label(dialogComposite, SWT.NULL);
            }
        }
    }

    /**
     * get dialog icon.
     */
    protected Image getImage() {
        if (status != null) {
            if (status.getSeverity() == IStatus.WARNING
                    || hasCancelButton == true) {
                // if the cancel button is needed, then we should use warning
                // icon
                return getWarningImage();
            }
            if (status.getSeverity() == IStatus.INFO) {
                return getInfoImage();
            }
        }
        // If it was not a warning or an error then return the error image
        return getErrorImage();
    }

    /**
     * create the details list
     * 
     * @param parent
     * @return
     */
    protected List createDetailsList(Composite parent) {
        // create the list
        messageList = new List(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL
                | SWT.MULTI);
        // fill the list
        fillList(messageList, status, 0);
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL
                | GridData.GRAB_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL
                | GridData.GRAB_VERTICAL);
        data.heightHint = messageList.getItemHeight() * RESERVE_LIST_ITEM_COUNT;
        data.horizontalSpan = 2;
        messageList.setLayoutData(data);
        messageList.setFont(parent.getFont());
        Menu copyMenu = new Menu(messageList);
        MenuItem copyItem = new MenuItem(copyMenu, SWT.NONE);
        copyItem.addSelectionListener(new SelectionListener() {
            /*
             * @see SelectionListener.widgetSelected (SelectionEvent)
             */
            public void widgetSelected(SelectionEvent e) {
                copyDetailsToClipboard();
            }

            /*
             * @see SelectionListener.widgetDefaultSelected(SelectionEvent)
             */
            public void widgetDefaultSelected(SelectionEvent e) {
                copyDetailsToClipboard();
            }
        });
        copyItem.setText(JFaceResources.getString("copy")); //$NON-NLS-1$
        messageList.setMenu(copyMenu);
        isListCreated = true;
        return messageList;
    }

    /**
     * open error dialog.only show desired status based on status mask
     */
    public int open() {
        if (shouldShow(status, filterMask)) {
            return super.open();
        }
        setReturnCode(OK);
        return OK;
    }

    public static int openError(Shell parent, String dialogTitle,
            String message, IStatus status) {
        return openError(parent, dialogTitle, message, status, IStatus.OK
                | IStatus.INFO | IStatus.WARNING | IStatus.ERROR);
    }

    public static int openError(Shell parentShell, String title,
            String message, IStatus status, int displayMask) {
        return openError(parentShell, title, message, status, displayMask,
                false, false, false, false);
    }

    public static int openError(Shell parentShell, String title,
            String message, IStatus status, int displayMask,
            boolean needLineBreak, boolean needLineTitle,
            boolean needCancelButton, boolean changeButtonLabel) {
        SOAErrorDialog dialog = new SOAErrorDialog(parentShell, title, message,
                status, displayMask, needLineBreak, needLineTitle,
                needCancelButton, changeButtonLabel);
        return dialog.open();
    }

    private void fillList(List listToPopulate, IStatus status, int nesting) {
        if (!status.matches(filterMask)) {
            return;
        }
        java.util.List<String> msg = new ArrayList<String>();
        createDetailContent(status, msg, 0);
        for (String str : msg) {
            listToPopulate.add(str);
        }
    }

    protected static boolean shouldShow(IStatus status, int mask) {
        IStatus[] children = status.getChildren();
        if (children == null || children.length == 0) {
            return status.matches(mask);
        }
        for (int i = 0; i < children.length; i++) {
            if (children[i].matches(mask)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Toggles the unfolding of the details area. This is triggered by the user
     * pressing the details button.
     */
    private void showDetialsArea() {
        Point windowSize = getShell().getSize();
        Point oldSize = getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT);
        // if list showed, hide list. Otherwise, show list.
        if (isListCreated) {
            messageList.dispose();
            isListCreated = false;
            detailsButton.setText(IDialogConstants.SHOW_DETAILS_LABEL);
            // #added by wecai
            ((GridData) this.dialogArea.getLayoutData()).grabExcessVerticalSpace = true;
        } else {
            messageList = createDetailsList((Composite) getContents());
            detailsButton.setText(IDialogConstants.HIDE_DETAILS_LABEL);
            // #added by wecai
            ((GridData) this.dialogArea.getLayoutData()).grabExcessVerticalSpace = false;
        }
        // resize dialog.
        Point newSize = getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT);
        getShell()
                .setSize(
                        new Point(windowSize.x, windowSize.y
                                + (newSize.y - oldSize.y)));
    }

    /**
     * generate content to show.
     * 
     * @param buildingStatus
     * @param msg
     * @param nesting
     */
    private void createDetailContent(IStatus buildingStatus,
            java.util.List<String> msg, int nesting) {
    	StringBuffer buffer = new StringBuffer();
        // not to be showed, just return.
        if (!buildingStatus.matches(filterMask)) {
            return;
        }
        if (hasLineBreak == true && buffer.length() > 0) {
            msg.add("\r\n");
        }

        for (int i = 0; i < nesting; i++) {
            buffer.append(NESTING_INDENT);
        }

        Class<?> statusClazz = buildingStatus.getClass();
        
        if (hasLineTitle == true && buildingStatus.isOK() == false) {
            if (buildingStatus.getSeverity() == IStatus.WARNING
                    && (buildingStatus instanceof MultiStatus) == false
                    && statusClazz.equals(Status.class) == false
                    && buildingStatus.getCode() != IStatus.OK) {
                // this is the case that we are dealing with customized status
                buffer.append(buildingStatus.toString());
            } else {
                buffer
                        .append(buildingStatus.getSeverity() == IStatus.ERROR ? "Must Fix Error: "
                                : "Should Fix Warning: ");
                buffer.append(buildingStatus.getMessage());
            }

        } else {
        	buffer.append(buildingStatus.getMessage());
        }
        msg.add(buffer.toString());
        buffer.delete(0, buffer.length());

        // Look for a nested core exception
        Throwable t = buildingStatus.getException();
        if (t instanceof CoreException) {
            CoreException ce = (CoreException) t;
            createDetailContent(ce.getStatus(), msg, nesting + 1);
        } else if (t != null) {
            // Include low-level exception message
            for (int i = 0; i < nesting; i++) {
                buffer.append(NESTING_INDENT);
            }
            String message = t.getLocalizedMessage();
            if (message == null) {
                message = t.toString();
            }
            buffer.append(message);
            msg.add(buffer.toString());
            buffer.delete(0, buffer.length());
        }

        IStatus[] children = buildingStatus.getChildren();
        Arrays.sort(children, StatusComparator.INSTANCE);
        for (int i = 0; i < children.length; i++) {
            createDetailContent(children[i], msg, nesting + 1);
        }
    }

    /**
     * Copy the contents of the statuses to the clipboard.
     */
    private void copyDetailsToClipboard() {
        if (clipboard != null) {
            clipboard.dispose();
        }
        StringBuffer statusBuffer = new StringBuffer();
        java.util.List<String> msg = new ArrayList<String>();
        createDetailContent(status, msg, 0);
        for (String str : msg) {
            statusBuffer.append(str + "\r\n");
        }
        clipboard = new Clipboard(messageList.getDisplay());
        clipboard.setContents(new Object[] { statusBuffer.toString() },
                new Transfer[] { TextTransfer.getInstance() });
    }

    public boolean close() {
        if (clipboard != null) {
            clipboard.dispose();
        }
        return super.close();
    }

    protected boolean shouldShowDetailsButton() {
        return status.isMultiStatus() || status.getException() != null;
    }
    
    private static class StatusComparator implements Comparator<IStatus>{
    	
    	public static StatusComparator INSTANCE = new StatusComparator();

    	public int compare(IStatus status1, IStatus status2) {
    		return status2.getSeverity() - status1.getSeverity();
    	}
    	
    }

}
