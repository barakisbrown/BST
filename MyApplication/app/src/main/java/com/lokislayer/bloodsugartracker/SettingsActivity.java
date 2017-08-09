package com.lokislayer.bloodsugartracker;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.widget.Toast;

import com.lokislayer.bloodsugartracker.DB.DatabaseHelper;

/**
 * Activity Class that will create the SettingsFragment that I will use to power my settings.
 * All functionality will be in the settings fragment class.
 */
public class SettingsActivity extends PreferenceActivity implements SettingPreferenceFragment.PurgeNotificationListener
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content,new SettingPreferenceFragment())
                .commit();
    }

    @Override
    public void isPurged(boolean value)
    {
        Log.d("isPurged = ",Boolean.toString(value));
        Bundle bundle = new Bundle();
        bundle.putBoolean("PURGE",value);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        finish();
    }
}
