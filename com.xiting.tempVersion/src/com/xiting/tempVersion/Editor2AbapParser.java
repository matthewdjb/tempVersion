package com.xiting.tempVersion;

import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.IFileEditorInput;

public class Editor2AbapParser implements IAdt2AbapParser {

	private MultiPageEditorPart editor;

	Editor2AbapParser(MultiPageEditorPart editor) {
		this.editor = editor;
	}

	@Override
	public String getName() {
		if (isFunctionInclude()) {
			return removeProject(getGroupFromInclude());
		} else {
			return removeProject(editor.getTitle());
		}
	}

	@Override
	public String getType() {
		IFileEditorInput editorInput = (IFileEditorInput) editor.getEditorInput();
		String type = ""; //$NON-NLS-1$
		switch (editorInput.getFile().getFileExtension()) {
		case "acinc": //$NON-NLS-1$
		case "aclass": //$NON-NLS-1$
			type = "CLAS"; //$NON-NLS-1$
			break;
		case "asfunc": //$NON-NLS-1$
			type = "FUNC"; //$NON-NLS-1$
			break;
		case "asinc": //$NON-NLS-1$
		case "asprog": //$NON-NLS-1$
			type = "PROG"; //$NON-NLS-1$
			break;
		case "asfugr": //$NON-NLS-1$
		case "asfinc": //$NON-NLS-1$
			type = "FUGR"; //$NON-NLS-1$
			break;
		case "aint": //$NON-NLS-1$
			type = "INTF"; //$NON-NLS-1$
			break;
		}
		return type;
	}

	private boolean isFunctionInclude() {
		IFileEditorInput editorInput = (IFileEditorInput) editor.getEditorInput();
		return editorInput.getFile().getFileExtension() == "asfinc"; //$NON-NLS-1$
	}

	private String getGroupFromInclude() {
		IFileEditorInput editorInput = (IFileEditorInput) editor.getEditorInput();
		String fileName = editorInput.getFile().getName();
		return Adt2AbapParserFactory.getGroupFromFunctionInclude(fileName);
	}

	private String removeProject(String name) {
		String[] parts = name.split(" "); //$NON-NLS-1$
		return parts[parts.length - 1];
	}
}
