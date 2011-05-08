/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.utils.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.ebayopensource.turmeric.eclipse.core.IRunnable;
import org.ebayopensource.turmeric.eclipse.exception.core.SOAPartNotFoundException;
import org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.help.IWorkbenchHelpSystem;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.progress.IProgressService;
import org.eclipse.ui.progress.UIJob;


/**
 * Standard Utility class for UI operations. Most of these will be used form
 * wizards, editors and views.
 * 
 * @author smathew
 * 
 */
public class UIUtil {

	/**
	 * Progress service.
	 *
	 * @return the i progress service
	 */
	public static IProgressService progressService() {
		return getWorkbench().getProgressService();
	}
 
	/**
	 * Gets the workbench.
	 *
	 * @return the workbench
	 */
	public static IWorkbench getWorkbench() {
		return PlatformUI.getWorkbench();
	}

	/**
	 * Gets the help system.
	 *
	 * @return the help system
	 */
	public static IWorkbenchHelpSystem getHelpSystem() {
		return getWorkbench().getHelpSystem();
	}

	/**
	 * Display.
	 *
	 * @return the display
	 */
	public static Display display() {
		if (Display.getCurrent() != null)
			return Display.getCurrent();
		return Display.getDefault();
	}

	/**
	 * File dialog.
	 *
	 * @param title the title
	 * @param filterExtensions the filter extensions
	 * @return the string
	 */
	public static String fileDialog(final String title,
			final String... filterExtensions) {
		final FileDialog dialog = new FileDialog(getActiveShell(), SWT.OPEN);
		dialog.setText(title);
		dialog.setFilterExtensions(filterExtensions);
		return dialog.open();
	}

	/**
	 * File dialog.
	 *
	 * @param title the title
	 * @param sourceFile the source file
	 * @param filterExtensions the filter extensions
	 * @return the string
	 */
	public static String fileDialog(final String title, File sourceFile,
			final String... filterExtensions) {
		boolean isFile = sourceFile.isFile();
		boolean exist = sourceFile.exists();
		String fileName = "";
		if(isFile == true && exist == true){
			fileName = sourceFile.getName();
		}
		return fileDialog(title, fileName, sourceFile, filterExtensions);
	}
	
	/**
	 * File dialog.
	 *
	 * @param title the title
	 * @param filterName the filter name
	 * @param sourceFile the source file
	 * @param filterExtensions the filter extensions
	 * @return the string
	 */
	public static String fileDialog(final String title, String filterName,
			File sourceFile, final String... filterExtensions) {
		final FileDialog dialog = new FileDialog(getActiveShell(), SWT.OPEN);
		boolean isFile = sourceFile.isFile();
		boolean exist = sourceFile.exists();
		String filePath = "";
		if (isFile == true && exist == true) {
			filePath = sourceFile.getParent();
		} else if (isFile == false) {
			filePath = sourceFile.getPath();
		}
		dialog.setText(title);
		dialog.setFilterExtensions(filterExtensions);
		dialog.setFilterPath(filePath);
		dialog.setFilterNames(new String[] { filterName });
		return dialog.open();
	}

	/**
	 * Gets the active shell.
	 *
	 * @return the active shell
	 */
	public static Shell getActiveShell() {
		return getActiveWorkBenchWindow().getShell();
	}

