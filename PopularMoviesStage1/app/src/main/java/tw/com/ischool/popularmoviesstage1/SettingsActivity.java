package tw.com.ischool.popularmoviesstage1;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;

import java.util.Map;

public class SettingsActivity extends AppCompatActivity
//        PreferenceActivity
//        implements Preference.OnPreferenceChangeListener {
{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add 'general' preferences, defined in the XML file
//        addPreferencesFromResource(R.xml.pref_general);

        // For all preferences, attach an OnPreferenceChangeListener so the UI summary can be
        // updated when the preference changes.
//        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_sorting_key)));
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MoviePrefsFragement()).commit();
    }

    /**
     * Attaches a listener so the summary is always updated with the preference value.
     * Also fires the listener once, to initialize the summary (so it shows up before the value
     * is changed.)
     */
//    private void bindPreferenceSummaryToValue(Preference preference) {
//        // Set the listener to watch for value changes.
//        preference.setOnPreferenceChangeListener(this);
//
//        // Trigger the listener immediately with the preference's
//        // current value.
//        onPreferenceChange(preference,
//                PreferenceManager
//                        .getDefaultSharedPreferences(preference.getContext())
//                        .getString(preference.getKey(), ""));
//    }
//
//    @Override
//    public boolean onPreferenceChange(Preference preference, Object value) {
//        String stringValue = value.toString();
//
//        if (preference instanceof ListPreference) {
//            // For list preferences, look up the correct display value in
//            // the preference's 'entries' list (since they have separate labels/values).
//            ListPreference listPreference = (ListPreference) preference;
//            int prefIndex = listPreference.findIndexOfValue(stringValue);
//            if (prefIndex >= 0) {
//                preference.setSummary(listPreference.getEntries()[prefIndex]);
//            }
//        } else {
//            // For other preferences, set the summary to the value's simple string representation.
//            preference.setSummary(stringValue);
//        }
//        return true;
//    }


    public static class MoviePrefsFragement extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
        private SharedPreferences sharedPreferences;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
        }

        @Override
        public void onResume() {
            super.onResume();

            sharedPreferences = getPreferenceManager().getSharedPreferences();

            // we want to watch the preference values' changes
            sharedPreferences.registerOnSharedPreferenceChangeListener(this);

            Map<String, ?> preferencesMap = sharedPreferences.getAll();
            // iterate through the preference entries and update their summary if they are an instance of EditTextPreference
            for (Map.Entry<String, ?> preferenceEntry : preferencesMap.entrySet()) {
                String key = preferenceEntry.getKey() ;
                if (key.equals(getString(R.string.pref_sorting_key))) {
                    ListPreference listPreference = (ListPreference)findPreference(key);
                    setListPreferenceSummary(listPreference);
                }
            }
        }

        @Override
        public void onPause() {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
            super.onPause();
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                              String key) {
            Map<String, ?> preferencesMap = sharedPreferences.getAll();

            // get the preference that has been changed
            Object changedPreference = preferencesMap.get(key);
            // and if it's an instance of EditTextPreference class, update its summary
            //if (preferencesMap.get(key) instanceof EditTextPreference) {
            if (key.equals(getString(R.string.pref_sorting_key))) {
                ListPreference listPreference = (ListPreference)findPreference(key);
                setListPreferenceSummary(listPreference);
            }
            else if (key.equals("others...")) {

            }
        }

        private void updateEditTextPreferenceSummary(EditTextPreference preference) {
            preference.setSummary(preference.getText());
        }

        private void setListPreferenceSummary(ListPreference preference) {
            int prefIndex = preference.findIndexOfValue(preference.getValue());
            if (prefIndex >= 0) {
                preference.setSummary(preference.getEntries()[prefIndex]);
            }
        }

    }

}