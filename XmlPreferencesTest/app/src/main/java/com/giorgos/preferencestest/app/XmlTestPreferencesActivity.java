package com.giorgos.preferencestest.app;

import android.preference.PreferenceActivity;

import java.util.List;

/**
 * For Android versions equal or later to 3.0, the PreferenceActivity
 * can either use the FragmentManager to directly display a PreferenceFragment
 * or utilise the Headers mechanism.
 * In this case, just for completeness, I chose to load the headers XML resource
 * as well and only load the associate fragment once the use has selected the header.
 */
public class XmlTestPreferencesActivity extends PreferenceActivity {
    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.header, target);
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return true;
    }
}
