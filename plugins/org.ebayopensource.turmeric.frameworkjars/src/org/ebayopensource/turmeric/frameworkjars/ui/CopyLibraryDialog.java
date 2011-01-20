/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.frameworkjars.ui;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.ArtifactScopeEnum;
import org.apache.maven.repository.metadata.ArtifactMetadata;
import org.ebayopensource.turmeric.eclipse.mavenapi.MavenApiPlugin;
import org.ebayopensource.turmeric.eclipse.mavenapi.impl.EclipseArtifactMetadata;
import org.ebayopensource.turmeric.eclipse.mavenapi.intf.IMavenEclipseApi;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;
import org.maven.ide.eclipse.embedder.ArtifactKey;
import org.maven.ide.eclipse.index.IIndex;
import org.maven.ide.eclipse.index.IndexedArtifactFile;
import org.maven.ide.eclipse.ui.dialogs.MavenRepositorySearchDialog;


/**
 * @author yayu
 *
 */
public class CopyLibraryDialog extends TitleAreaDialog {
	private Text libraryText;
	private Text destinationText;
	private ArtifactMetadata metadata = null;

	/**
	 * @param shell
	 */
	public CopyLibraryDialog(Shell shell) {
		super(shell);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.TitleAreaDialog#createContents(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createContents(Composite parent) {		
		return super.createContents(parent);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.TitleAreaDialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		
		Composite control = (Composite)super.createDialogArea(parent);
		setTitle("Copy Maven Library");
		setMessage("copy selected library and its dependencies to a selected directory.");
		addLibraryChooser(control);
		addDestinationChooser(control);
		Dialog.applyDialogFont(control);
		return control;
	}
	
	protected Composite addLibraryChooser(Composite parentComposite) {
		parentComposite = new Composite(parentComposite, SWT.NONE);
        GridLayout layout = new GridLayout(4, false);
        layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
        layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
        layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
        layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
        parentComposite.setLayout(layout);
        parentComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        parentComposite.setFont(parentComposite.getFont());
        
		new Label(parentComposite, SWT.LEFT).setText("&Maven Library:");
		libraryText = new Text(parentComposite, SWT.BORDER);
		libraryText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		libraryText.setEditable(false);
		
		Button browseBtn = new Button(parentComposite, SWT.PUSH);
		browseBtn.setAlignment(SWT.RIGHT);
		browseBtn.setText("&Search...");
		
		final SelectionListener workspaceBrowseListener = new SelectionListener() {
			public void widgetDefaultSelected(final SelectionEvent e) {
				widgetSelected(e);
			}

			public void widgetSelected(final SelectionEvent e) {
				final MavenRepositorySearchDialog dialog = new MavenRepositorySearchDialog( 
						getShell(), "Select Library to copy:", 
		        		IIndex.SEARCH_ARTIFACT, null);
		        if( dialog.open() == Window.OK ) {
		        	final Object obj = dialog.getFirstResult();
		        	if (obj instanceof IndexedArtifactFile) {
		        		final IndexedArtifactFile artifactFile = ( IndexedArtifactFile )obj;
		        		final ArtifactKey artifactKey = artifactFile.getArtifactKey();
		        		
		        		metadata = new EclipseArtifactMetadata( artifactKey.getGroupId(), artifactKey.getArtifactId(), artifactKey.getVersion(), 
		                		"jar", ArtifactScopeEnum.DEFAULT_SCOPE );
		        		libraryText.setText(metadata.toString());
		        	}
		        }
		        dialogChanged();
			}
		};
		browseBtn.addSelectionListener(workspaceBrowseListener);

		return parentComposite;
	}
	
	protected Composite addDestinationChooser(Composite parentComposite) {
		parentComposite = new Composite(parentComposite, SWT.NONE);
        GridLayout layout = new GridLayout(4, false);
        layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
        layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
        layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
        layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
        parentComposite.setLayout(layout);
        parentComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        parentComposite.setFont(parentComposite.getFont());
        
		new Label(parentComposite, SWT.LEFT).setText("&Destination:");
		destinationText = new Text(parentComposite, SWT.BORDER);
		destinationText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		//libraryText.addModifyListener(modifyListener);
		destinationText.setEditable(false);
		
		Button browseBtn = new Button(parentComposite, SWT.PUSH);
		browseBtn.setAlignment(SWT.RIGHT);
		browseBtn.setText("&Browse...");
		final String filterPath = null;
		final SelectionListener workspaceBrowseListener = new SelectionListener() {
			public void widgetDefaultSelected(final SelectionEvent e) {
				widgetSelected(e);
			}

			public void widgetSelected(final SelectionEvent e) {
				final DirectoryDialog dialog = new DirectoryDialog(getShell(),
						SWT.PUSH);
				dialog.setText("Select Destination");
				if (StringUtils.isNotBlank(filterPath))
					dialog.setFilterPath(filterPath);
				final String dirName = dialog.open();
				if (StringUtils.isNotBlank(dirName)) {
					destinationText.setText(dirName);
				}
				dialogChanged();
			}
		};
		browseBtn.addSelectionListener(workspaceBrowseListener);

		return parentComposite;
	}
	
	private void dialogChanged() {
		updatePageState(null);
		if (metadata == null) {
			updatePageState("library is not valid");
			return;
		}
		if (this.destinationText != null) {
			String dest =  this.destinationText.getText();
			if (StringUtils.isBlank(dest)) {
				updatePageState("Destination must not be empty.");
				return;
			}
			File destDir = new File(dest);
			if (destDir.exists() == false) {
				updatePageState("Destination does not exist.");
				return;
			}
		}
	}
	
	private void updatePageState(String message) {
		setMessage(message);
		setErrorMessage(message);
		getButton(IDialogConstants.OK_ID).setEnabled(message == null);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		if (metadata == null)
			return;
		final String destination = this.destinationText.getText();
		final IRunnableWithProgress runnable = new IRunnableWithProgress() {

			public void run(IProgressMonitor monitor)
					throws InvocationTargetException,
					InterruptedException {
				monitor.beginTask(
						"Copying jars for library ->" + metadata + "...", 
						IProgressMonitor.UNKNOWN);
				try {
					monitor.internalWorked(20);
					IMavenEclipseApi api = MavenApiPlugin.getDefault().getMavenEclipseApi();
					
					File destDir = new File(destination);
					if (destDir.exists() == false)
						destDir.mkdir();
					else {
						try {
							FileUtils.cleanDirectory(destDir);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					monitor.internalWorked(20);
					try {
						for (Artifact artifact : api.resolveArtifactAsClasspath(metadata)) {
							File artifactFile = artifact.getFile();
							if (artifactFile.getName().endsWith(".pom")) {
								System.out.println("bad");
							}
							monitor.setTaskName("Copying from " + artifactFile + " to " + destDir);
							FileUtils.copyFileToDirectory(artifact.getFile(), destDir);
							monitor.internalWorked(20);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (Exception e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		
		final IProgressService service = PlatformUI
		.getWorkbench().getProgressService();
		try {
			service.run(false, false, runnable);
		} catch (Exception e) {
			ErrorDialog.openError(getShell(), "Error Occurred", "error occurred while copying library->" + metadata, 
					new Status(IStatus.ERROR, "org.ebayopensource.turmeric", e.getLocalizedMessage(), e));
			e.printStackTrace();
		}
		
		super.okPressed();
	}
	
}
