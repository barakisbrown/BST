package com.lokislayer.bloodsugartracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
public class SettingPreferenceFragment extends PreferenceFragment
{
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
                    // Instantiate a AlertBuilder to let the user know of this action
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    builder.setMessage(R.string.purge_db_warning);
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            DatabaseHelper db = DatabaseHelper.getInstance(getActivity());
                            db.purgeDB();
                            NoticeDialogListener activity = (NoticeDialogListener)getActivity();
                            activity.onPurge(true);
                        }
                    });

                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            NoticeDialogListener activity = (NoticeDialogListener)getActivity();
                            activity.onPurge(false);
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
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

        /**
         * Code segment is for showing a simple popup that will show the version via a toast !FOR NOW!
        */
        final Preference aboutPreference = findPreference(KEY_2);
        aboutPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                if (preference.getKey().equals(KEY_2))
                {
                    Toast.makeText(getActivity(),"VERSION 1.0",Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });
    }
}
