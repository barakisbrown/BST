package com.lokislayer.bloodsugartracker;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Activity Class that will create the SettingsFragment that I will use to power my settings.
 * All functionality will be in the settings fragment class.
 */
public class SettingsActivity extends PreferenceActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content,new SettingPreferenceFragment())
                .commit();
    }
}
