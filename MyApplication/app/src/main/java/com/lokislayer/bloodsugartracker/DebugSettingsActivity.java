package com.lokislayer.bloodsugartracker;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.widget.Toast;

import com.lokislayer.bloodsugartracker.DB.DatabaseHelper;

/**
 * Created by barak on 7/30/2017.
 */

public class DebugSettingsActivity extends PreferenceActivity implements DebugSettingPreferenceFragment.CheckRowCountListener
{
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content,new DebugSettingPreferenceFragment())
                .commit();
    }


    @Override
    public void checkRowCount()
    {
        final DatabaseHelper db = DatabaseHelper.getInstance(this);
        final int rowCount = db.getResultCount();

        Toast.makeText(this, "Number of Rows in DB = " + String.valueOf(rowCount),Toast.LENGTH_SHORT).show();

    }
}
