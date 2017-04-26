package com.example.android.movies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

    }


    public static class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.general_pref);
            Preference sortby = findPreference(getString(R.string.sort_key));
            BindValueToSummary(sortby);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {

            String PreferenceValue = value.toString();
            if (preference instanceof ListPreference){
                ListPreference mylistPreference = (ListPreference) preference;
                int PrefIndex = mylistPreference.findIndexOfValue(PreferenceValue);
                if (PrefIndex>=0){
                    CharSequence [] mylistLabel = mylistPreference.getEntries();
                    preference.setSummary(mylistLabel[PrefIndex]);

                }
            }

            return true;
        }



        public void BindValueToSummary(Preference preference) {

            preference.setOnPreferenceChangeListener(this);
            SharedPreferences PossPreferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceValue = PossPreferences.getString(preference.getKey(),null);
            onPreferenceChange(preference, preferenceValue);
        }

    }
}
