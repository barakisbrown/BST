package com.lokislayer.bloodsugartracker.Model;

/**
 * Created by Matthew on 9/4/2016.
 * The underlying model for the application which stores all information about a blood sugar
 * reading
 */
public class BloodSugarModel {
    private String _dateTested;
    private String _timeTested;
    private int _amount;

    public String getDateTested() {
        return _dateTested;
    }

    public String getTimeTested() {
        return _timeTested;
    }

    public int getAmount() {
        return _amount;
    }

    public void setDateTested(String dateTested)
    {
        _dateTested = dateTested;
    }

    public void setTimeTested(String timeTested)
    {
        _timeTested = timeTested;
    }

    public void setAmount(int amount)
    {
        _amount = amount;
    }
}
