package com.lokislayer.bloodsugartracker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import com.lokislayer.bloodsugartracker.DB.DatabaseHelper;

/**
 * Created by Matthew on 8/29/2016.
 * This fragment will allow me to purge the database and allow the ui to change the default
 * behavior how the time is displayed.
 */
public class SettingPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener
{
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue)
    {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        if (newValue instanceof Boolean)
        {
            Boolean value = (Boolean)newValue;
            if (value)
            {
                editor.putBoolean("24HRTIME",true);
            }
            else
            {
                editor.putBoolean("24HRTIME",false);
            }
        }
        return true;

    }

    @Override
    public boolean onPreferenceClick(Preference preference)
    {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        if (preference.getKey().equals(KEY))
        {
            editor.putBoolean("PURGE",true);
        }
        else if (preference.getKey().equals(KEY_2))
        {
            editor.putBoolean("ABOUT",true);
        }

        return true;
    }

    public interface NoticeDialogListener {
        public void onPurge(boolean isPurged);
    }

    private static final String KEY = "purgeDB";
    private static final String KEY_1 = "change24time";
    private static final String KEY_2 = "aboutBST";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        final CheckBoxPreference cboxPrf = (CheckBoxPreference)getPreferenceManager().findPreference(KEY_1);
        cboxPrf.setOnPreferenceChangeListener(this);

        final Preference purge = findPreference(KEY);
        purge.setOnPreferenceClickListener(this);

        final Preference about = findPreference(KEY_2);
        about.setOnPreferenceClickListener(this);

    }
}
