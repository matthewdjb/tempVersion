package com.xiting.tempVersion;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoStructure;

public class RFCRunner {
	public void callRFC(JCoDestination destination, String name, String type, String pgmid) throws JCoException {
		ScopedPreferenceStore preferences = new ScopedPreferenceStore(InstanceScope.INSTANCE, "com.xiting.tempVersion");
		String fmName = preferences.getString("FM_NAME");
		JCoFunction function = destination.getRepository().getFunction(fmName);
		JCoStructure e071EntryStructure = function.getImportParameterList().getStructure("E071_ENTRY");
		e071EntryStructure.setValue("PGMID", pgmid);
		e071EntryStructure.setValue("OBJECT", type);
		e071EntryStructure.setValue("OBJ_NAME", name);
		function.getImportParameterList().setValue("STATUS", "A");
		function.execute(destination);
		JCoStructure returnStructure = function.getExportParameterList().getStructure("RETURN");
		String message = returnStructure.getString("MESSAGE");
		boolean failed = (function.getExportParameterList().getChar("FAILED") == 'X');
		String version = ((Integer) function.getExportParameterList().getInt("VERSION_NEW")).toString();
		if (failed)
			MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "Temporary Versioning", message);
		else
			MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "Temporary Versioning",
					"Version " + version + " created");
	}

}
