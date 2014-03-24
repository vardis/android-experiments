package com.giorgos.preferencestest.app;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * For Android versions older than 3.0, instead of using a PreferenceFragment we load directly
 * the XML preferences resource from within a PreferenceActivity.
 */
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class XmlTestCompatibilityPreferencesActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);
    }
}
