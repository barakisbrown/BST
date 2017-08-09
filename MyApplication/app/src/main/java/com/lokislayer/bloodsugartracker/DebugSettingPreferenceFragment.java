package com.lokislayer.bloodsugartracker;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;

/**
 * Created by barak on 7/30/2017.
 */

public class DebugSettingPreferenceFragment extends PreferenceFragment
        implements OnPreferenceClickListener

{
    private static final String KEY = "rowCountDB";
    private Activity activity;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.debug_settings);

        // SET CLICK LISTENER TO THE
        final Preference rowCountPref = findPreference(KEY);
        rowCountPref.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

        if (preference.getKey().equals(KEY))
        {
            ((CheckRowCountListener)activity).checkRowCount();
        }


        return true;
    }

    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        this.activity = activity;
    }

    public interface CheckRowCountListener
    {
        public void checkRowCount();
    }
}
