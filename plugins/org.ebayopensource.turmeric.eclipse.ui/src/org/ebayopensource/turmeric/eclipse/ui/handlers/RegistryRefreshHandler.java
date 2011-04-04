package org.ebayopensource.turmeric.eclipse.ui.handlers;

import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.SOAGlobalRegistryAdapter;
import org.ebayopensource.turmeric.eclipse.ui.views.registry.RegistryView;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * An event handler that refreshes the Type Library tree view. This invalidates
 * the existing SOA Type Registry and re-populates it. It also uses the Job API
 * to allow the user to move this to a background thread in case it runs too
 * long.
 * 
 * @author dcarver
 * @since 1.0
 */
public class RegistryRefreshHandler extends AbstractHandler implements IHandler {

	private TreeViewer typeLibraryViewer = null;
	private Object input = null;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		init(event);
		if (typeLibraryViewer == null) {
			return null;
		}
		RefreshJob job = new RefreshJob("Registry Refresh Job");
		job.setUser(true);
		job.schedule();
		return null;
	}
	
	private void init(ExecutionEvent event) {
		IWorkbenchPage page = HandlerUtil.getActiveWorkbenchWindow(event)
				.getActivePage();
		IViewPart viewPart = page.findView(RegistryView.VIEW_ID);
		if (!(viewPart instanceof RegistryView)) {
			return;
		}
		RegistryView view = (RegistryView) viewPart;
		typeLibraryViewer = view.getTypeLibraryViewer();
	}

	/**
	 * This class executes a refresh of the treeViewer using the Job API to 
	 * allow the user to move this to a background thread if it takes a long
	 * time to run.   The class fetches the latest Type Libraries from the registry.
	 * 
	 * @author dcarver
	 *
	 */
	private class RefreshJob extends Job {

		public RefreshJob(String name) {
			super(name);
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			monitor.beginTask("Refreshing Registry", 100);
			SOAGlobalRegistryAdapter registryAdapter = SOAGlobalRegistryAdapter.getInstance();
			registryAdapter.invalidateRegistry();
			monitor.worked(50);
			
			if (monitor.isCanceled()) {
				return cancelJob(monitor);
			}
			
			refreshRegistry(monitor, registryAdapter);
			
			if (monitor.isCanceled()) {
				return cancelJob(monitor);
			}
			
			if (input == null) {
				return cancelJob(monitor);
			}
			
			refreshViewers();
			
			monitor.done();
			return Status.OK_STATUS;
		}

		private IStatus cancelJob(IProgressMonitor monitor) {
			monitor.done();
			return Status.CANCEL_STATUS;
		}

		private void refreshViewers() {
			Display.getDefault().asyncExec(new Runnable() {

				/**
				 * Refresh the tree viewer with the data
				 */
				public void run() {
					typeLibraryViewer.setInput(input);
					typeLibraryViewer.refresh();
				};
			});
		}

		private void refreshRegistry(IProgressMonitor monitor,
				SOAGlobalRegistryAdapter registryAdapter) {
			try {
				input = registryAdapter.getGlobalRegistry();
				monitor.worked(30);
			} catch (Exception e) {
				SOALogger.getLogger().error(e);
			}
		}
	}		
}
