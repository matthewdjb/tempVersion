package com.xiting.tempVersion;

import org.eclipse.ui.part.MultiPageEditorPart;

public class Adt2AbapParserFactory {
	public static final IAdt2AbapParser getInstance(Object object) {
		if (object instanceof MultiPageEditorPart) {
			return new Editor2AbapParser((MultiPageEditorPart) object);
		}
		return null;
	}

	static String getGroupFromFunctionInclude(String includeName) {
		return inANameSpace(includeName) ? getGroupWithNameSpace(includeName) : getGroupWithoutNameSpace(includeName);
	}

	private static boolean inANameSpace(String fileName) {
		return fileName.substring(0, 1) == "/"; //$NON-NLS-1$
	}

	private static String getGroupWithNameSpace(String objectName) {
		String splitter = objectName.contains("%2F") ? "%2F" : "//"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		String[] parts = objectName.split(splitter);
		String nameSpace = parts[0];
		String name = parts[1].substring(1);
		return "/" + nameSpace + "/" + name; //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	private static String getGroupWithoutNameSpace(String objectName) {
		return objectName.substring(1);
	}
}
