package com.lokislayer.bloodsugartracker.DB;

import android.content.ContentValues;
import android.database.Cursor;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lokislayer.bloodsugartracker.Model.BloodSugarModel;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Matthew on 9/4/2016.
 * Database Layer for my application that connects to the SQLLite DB
 */
public class DatabaseHelper extends SQLiteOpenHelper
{
    public static final String DB_NAME = "BSResultsDB";
    public static final String TABLE_NAME = "TestResults";
    public static final String COLUMN_ID = "id";
    public static final int DB_VERSION=1;

    private BloodSugarModel model;
    private List<BloodSugarModel> results = new ArrayList<>();
    private int totalBloodSugarAmount;
    private int minBloodSugarAmount;
    private int maxBloodSugarAmount;
    private int avgBloodSugarAmount;

    public DatabaseHelper(Context context)
    {
        super(context,DB_NAME,null,DB_VERSION);
        model = new BloodSugarModel();
        totalBloodSugarAmount = 0;
        minBloodSugarAmount = 0;
        maxBloodSugarAmount = 0;
        avgBloodSugarAmount = 0;
        loadResults();
    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String sql = "CREATE TABLE " +TABLE_NAME
                +"(" + BloodSugarModel.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +BloodSugarModel.DATE_TESTED + " VARCHAR, "
                +BloodSugarModel.TIME_TESTED + " VARCHAR, "
                +BloodSugarModel.AMOUNT + " INTEGER);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int o, int n)
    {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }

    private boolean doesDbExist()
    {
        SQLiteDatabase db = getReadableDatabase();
        if (db == null)
        {
            db.close();
            return false;
        }
        else
        {
            db.close();
            return true;
        }

    }

    private void loadResults()
    {
        if (doesDbExist())
        {
            SQLiteDatabase db = getReadableDatabase();
            String sql = "SELECT * FROM " + TABLE_NAME;
            Cursor c = db.rawQuery(sql,null);

            if (c.getCount() == 0) { return; }
            c.moveToFirst();
            do
            {
                String date = c.getString(c.getColumnIndex(BloodSugarModel.DATE_TESTED));
                String time = c.getString(c.getColumnIndex(BloodSugarModel.TIME_TESTED));
                int amt = c.getInt(c.getColumnIndex(BloodSugarModel.AMOUNT));
                totalBloodSugarAmount += amt;
                if (minBloodSugarAmount == 0 && (maxBloodSugarAmount == 0))
                {
                    minBloodSugarAmount = maxBloodSugarAmount = amt;
                }
                if (amt > maxBloodSugarAmount)
                    maxBloodSugarAmount = amt;
                if (amt < minBloodSugarAmount)
                    minBloodSugarAmount = amt;
                BloodSugarModel model = new BloodSugarModel();
                model.setAmount(amt);
                model.setDateTested(date);
                model.setTimeTested(time);
                results.add(model);
            }while(c.moveToNext());
            avgBloodSugarAmount = totalBloodSugarAmount / results.size();
            db.close();
        }
    }

    public boolean addTestResults(BloodSugarModel model)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(BloodSugarModel.DATE_TESTED,model.getTimeTested().toString());
        values.put(BloodSugarModel.TIME_TESTED,model.getTimeTested().toString());
        values.put(BloodSugarModel.AMOUNT, model.getAmount());
        results.add(model);

        db.insert(TABLE_NAME,null,values);
        db.close();
        int value = model.getAmount();

        totalBloodSugarAmount += value;
        avgBloodSugarAmount = totalBloodSugarAmount / results.size();
        if (results.size() == 1)
        {
            maxBloodSugarAmount = minBloodSugarAmount = value;
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

    public int getMaxAmount()
    {
        return maxBloodSugarAmount;
    }

    public int getMinAmount()
    {
        return minBloodSugarAmount;
    }

    public int getAvgAmount() { return avgBloodSugarAmount; }

    public int getTotalBloodSugarAmount() { return totalBloodSugarAmount; }

    public List<BloodSugarModel> getAllResults()
    {
        return results;
    }

}
