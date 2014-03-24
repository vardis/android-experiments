package com.giorgos.preferencestest.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * This sample demonstrates how to build a preferences screen using the built-in XML Preferences
 * framework from Android. It supports two different modes of operations:
 * <ol>
 *     <li>
 *         Compatibility mode
 *         The preferences are loaded through XmlTestCompatibilityPreferencesActivity which extends
 *         the PreferencesActivity class.
 *     </li>
 *     <li>
 *         Current mode
 *         The preferences are loaded through a Fragment class which loads the respective preferences
 *         XML resources. In this sample however, it goes a small step further, by also utilizing the
 *         Headers mechanism. Whereby the activity doesn't load the preferences fragment directly. Instead
 *         it loads another XML resource which defines the preferences headers. Each header is associated
 *         with a single fragment which is automatically loaded by Android when the corresponding header
 *         is selected. The great benefit of using headers is that large screen can display simultaneously
 *         the list of headers on the left side and the corresponding fragment with the preferences views
 *         on the right side.
 *     </li>
 * </ol>
 * To use the built-in XML Preferences framework, we need to create:
 * <ul>
 *     <li>A XML resource that describes a single preferences screen</li>
 *     <li>A Fragment class that extends PreferenceFragment and loads the XML resource
 *     in its onCreate method by calling addPreferencesFromResource(R.xml...).
 *     We need to specify a title, a summary and an icon per fragment.
 *     </li>
 *     <li>For Android 3.0+, a Preference header XML resource. Each header identifies one or more preference
 *     fragments. We need to specify a title, a summary and an icon per header.
 *     </li>
 *     <li>
 *         For Android 3.0+, an activity class that extends PreferenceActivity which will load
 *         the headers xml resources in its onBuildHeaders method by invoking the loadHeadersFromResource method.
 *         Actually it is not required to extend the PreferenceActivity class but that will give automatically
 *         extra functionality for large screens. In those cases, the headers are displayed on the left
 *         hand side while the actual preference view is displayed on the right.
 *     </li>
 *     <li>
 *         For Android up to 2.3, an activity class that extends PreferenceActivity which will load
 *         the XML resource for the preferences screen directly in its onCreate method by invoking
 *         the addPreferencesFromResource method.
 *     </li>
 * </ul>
 *
 * The problem with the headers approach is that it creates one more intermediate screen and therefore
 * the user has to click twice on the back button in order to return to the activity.
 *
 * The old way will be used if either the Android version is less than 3.0 or the boolean field forceCompatibilityMode
 * is set to true.
 */
public class MainActivity extends ActionBarActivity {

    public static final int MENU_PREFERENCES = 1;
    public static final int SHOW_PREFERENCES = 2;

    public static final String PREF_AUTO_UPDATE = "PREF_AUTO_UPDATE";
    public static final String PREF_PROVIDER = "PREF_PROVIDER";

    private boolean forceCompatibilityMode = false;
    private TextView autoUpdateView;
    private TextView providerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        autoUpdateView = (TextView) findViewById(R.id.textAutoUpdate);
        providerView = (TextView) findViewById(R.id.textProvider);

        // sets default values but only the first time the user opens the application
        // subsequent calls will not overwrite the existing preferences with any default values
        PreferenceManager.setDefaultValues(this, R.xml.prefs, false);
        updateFromPreferences();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {

            case (R.id.action_settings): {
                Class c = forceCompatibilityMode || (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) ?
                        XmlTestCompatibilityPreferencesActivity.class : XmlTestPreferencesActivity.class;
                Intent i = new Intent(this, c);

                startActivityForResult(i, SHOW_PREFERENCES);
                return true;
            }
        }
        return false;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SHOW_PREFERENCES) {
            updateFromPreferences();
        }

    }

    private void updateFromPreferences() {
        Context context = getApplicationContext();
        SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(context);
        autoUpdateView.setText(Boolean.toString(prefs.getBoolean(PREF_AUTO_UPDATE, false)));
        providerView.setText(prefs.getString(PREF_PROVIDER, "None!"));
    }

}
