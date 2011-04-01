package org.ebayopensource.turmeric.eclipse.ui.handlers;

import org.ebayopensource.turmeric.eclipse.ui.views.registry.RegistryView;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * Used to toggle the state of sorting on and off. This uses the views
 * comparator to toggle whether items are sorted or not in the view.
 * 
 * @author dcarver
 * @since 1.0
 */
public class SortRegistryHandler extends AbstractHandler {

	/**
	 * {@inheritDoc}
	 * 
	 * This sets the state of the comparator for the view for sorting puproses.
	 * 
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPage page = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();
		IViewPart viewPart = page.findView(RegistryView.VIEW_ID);
		
		if (!(viewPart instanceof RegistryView)) {
			return null;
		}
		RegistryView view = (RegistryView) viewPart;
		StructuredViewer viewer = view.getTypeLibraryViewer();
		if (viewer.getComparator() != null) {
			// Toggle sorting off
			viewer.setComparator(null);
		} else { 
			ViewerComparator comparator = view.getLibraryComparator();
			viewer.setComparator(comparator);
		}
		return null;
	}

}
