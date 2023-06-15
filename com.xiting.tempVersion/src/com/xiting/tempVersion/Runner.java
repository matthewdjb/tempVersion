package com.xiting.tempVersion;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;

import com.sap.adt.project.IAdtCoreProject;
import com.sap.adt.project.IProjectProvider;
import com.sap.adt.project.ui.util.ProjectUtil;
import com.sap.adt.projectexplorer.ui.node.IAbapRepositoryObjectNode;
import com.sap.adt.tools.abapsource.sources.objectstructure.IObjectStructureElement;
import com.sap.adt.tools.abapsource.ui.sources.outline.IAdtOutlineTreeNode;
import com.sap.adt.tools.core.IAdtObjectReference;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;

@SuppressWarnings("restriction")
public class Runner {

	public void run(ISelection selection) {
		IProject project = ProjectUtil.getActiveAdtCoreProject(selection, null, null,
				IAdtCoreProject.ABAP_PROJECT_NATURE);
		String name = "";
		String type = "";
		if (selection instanceof IStructuredSelection) {
			Object[] selectedArray = ((IStructuredSelection) selection).toArray();
			for (int i = 0; i < selectedArray.length; ++i) {
				Object selected = selectedArray[i];
				if (selected instanceof IAbapRepositoryObjectNode) {
					IAbapRepositoryObjectNode selectedNode = (IAbapRepositoryObjectNode) selected;
					IAdtObjectReference navigationTarget = selectedNode.getNavigationTarget();
					name = navigationTarget.getName();
					type = navigationTarget.getType();
				} else if (selected instanceof IAdtOutlineTreeNode) {
					IAdtOutlineTreeNode outlineNode = (IAdtOutlineTreeNode) selected;
					Object dataObject = outlineNode.getData();
					if (dataObject instanceof IObjectStructureElement) {
						IObjectStructureElement structureElement = (IObjectStructureElement) dataObject;
						name = structureElement.getAttribute("name");
						type = structureElement.getAttribute("type");
					}
				}
				if (!(type == null))
					type = AbapAdtTypes.toAbapType(type).name();
			}

		}
		if (!name.isBlank() && !type.isBlank())
			try {
				String pgmid = getPgmid(type);
				new RFCRunner().callRFC(getDestinationFromProject(project), name, type, pgmid);
			} catch (JCoException e) {
				e.printStackTrace();
			}
		else
			MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "No selection",
					"No ABAP Project selected");

	}

	private String getPgmid(String type) {
		AbapAdtTypes.AbapType abapType = AbapAdtTypes.AbapType.valueOf(type);
		switch (abapType) {
		case FUNC:
		case METH:
		case REPS:
			return "LIMU";
		default:
			return "R3TR";
		}
	}

	public void run(IEditorPart editor) {
		IProject project = null;
		if (editor instanceof IProjectProvider) {
			IProjectProvider projectProvider = (IProjectProvider) editor;
			project = projectProvider.getProject();
		}
		if (project == null) {
			MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "No selection",
					"No ABAP Project selected");
		} else {
			IAdt2AbapParser parser = Adt2AbapParserFactory.getInstance(editor);
			String name = parser.getName();
			String type = parser.getType();
			System.out.println(name + " " + type);
			if (!name.isBlank() && !type.isBlank())
				try {
					String pgmid = getPgmid(type);
					new RFCRunner().callRFC(getDestinationFromProject(project), name, type, pgmid);
				} catch (JCoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	private JCoDestination getDestinationFromProject(IProject project) throws JCoException {
		String destinationId = com.sap.adt.project.AdtCoreProjectServiceFactory.createCoreProjectService()
				.getDestinationId(project);
		return JCoDestinationManager.getDestination(destinationId);
	}

}
