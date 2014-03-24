package com.giorgos.preferencestest.app;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import java.util.List;

/**
 * Displays the preferences view. The controls are generated automatically by the XML resources
 * that is passed to addPreferencesFromResource.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class XmlTestPreferencesFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);
    }
}
