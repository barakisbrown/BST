package com.lokislayer.bloodsugartracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.widget.Toast;

import com.lokislayer.bloodsugartracker.DB.DatabaseHelper;

/**
 * Created by Matthew on 8/29/2016.
 * This fragment will allow me to purge the database and allow the ui to change the default
 * behavior how the time is displayed.
 */
public class SettingPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener
{
    private static final String KEY = "purgeDB";
    private static final String KEY_1 = "change24time";
    private static final String KEY_2 = "aboutBST";

    private Activity activity;

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
        editor.apply();
        return true;

    }

    @Override
    public boolean onPreferenceClick(Preference preference)
    {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

        if (preference.getKey().equals(KEY))
        {
            // Instantiate a AlertBuilder to let the user know of this action
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setMessage(R.string.purge_db_warning);
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    ((PurgeNotificationListener) activity).isPurged(true);
                }
            });

            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if (preference.getKey().equals(KEY_2))
        {
            Toast.makeText(getActivity(),"VERSION 1.0",Toast.LENGTH_LONG).show();
        }

        return true;
    }

    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        this.activity = activity;
    }

    public interface PurgeNotificationListener
    {
        public void isPurged(boolean value);
    }
}
