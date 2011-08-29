package org.ebayopensource.turmeric.eclipse.ui.views.registry;

import org.ebayopensource.turmeric.common.config.LibraryType;
import org.ebayopensource.turmeric.eclipse.core.TurmericCoreActivator;
import org.ebayopensource.turmeric.eclipse.ui.UIActivator;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.ebayopensource.turmeric.eclipse.utils.xml.XMLUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDTypeDefinition;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving typeViewerDC events.
 * The class that is interested in processing a typeViewerDC
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addTypeViewerDCListener</code> method. When
 * the typeViewerDC event occurs, that object's appropriate
 * method is invoked.
 *
 * @see TypeViewerDCEvent
 */
class TypeViewerDCListener implements IDoubleClickListener {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doubleClick(DoubleClickEvent event) {
		if (event.getSelection() instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) event
					.getSelection();
			if (structuredSelection.getFirstElement() instanceof LibraryType) {
				LibraryType selectedLibraryType = (LibraryType) structuredSelection
						.getFirstElement();
				XSDSchema xsdSchema = null;
				try {
					xsdSchema = TurmericCoreActivator.parseSchema(UIActivator
							.getXSD(selectedLibraryType));
					XSDTypeDefinition typeDef = xsdSchema
							.getTypeDefinitions().get(0);

					MessageDialog.openInformation(UIUtil.getActiveShell(),
							"Content of the selected xsd", TurmericCoreActivator.formatContents(XMLUtil
											.convertXMLToString(typeDef
													.getElement())));
				} catch (Exception e) {
					// Silently die
					// this is not an exposed feature.
				}
			}
		}
	}
}