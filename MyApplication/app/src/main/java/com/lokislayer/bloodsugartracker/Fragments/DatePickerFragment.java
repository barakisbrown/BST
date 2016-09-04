package com.lokislayer.bloodsugartracker.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Matthew on 9/4/2016.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
{
    public interface DataReturnedListener
    {
        void onGetDataEntered(String str);
    }

    private boolean isDefault = true;
    private int year, month, day;
    String str;

    public DatePickerFragment()
    {
        super();
        year = month = day = 0;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        if (isDefault)
        {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year,month,day);
        }else
        {
            return new DatePickerDialog(getActivity(),this,this.year,this.month,this.day);
        }
    }


    @Override
    public void onDateSet(DatePicker view, int y, int m, int d)
    {
        year = y;
        month = m;
        day = d;
        isDefault = false;
        str = String.format(Locale.getDefault(),"%d/%d/%s",y,m,d);
        // DEBUG
        Log.d("OnDateSet Str:",str);
        // END DEDBUG
        DataReturnedListener activity = (DataReturnedListener)getActivity();
        activity.onGetDataEntered(str);
        this.dismiss();
    }

    public void setDefault(boolean newDefault) { isDefault = newDefault; }

}
