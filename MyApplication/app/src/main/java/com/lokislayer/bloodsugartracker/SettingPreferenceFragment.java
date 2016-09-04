package com.lokislayer.bloodsugartracker;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

/**
 * Created by Matthew on 8/29/2016.
 * This fragment will allow me to purge the database and allow the ui to change the default
 * behavior how the time is displayed.
 */
public class SettingPreferenceFragment extends PreferenceFragment
{
    private static final String KEY = "purgeDB";
    private static final String KEY_1 = "change24time";
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        /**
         * NOTE: The following anonymous inner class will purge the existing data.
         */
        final Preference purgePreference = findPreference(KEY);
        purgePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                if (preference.getKey().equals(KEY))
                {
                    Toast.makeText(getActivity(),"HELLO WORLD",Toast.LENGTH_LONG).show();

                }
                return false;
            }
        });

        /**
         * The following code segment will allow the program to display time in normal time such
         * as 4:21 PM or it will revert it back to military time.
         */
        final CheckBoxPreference timePreference = (CheckBoxPreference)findPreference(KEY_1);
        timePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                if(preference.getKey().equals(KEY_1))
                {
                    boolean toggle = timePreference.isChecked();
                    if (!toggle)
                    {
                        Toast.makeText(getActivity(),"Time Unchecked",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"Time Checked",Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });

    }
}
