package org.ebayopensource.turmeric.eclipse.ui.handlers;

import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.ui.monitor.typelib.SOAGlobalRegistryAdapter;
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
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * An event handler that refreshes the Type Library tree view.  This invalidates the
 * existing SOA Type Registry and re-populates it.  It also uses the Job API to allow
 * the user to move this to a background thread in case it runs too long.
 * 
 * @author dcarver
 * @since 1.0.0
 */
public class RegistryRefreshHandler extends AbstractHandler implements IHandler {

	private TreeViewer typeLibraryViewer = null;
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPage page = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();
		IViewPart viewPart = page.findView(RegistryView.VIEW_ID);
		if (!(viewPart instanceof RegistryView)) {
			return null;
		}
		RegistryView view = (RegistryView) viewPart;
		typeLibraryViewer = view.getTypeLibraryViewer();
		if (typeLibraryViewer != null) {
			Job job = new Job("Registry Refresh Job") {
 			    @Override
			  	protected IStatus run(IProgressMonitor monitor) {
 			    	monitor.beginTask("Invalidate Registry", IProgressMonitor.UNKNOWN);
					SOAGlobalRegistryAdapter.getInstance().invalidateRegistry();
					try {
						final Object input = SOAGlobalRegistryAdapter.getInstance()
						.getGlobalRegistry();
						typeLibraryViewer.setInput(input);
					} catch (Exception e) {
						SOALogger.getLogger().error(e);
						return Status.CANCEL_STATUS;
					}
		 			typeLibraryViewer.refresh();
		 			return Status.OK_STATUS;
				}
			};
			job.setUser(true);
			job.schedule();
		}
		return null;
	}
}
