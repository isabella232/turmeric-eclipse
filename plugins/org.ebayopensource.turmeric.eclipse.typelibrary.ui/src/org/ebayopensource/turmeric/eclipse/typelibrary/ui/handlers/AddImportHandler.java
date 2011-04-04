package org.ebayopensource.turmeric.eclipse.typelibrary.ui.handlers;

import java.util.ArrayList;
import java.util.Iterator;

import org.ebayopensource.turmeric.common.config.LibraryType;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.wst.ImportTypeFromTypeLibrary;
import org.ebayopensource.turmeric.eclipse.typelibrary.utils.TypeLibraryUtil;
import org.ebayopensource.turmeric.eclipse.ui.views.registry.RegistryView;
import org.ebayopensource.turmeric.eclipse.ui.views.registry.TypeViewer;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.xsd.XSDSchema;

/**
 * The Add Import Action for the type viewer. This action appears in the Types
 * Registry View.  This was converted from a Action to a handler.  The old class is AddImportAction.
 * 
 * @author dcarver
 * @since 1.0
 */
public class AddImportHandler extends AbstractHandler {

	private TypeViewer typeViewer = null;
	private static final String MISSING_EDITOR_ERR_MSG = "The plugin could not find the right editor to import. Please open the XSD/WSDL document in a XSD/WTP editor and try again. ";
	private SOALogger logger = SOALogger.getLogger();
	
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPage page = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();
		IViewPart viewPart = page.findView(RegistryView.VIEW_ID);
		
		if (!(viewPart instanceof RegistryView)) {
			return null;
		}
		
		RegistryView view = (RegistryView) viewPart;
		typeViewer = view.getTypeViewer();
		
		if (typeViewer != null && typeViewer.getSelection() != null
				&& typeViewer.getSelection() instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) typeViewer
					.getSelection();
			ArrayList<LibraryType> selectedTypes = new ArrayList<LibraryType>();
			Iterator<?> iterator = structuredSelection.iterator();
			while (iterator.hasNext()) {
				Object selectedObject = iterator.next();
				if (selectedObject instanceof LibraryType) {
					LibraryType libraryType = (LibraryType) selectedObject;
					selectedTypes.add(libraryType);
				}
			}
			if (!selectedTypes.isEmpty()) {
				IEditorPart editorPart = UIUtil.getActiveEditor();
				if (editorPart == null) {
					UIUtil
							.showErrorDialog(
									null,
									"Editor couldnt be located",
									"Falied to locate an appropriate editor to import.",
									MISSING_EDITOR_ERR_MSG);
					return null;
				}

				Object adaptedObject = TypeLibraryUtil
						.getAdapterClassFromWTPEditors(editorPart);
				if (adaptedObject == null) {
					showEditorErrorMsg();
					return null;
				}

				if (editorPart.getEditorInput() == null
						|| !(editorPart.getEditorInput() instanceof IFileEditorInput)) {
					showEditorErrorMsg();
					return null;
				}

				IFileEditorInput editorInput = (IFileEditorInput) editorPart
						.getEditorInput();
				IFile selectedFile = editorInput.getFile();
				if (!ImportTypeFromTypeLibrary
						.validateSelectedTypeForImport(selectedTypes
								.toArray(new LibraryType[0]), selectedFile)) {
					return null;
				}
				if (adaptedObject instanceof Definition) {
					try {
						ImportTypeFromTypeLibrary
								.performInlineOperationsForWSDLEditor(
										(Definition) adaptedObject,
										selectedTypes
												.toArray(new LibraryType[0]),
										selectedFile);
					} catch (Exception e) {
						logger.error(e);
						showGeneralErrorMsg();
					}
					return null;
				}
				if (adaptedObject instanceof XSDSchema) {
					try {
						ImportTypeFromTypeLibrary
								.performImportTasksForXSDEditor(
										(XSDSchema) adaptedObject,
										selectedTypes
												.toArray(new LibraryType[0]),
										selectedFile);
					} catch (Exception e) {
						logger.error(e);
						showGeneralErrorMsg();
					}
					return null;
				}
			}
		}
		return null;
	}

	private void showEditorErrorMsg() {
		UIUtil
				.showErrorDialog(
						null,
						"Editor couldnt be located",
						"Falied to locate an appropriate editor to import.",
						"Plugin located an editor, but is not a WTP editor. Make sure that you have opened the document in a XSD/WSDL editor and focus the editor before importing");
	}

	private void showGeneralErrorMsg() {
		UIUtil
				.showErrorDialog(
						null,
						"Import failed",
						"Plugin failed to import the selected schema types.",
						"This error is mostly due to the environment that you are using. Please refresh the type registry by clicking the Refresh button in this view, reopen the XSD/WSDL editor and try again.");
	}
	
}
