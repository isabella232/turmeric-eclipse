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

class TypeViewerDCListener implements IDoubleClickListener {
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
					XSDTypeDefinition typeDef = (XSDTypeDefinition) xsdSchema
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