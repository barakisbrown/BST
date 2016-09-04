package com.lokislayer.bloodsugartracker.Fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Matthew on 9/4/2016.
 * Fragment to show TimePickerFragment to the device
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener
{
    public interface TimeEnteredListener
    {
        void onTimeEntered(String str);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }



    @Override
    public void onTimeSet(TimePicker view, int h, int m)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(h).append(":");
        if (m < 10)
        {
            builder.append("0").append(m);
        }
        else
            builder.append(m);

        // DEBUG
        Log.d("Time is ",builder.toString());
        // DEBUG
        TimeEnteredListener activity = (TimeEnteredListener)getActivity();
        activity.onTimeEntered(builder.toString());
        this.dismiss();
    }
}
