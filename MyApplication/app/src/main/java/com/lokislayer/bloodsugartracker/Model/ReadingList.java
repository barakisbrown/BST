package com.lokislayer.bloodsugartracker.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Instead of creating a List<BSM> inside the DataBaseHelper Class I created
 * an object that will do all the heavy lifting itself.
 * Created by barakis on 10/19/16.
 */

public class ReadingList
{
    private static ReadingList mInstance = null;
    private List<BloodSugarModel> results = null;

    private int totalBloodSugarAmount;
    private int minBloodSugarAmount;
    private int maxBloodSugarAmount;
    private int avgBloodSugarAmount;


    private ReadingList()
    {
        results = new ArrayList<>();

    }

    public static synchronized ReadingList getInstance()
    {
        if (mInstance == null) {
            mInstance = new ReadingList();
        }
        return mInstance;
    }

    public boolean addReading(BloodSugarModel model)
    {
        if (model == null) return false;

        int size = results.size();
        int value = model.getAmount();

        results.add(model);

        if (size + 1 == results.size())
        {
            totalBloodSugarAmount += value;
            avgBloodSugarAmount = totalBloodSugarAmount / size + 1;
            if (results.size() == 1)
            {
                minBloodSugarAmount = maxBloodSugarAmount = value;
            }
            else
            {
                if (value > maxBloodSugarAmount)
                    maxBloodSugarAmount = value;
                if (value < minBloodSugarAmount)
                    minBloodSugarAmount = value;
            }
            return true;
        }

        return  false;
    }

    public List<BloodSugarModel> getAllResults() { return results; }

    public boolean resetReading()
    {
        results.clear();
        avgBloodSugarAmount = minBloodSugarAmount = maxBloodSugarAmount = 0;
        totalBloodSugarAmount = 0;

        return results.size() == 0;
    }

    public int getMax() { return maxBloodSugarAmount; }
    public int getMin() { return minBloodSugarAmount; }
    public int getAvg() { return avgBloodSugarAmount; }
    public int getTotal() { return totalBloodSugarAmount; }
    public int getNumReading() { return results.size(); }
}
