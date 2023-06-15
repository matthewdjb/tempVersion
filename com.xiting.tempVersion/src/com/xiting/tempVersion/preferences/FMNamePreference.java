package com.xiting.tempVersion.preferences;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

public class FMNamePreference extends PreferencePage implements IWorkbenchPreferencePage {

	private IPreferenceStore preferenceStore;
	private Text txtName;

	public FMNamePreference() {
		preferenceStore = new ScopedPreferenceStore(InstanceScope.INSTANCE, "com.xiting.tempVersion");
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite comp = new Composite(parent, SWT.BORDER);
		GridLayoutFactory.swtDefaults().numColumns(3).applyTo(comp);
		Label lblFMName = new Label(comp, SWT.NONE);
		lblFMName.setText("Function Module Name");
		txtName = new Text(comp, SWT.BORDER);
		txtName.setText(getPreferenceStore().getString("FM_NAME"));
		return comp;
	}

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(preferenceStore); // $NON-NLS-1$);
	}

	@Override
	protected void performDefaults() {
		txtName.setText(preferenceStore.getDefaultString("FM_NAME"));
		super.performDefaults();
	}

	@Override
	public boolean performOk() {
		if (okToLeave()) {
			getPreferenceStore().setValue("FM_NAME", txtName.getText());
			return super.performOk();
		}
		return false;
	}
}
