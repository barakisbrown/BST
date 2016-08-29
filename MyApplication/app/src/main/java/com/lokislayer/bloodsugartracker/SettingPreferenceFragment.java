package com.lokislayer.bloodsugartracker;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

/**
 * Created by Matthew on 8/29/2016.
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
