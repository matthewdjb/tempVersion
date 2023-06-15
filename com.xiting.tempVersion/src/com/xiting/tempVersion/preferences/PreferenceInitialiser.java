package com.xiting.tempVersion.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

public class PreferenceInitialiser  extends AbstractPreferenceInitializer {
	private static final String RFC = "Z_SVRS_AFTER_CHANGED_ONLINE"; //$NON-NLS-1$
	private ScopedPreferenceStore preferenceStore;

	public PreferenceInitialiser() {
		preferenceStore = new ScopedPreferenceStore(InstanceScope.INSTANCE, "com.xiting.tempVersion");
	}
	
	@Override
	public void initializeDefaultPreferences() {
		preferenceStore.setDefault("FM_NAME", RFC );
	}
}