	/**
	 * Gets the active work bench window.
	 *
	 * @return the active work bench window
	 */
	public static IWorkbenchWindow getActiveWorkBenchWindow() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final WorkBenchWindowWrapper windowWrapper = new WorkBenchWindowWrapper();
		workbench.getDisplay().syncExec(new Runnable() {
			public void run() {
				IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
				windowWrapper.setWorkbenchWindow(window);
				if (window == null) {
					if (workbench.getWorkbenchWindowCount() > 0) {
						window = workbench.getWorkbenchWindows()[0];
						windowWrapper.setWorkbenchWindow(window);
					}
				}

			}
		});
		return windowWrapper.getWorkbenchWindow();
	}

	/**
	 * This dialogs open up a modal file dialog The modal behaviour is on
	 * application level.
	 *
	 * @param title the title
	 * @param filterExtensions the filter extensions
	 * @return the string
	 */
	public static String modalFileDialog(final String title,
			final String... filterExtensions) {
		final FileDialog dialog = new FileDialog(getActiveShell(), SWT.OPEN
				| SWT.APPLICATION_MODAL);
		dialog.setText(title);
		dialog.setFilterExtensions(filterExtensions);
		return dialog.open();
	}

	/**
	 * Directory dialog.
	 *
	 * @param title the title
	 * @param filterPath the filter path
	 * @return the string
	 */
	public static String directoryDialog(final String title,
			final String filterPath) {
		final DirectoryDialog dialog = new DirectoryDialog(getActiveShell(),
				SWT.PUSH);
		dialog.setText(title);
		if (StringUtils.isNotBlank(filterPath))
			dialog.setFilterPath(filterPath);
		return dialog.open();
	}

	/**
	 * Run job in ui dialog.
	 *
	 * @param <J> the generic type
	 * @param job the job
	 * @return the j
	 */
	public static <J extends Job> J runJobInUIDialog(J job) {
		getWorkbench().getProgressService()
				.showInDialog(
						PlatformUI.getWorkbench().getActiveWorkbenchWindow()
								.getShell(), job);
		return job;
	}

	/**
	 * Open property page.
	 *
	 * @param adaptable the adaptable
	 * @param pageID the page id
	 */
	public static void openPropertyPage(final IAdaptable adaptable,
			final String pageID) {
		final Runnable runnable = new Runnable() {
			public void run() {
				PreferencesUtil.createPropertyDialogOn(
						PlatformUI.getWorkbench().getActiveWorkbenchWindow()
								.getShell(), adaptable, pageID, null, null)
						.open();
			}
		};
		Display.getCurrent().asyncExec(runnable);
	}

	/**
	 * Open choice dialog.
	 *
	 * @param title the title
	 * @param message the message
	 * @param type the type
	 * @param yesLabel the yes label
	 * @param noLabel the no label
	 * @return true, if successful
	 */
	public static boolean openChoiceDialog(final String title,
			final String message, final int type, final String yesLabel,
			final String noLabel) {
		final boolean[] returnValue = new boolean[1];
		final Runnable runnable = new Runnable() {
			public void run() {
				MessageDialog dialog = new MessageDialog(getWorkbench()
						.getActiveWorkbenchWindow().getShell(),
						"eBay SOA Plugin: " + title, null, message, type,
						new String[] { yesLabel, noLabel }, 0);
				dialog.setBlockOnOpen(true);
				returnValue[0] = dialog.open() == Window.OK;
			}
		};
		Display.getCurrent().syncExec(runnable);
		return returnValue[0];
	}

	/**
	 * Open choice dialog.
	 *
	 * @param title the title
	 * @param message the message
	 * @param type the type
	 * @return true, if successful
	 */
	public static boolean openChoiceDialog(final String title,
			final String message, final int type) {
		final boolean[] returnValue = new boolean[1];
		final Runnable runnable = new Runnable() {
			public void run() {
				MessageDialog dialog = new MessageDialog(getWorkbench()
						.getActiveWorkbenchWindow().getShell(),
						"eBay SOA Plugin: " + title, null, message, type,
						new String[] { IDialogConstants.OK_LABEL,
								IDialogConstants.CANCEL_LABEL }, 0);
				dialog.setBlockOnOpen(true);
				returnValue[0] = dialog.open() == Window.OK;
			}
		};
		Display.getCurrent().syncExec(runnable);
		return returnValue[0];
	}
	
	/**
	 * Gets the detailed exception stack trace.
	 *
	 * @param exception the exception
	 * @return the detailed exception stack trace
	 */
	public static IStatus getDetailedExceptionStackTrace(final Throwable exception) {
		IStatus status = null;
		if (exception instanceof CoreException) {
			status = ((CoreException) exception).getStatus();
		} else {
			final String reason;
			if (exception.getCause() != null) {
				reason = exception.getCause().getLocalizedMessage();
			} else {
				reason = exception.getLocalizedMessage();
			}

			MultiStatus multiStatus = new MultiStatus(
					Activator.PLUGIN_ID, 1, StringUtils
							.defaultString(reason), null);
			status = multiStatus;
			String stackTrace = ExceptionUtils.getStackTrace(exception);
			BufferedReader br = new BufferedReader(new StringReader(
					stackTrace));
			String line = null;
			try {
				while ((line = br.readLine()) != null) {
					multiStatus.merge(new Status(IStatus.ERROR,
							Activator.PLUGIN_ID, 1, line, null));
				}
			} catch (IOException ioex) {
				ioex.printStackTrace();

			} finally {
				IOUtils.closeQuietly(br);
			}
		}
		
		return status;
	}

	/**
	 * open the error dialog and display error stacktrace.
	 *
	 * @param parent the parent shell of the dialog, or <code>null</code> if none
	 * @param dialogTitle the title to use for this dialog, or <code>null</code> to
	 * indicate that the default title should be used,default is
	 * "Problem Occurred"
	 * @param message the message to show in this dialog, or <code>null</code> to
	 * indicate that the exception's message should be shown as the
	 * primary message
	 * @param exception the exception used to display stackTrace ,or <code>null</code>
	 * to indicate that the exception is not available,only
	 * errorMessage will be shown.
	 */
	public static void showErrorDialog(final Shell parent,
			final String dialogTitle, final String message,
			final Throwable exception) {
		final Runnable runnable = new Runnable() {

			public void run() {
				IStatus status = null;
				if (exception == null) {
					showErrorDialog(parent, "", dialogTitle, message);
					return;
				}
				if (exception instanceof CoreException) {
					status = ((CoreException) exception).getStatus();
				} else {
					final String reason;
					if (exception.getCause() != null) {
						reason = exception.getCause().getLocalizedMessage();
					} else {
						reason = exception.getLocalizedMessage();
					}

					MultiStatus multiStatus = new MultiStatus(
							Activator.PLUGIN_ID, 1, StringUtils
									.defaultString(reason), null);
					status = multiStatus;
					String stackTrace = ExceptionUtils.getStackTrace(exception);
					BufferedReader br = new BufferedReader(new StringReader(
							stackTrace));
					String line = null;
					try {
						while ((line = br.readLine()) != null) {
							multiStatus.merge(new Status(IStatus.ERROR,
									Activator.PLUGIN_ID, 1, line, null));
						}
					} catch (IOException ioex) {
						ioex.printStackTrace();

					} finally {
						IOUtils.closeQuietly(br);
					}
				}
				String finalMessage = message;
				if (StringUtils.isBlank(message)) {
					// The message is blank, try to have a message for it.
					finalMessage = StringUtils.isNotBlank(exception
							.getLocalizedMessage()) ? exception
							.getLocalizedMessage() : exception.toString();
				}

				SOAErrorDialog.openError(parent, dialogTitle, finalMessage,
						status, IStatus.ERROR | IStatus.WARNING);

			}

		};
		if (Display.getCurrent() == null) {
			// not running in a UI thread.
			new UIJob("Error Occured") {
				@Override
				public IStatus runInUIThread(IProgressMonitor monitor) {
					runnable.run();
					return Status.OK_STATUS;
				}
			}.schedule();
		} else {
			runnable.run();
		}

	}

	/**
	 * Show error dialog.
	 *
	 * @param dialogTitle the title to use for this dialog, or <code>null</code> to
	 * indicate that the default title should be used,default is
	 * "Problem Occurred"
	 * @param exception the exception
	 */
	public static void showErrorDialog(String dialogTitle, Throwable exception) {
		showErrorDialog(null, dialogTitle, null, exception);
	}
	
	/**
	 * Show error dialog.
	 *
	 * @param dialogTitle the dialog title
	 * @param message the message
	 * @param exception the exception
	 */
	public static void showErrorDialog(String dialogTitle, String message,  Throwable exception) {
		showErrorDialog(null, dialogTitle, message, exception);
	}

	/**
	 * This will use the default shell, and the dialog titiel will be "Problem
	 * Occurred".
	 *
	 * @param exception the exception
	 */
	public static void showErrorDialog(Throwable exception) {
		showErrorDialog(null, null, null, exception);
	}

	/**
	 * Show error dialog.
	 *
	 * @param shell the shell
	 * @param title the title
	 * @param message the message
	 */
	public static void showErrorDialog(Shell shell, String title, String message) {
		StringWriter msg = new StringWriter();
		if (message != null) {
			msg.write(message);
			msg.write("\n\n"); //$NON-NLS-1$
		}
		MessageDialog.openError(shell, title, msg.toString());
	}

	/**
	 * Show error dialog.
	 *
	 * @param shell the shell
	 * @param title the title
	 * @param status the status
	 * @return the int
	 */
	public static int showErrorDialog(Shell shell, String title, IStatus status) {
		return showErrorDialog(shell, title, status.getMessage(), status);
	}
	
	/**
	 * Show error dialog.
	 *
	 * @param shell the shell
	 * @param title the title
	 * @param message the message
	 * @param status the status
	 * @param needLineBreak the need line break
	 * @param needLineTitle the need line title
	 * @param needCancelButton the need cancel button
	 * @param changeButtonLabel the change button label
	 * @return the int
	 */
	public static int showErrorDialog(Shell shell, String title, String message, IStatus status, 
			boolean needLineBreak, boolean needLineTitle, boolean needCancelButton, 
			boolean changeButtonLabel) {
		return SOAErrorDialog.openError(shell, title, message,
				status, IStatus.ERROR | IStatus.WARNING, 
						needLineBreak, needLineTitle, 
				needCancelButton, changeButtonLabel);
	}
	
	/**
	 * Show error dialog.
	 *
	 * @param shell the shell
	 * @param title the title
	 * @param message the message
	 * @param status the status
	 * @return the int
	 */
	public static int showErrorDialog(Shell shell, String title, String message, IStatus status) {
		return SOAErrorDialog.openError(shell, title, message,
				status, IStatus.ERROR | IStatus.WARNING);
	}

	/**
	 * Show error dialog.
	 *
	 * @param shell the shell
	 * @param exceptionMessage the exception message
	 * @param title the title
	 * @param message the message
	 */
	public static void showErrorDialog(Shell shell, String exceptionMessage,
			String title, String message) {
		StringWriter msg = new StringWriter();
		if (message != null) {
			msg.write(message);
			msg.write("\n\n"); //$NON-NLS-1$
		}
		if (exceptionMessage == null || exceptionMessage.length() == 0){
			// bug http://quickbugstage.arch.ebay.com/show_bug.cgi?id=16775
			// in fact there is no more details 
			// msg.write("See error log for more details.");
		} else{
			msg.write(exceptionMessage);
		}
		MessageDialog.openError(shell, title, msg.toString());
	}

	/**
	 * Show error dialog in new thread.
	 *
	 * @param shell the shell
	 * @param title the title
	 * @param message the message
	 */
	public static void showErrorDialogInNewThread(Shell shell, String title,
			final String message) {
		final Shell parent = shell != null ? shell : getActiveShell();
		final String dialogTitle = StringUtils.isNotBlank(title) ? title
				: "Error";
		if (StringUtils.isNotBlank(message)) {
			new UIJob("Opening Error Dialog") {

				@Override
				public IStatus runInUIThread(IProgressMonitor monitor) {
					MessageDialog.openError(parent, dialogTitle, message);
					return Status.OK_STATUS;
				}

			}.schedule();
		}
	}

	/**
	 * Returns a width hint for a button control.
	 * 
	 * @param button
	 *            the button
	 * @return the width hint
	 */
	public static int getButtonWidthHint(Button button) {
		button.setFont(JFaceResources.getDialogFont());
		Font font = button.getFont();
		GC gc = new GC(font.getDevice());
		gc.setFont(font);
		int widthHint = Dialog.convertHorizontalDLUsToPixels(gc
				.getFontMetrics(), IDialogConstants.BUTTON_WIDTH);
		gc.dispose();
		return Math.max(widthHint, button.computeSize(SWT.DEFAULT, SWT.DEFAULT,
				true).x);
	}

	/**
	 * Sets width and height hint for the button control. <b>Note:</b> This is
	 * a NOP if the button's layout data is not an instance of
	 * <code>GridData</code>.
	 * 
	 * @param button
	 *            the button for which to set the dimension hint
	 */
	public static void setButtonWidthHint(Button button) {
		Assert.isNotNull(button);
		setButtonWidthHint(button, getButtonWidthHint(button));
	}

	private static void setButtonWidthHint(Button button, int widthHint) {
		Assert.isNotNull(button);
		Object gd = button.getLayoutData();
		if (gd == null) {
			gd = new GridData();
			button.setLayoutData(gd);
		}
		if (gd instanceof GridData) {
			((GridData) gd).widthHint = widthHint;
			((GridData) gd).horizontalAlignment = GridData.FILL;
		}

	}

	/**
	 * make buttons with equal widthHint.
	 *
	 * @param buttons the new equal width hint for buttons
	 */
	public static void setEqualWidthHintForButtons(Button... buttons) {
		int max = 0;
		int curButtonWidthHint = 0;
		for (Button button : buttons) {
			curButtonWidthHint = getButtonWidthHint(button);
			max = Math.max(max, curButtonWidthHint);
		}

		for (Button button2 : buttons) {
			setButtonWidthHint(button2, max);
		}
	}

	/**
	 * Sync exec.
	 *
	 * @param <T> the generic type
	 * @param name the name
	 * @param runnable the runnable
	 * @return the t
	 */
	public static <T> T syncExec(String name, final IRunnable<T> runnable) {
		if (Thread.currentThread() == display().getThread())
			return runnable.run();
		return UIRunner.syncExec(name, runnable);
	}

	/**
	 * The Class UIRunner.
	 *
	 * @param <T> the generic type
	 */
	public static final class UIRunner<T> extends UIJob {
		private enum Sync {
			SYNC, ASYNC
		}

		/**
		 * Sync exec.
		 *
		 * @param <U> the generic type
		 * @param name the name
		 * @param runner the runner
		 * @return the u
		 */
		public static final <U> U syncExec(String name, IRunnable<U> runner) {
			checkNull(runner);
			return doExec(new UIRunner<U>(name, runner), Sync.SYNC);
		}

		/**
		 * Async exec.
		 *
		 * @param <U> the generic type
		 * @param name the name
		 * @param runner the runner
		 * @return the u
		 */
		public static final <U> U asyncExec(String name, IRunnable<U> runner) {
			checkNull(runner);
			return doExec(new UIRunner<U>(name, runner), Sync.ASYNC);
		}

		/**
		 * Sync exec.
		 *
		 * @param <U> the generic type
		 * @param name the name
		 * @param runner the runner
		 * @return the u
		 */
		public static final <U> U syncExec(String name, Runnable runner) {
			checkNull(runner);
			return doExec(new UIRunner<U>(name, runner), Sync.SYNC);
		}

		/**
		 * Async exec.
		 *
		 * @param <U> the generic type
		 * @param name the name
		 * @param runner the runner
		 * @return the u
		 */
		public static final <U> U asyncExec(String name, Runnable runner) {
			checkNull(runner);
			return doExec(new UIRunner<U>(name, runner), Sync.ASYNC);
		}

		private static void checkNull(Object o) {
			if (null == o) {
				throw new IllegalArgumentException("null runner");
			}
		}

		private static final <U> U doExec(UIRunner<U> runner, Sync sync) {
			if (sync == Sync.ASYNC) {
				runner.asyncExec();
			} else {
				runner.syncExec();
			}
			return runner.returnValue();
		}

		private static String nonNullName(String name) {
			return null == name ? DEFAULT_NAME : name;
		}

		private static final String DEFAULT_NAME = "UIUtil";

		private final Runnable runnable;
		private final IRunnable<T> irunner;
		private T returnValue;

		/**
		 * Instantiates a new uI runner.
		 *
		 * @param name the name
		 * @param irunner the irunner
		 */
		public UIRunner(String name, IRunnable<T> irunner) {
			super(nonNullName(name));
			this.irunner = irunner;
			this.runnable = null;
		}

		/**
		 * Instantiates a new uI runner.
		 *
		 * @param name the name
		 * @param runnable the runnable
		 */
		public UIRunner(String name, Runnable runnable) {
			super(nonNullName(name));
			this.runnable = runnable;
			this.irunner = null;
		}

		/**
		 * Do run.
		 */
		protected void doRun() {
			if (null != runnable) {
				runnable.run();
			} else if (null != irunner) {
				returnValue = irunner.run();
			}
		}

		/**
		 * Sync exec.
		 */
		public void syncExec() {
			if ((runnable == null) && (irunner == null)) {
				return;
			}
			final Display display = display();
			if (Thread.currentThread() == display.getThread()) {
				doRun();
				return;
			}
			final List<T> sink = new ArrayList<T>();
			final Runnable wrapper = new Runnable() {
				public void run() {
					syncExec();
					sink.add(returnValue);
				}
			};
			display.syncExec(wrapper);
			returnValue = sink.size() > 0 ? sink.get(0) : null;
		}

		/**
		 * Async exec.
		 */
		public void asyncExec() {
			if ((runnable == null) && (irunner == null)) {
				return;
			}
			final Display display = display();
			if (Thread.currentThread() == display.getThread()) {
				doRun();
				return;
			}
			final List<T> sink = new ArrayList<T>();
			final Runnable wrapper = new Runnable() {
				public void run() {
					asyncExec();
					sink.add(returnValue);
				}
			};
			display.asyncExec(wrapper);
			returnValue = sink.size() > 0 ? sink.get(0) : null;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.ui.progress.UIJob#runInUIThread(org.eclipse.core.runtime.IProgressMonitor)
		 */
		@Override
		public IStatus runInUIThread(IProgressMonitor monitor) {
			IStatus result = Status.OK_STATUS;
			try {
				if (null != runnable) {
					runnable.run();
				} else if (null != irunner) {
					returnValue = irunner.run();
				}
			} catch (Exception e) {
				result = EclipseMessageUtils.createErrorStatus(null, e);
			} catch (Error e) {
				result = EclipseMessageUtils.createErrorStatus(null, e);
			}
			return result;
		}

		/**
		 * Return value.
		 *
		 * @return the t
		 */
		public T returnValue() {
			return returnValue;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.core.runtime.jobs.Job#belongsTo(java.lang.Object)
		 */
		@Override
		public boolean belongsTo(final Object family) {
			return UIUtil.belongsTo(family);
		}
	}

	/**
	 * Belongs to.
	 *
	 * @param family the family
	 * @return true, if successful
	 */
	public static boolean belongsTo(final Object family) {
		return ResourcesPlugin.FAMILY_MANUAL_BUILD.equals(family)
				|| ResourcesPlugin.FAMILY_AUTO_BUILD.equals(family)
				|| ResourcesPlugin.FAMILY_AUTO_REFRESH.equals(family)
				|| "org.eclipse.jst.j2ee.refactor.component".equals(family)
				|| "EAR Libraries Update Job".equals(family);
	}

	private static class WorkBenchWindowWrapper {
		private IWorkbenchWindow workbenchWindow;

		public IWorkbenchWindow getWorkbenchWindow() {
			return workbenchWindow;
		}

		public void setWorkbenchWindow(IWorkbenchWindow workbenchWindow) {
			this.workbenchWindow = workbenchWindow;
		}
	}

	private static class WorkBenchPageWrapper {
		private IWorkbenchPage workbenchPage = null;

		public IWorkbenchPage getWorkbenchPage() {
			return workbenchPage;
		}

		public void setWorkbenchPage(IWorkbenchPage workbenchPage) {
			this.workbenchPage = workbenchPage;
		}
	}

	private static class EditorWrapper {
		private IEditorPart editorPart = null;

		public IEditorPart getEditorPart() {
			return editorPart;
		}

		public void setEditorPart(IEditorPart editorPart) {
			this.editorPart = editorPart;
		}

	}

	/**
	 * Gets the active page.
	 *
	 * @return the active page
	 */
	public static IWorkbenchPage getActivePage() {
		final IWorkbenchWindow workbenchWindow = getActiveWorkBenchWindow();
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final WorkBenchPageWrapper workBenchPageWrapper = new WorkBenchPageWrapper();
		if (workbenchWindow != null) {

			workbench.getDisplay().syncExec(new Runnable() {
				public void run() {
					IWorkbenchPage workbenchPage = workbenchWindow
							.getActivePage();
					workBenchPageWrapper.setWorkbenchPage(workbenchPage);
					if (workbenchPage == null) {
						if (workbenchWindow.getPages() != null
								&& workbenchWindow.getPages().length > 0) {
							workbenchPage = workbenchWindow.getPages()[0];
							workBenchPageWrapper
									.setWorkbenchPage(workbenchPage);
						}
					}

				}
			});
		}
		return workBenchPageWrapper.getWorkbenchPage();
	}

	/**
	 * Gets the active editor.
	 *
	 * @return the active editor
	 */
	public static IEditorPart getActiveEditor() {
		final IWorkbenchPage activePage = getActivePage();
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final EditorWrapper editorWrapper = new EditorWrapper();
		if (activePage != null) {

			workbench.getDisplay().syncExec(new Runnable() {
				public void run() {
					IEditorPart editorPart = activePage.getActiveEditor();
					editorWrapper.setEditorPart(editorPart);
					if (editorPart == null) {
						if (activePage.getEditorReferences() != null
								&& activePage.getEditorReferences().length > 0) {
							editorPart = activePage.getEditorReferences()[0]
									.getEditor(true);
							editorWrapper.setEditorPart(editorPart);
						}
					}

				}
			});
		}
		return editorWrapper.getEditorPart();
	}

	/**
	 * returns the parent project of the file opened in the active editor.
	 * 
	 * @return - the parent project of the editors file
	 * @throws SOAPartNotFoundException
	 *             if the editor is not active or if the editors input is not
	 *             FileEditorInput
	 */
	public static IProject getActiveEditorsProject()
			throws SOAPartNotFoundException {
		if (getActiveEditor() == null
				|| !(getActiveEditor().getEditorInput() instanceof FileEditorInput))
			throw new SOAPartNotFoundException(
					"The active editor does not exist or is not a File editor.");
		return ((FileEditorInput) getActiveEditor().getEditorInput()).getFile()
				.getProject();
	}
	
	/**
	 * The Interface ISOAControlDecorator.
	 */
	public static interface ISOAControlDecorator {
		
		/**
		 * Adds the control decoration.
		 *
		 * @param control the control
		 * @param controlDecoration the control decoration
		 */
		public void addControlDecoration(Control control, ControlDecoration controlDecoration);
	}
	
	/**
	 * decorate control with tooltip.
	 *
	 * @param decorator the decorator
	 * @param control the control
	 * @param tooltip the tooltip
	 * @return the control decoration
	 */
	public static ControlDecoration decorateControl(ISOAControlDecorator decorator, 
			final Control control, final String tooltip) {
		ControlDecoration controlDecoration = null;
		if (control != null && StringUtils.isNotBlank(tooltip)) {
			
			control.setToolTipText(tooltip);
			controlDecoration = new ControlDecoration(control, 
					SWT.LEFT | SWT.TOP);
			controlDecoration.setShowOnlyOnFocus(true);
			controlDecoration.setDescriptionText(tooltip);
			FieldDecoration fieldDecoration = FieldDecorationRegistry
			.getDefault().getFieldDecoration(
			FieldDecorationRegistry.DEC_INFORMATION);
			controlDecoration.setImage(fieldDecoration.getImage());
			if (decorator != null) {
				decorator.addControlDecoration(control, controlDecoration);
			}
			
		}
		return controlDecoration;
	}
	
	/**
	 * Gets the status line manager.
	 *
	 * @return the status line manager
	 */
	public static IStatusLineManager getStatusLineManager() {
		IWorkbenchPage page =  getActivePage();
		IWorkbenchPart part = page.getActivePart();
		IWorkbenchPartSite site = part.getSite();
		if (site instanceof IViewSite) {
			IViewSite vSite = ( IViewSite ) site;
			IActionBars actionBars =  vSite.getActionBars();
	
			if( actionBars == null ) {
				return null;
			}
			
			return actionBars.getStatusLineManager();
		} 
		return null;
		
	}
}
