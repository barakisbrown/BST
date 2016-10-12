package com.lokislayer.bloodsugartracker.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lokislayer.bloodsugartracker.Model.BloodSugarModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by Matthew on 9/4/2016.
 * Database Layer for my application that connects to the SQLLite DB
 */
public class DatabaseHelper extends SQLiteOpenHelper
{
    private static final String DB_NAME = "BSResultsDB";
    private static final String TABLE_NAME = "TestResults";
    private static final String ID = "id";
    private static final String DATE_TESTED = "date_tested";
    private static final String TIME_TESTED = "time_tested";
    private static final String AMOUNT = "amount";

    private static final int DB_VERSION=1;

    private BloodSugarModel model;
    private List<BloodSugarModel> results = new ArrayList<>();

    private int totalBloodSugarAmount;
    private int minBloodSugarAmount;
    private int maxBloodSugarAmount;
    private int avgBloodSugarAmount;

    private static DatabaseHelper sInstance;

    public static synchronized DatabaseHelper getInstance(Context context)
    {
        // I WILL BE USED THE APPLICATION CONTEXT SO THAT I DO NOT
        // LEAK the Activity Context
        if (sInstance == null)
        {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private DatabaseHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
        model = new BloodSugarModel();
        totalBloodSugarAmount = 0;
        minBloodSugarAmount = 0;
        maxBloodSugarAmount = 0;
        avgBloodSugarAmount = 0;
        results = getAllResults();
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String sql = "CREATE TABLE " +TABLE_NAME
                +"(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DATE_TESTED + " VARCHAR, "
                + TIME_TESTED + " VARCHAR, "
                + AMOUNT + " INTEGER);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if (oldVersion == newVersion)
        {
            String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
            db.execSQL(sql);
            onCreate(db);
        }
    }

    private boolean isEmpty()
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE TYPE = ? AND NAME = ?",
                new String[] {"table",TABLE_NAME});
        if (!cursor.moveToFirst())
        {
            cursor.close();
            return false;
        }
        int count = cursor.getCount();
        cursor.close();
        return count > 0;

    }

    public List<BloodSugarModel> getAllResults()
    {
        List<BloodSugarModel> results = new ArrayList<>();

        if (!isEmpty())
        {
            SQLiteDatabase db = getReadableDatabase();
            String sql = "SELECT * FROM " + TABLE_NAME;
            Cursor c = db.rawQuery(sql,null);

            if (c.getCount() == 0) { return null; }
            c.moveToFirst();
            do
            {
                String date = c.getString(c.getColumnIndex(DATE_TESTED));
                String time = c.getString(c.getColumnIndex(TIME_TESTED));
                int amt = c.getInt(c.getColumnIndex(AMOUNT));

                totalBloodSugarAmount += amt;
                if (minBloodSugarAmount == 0 && (maxBloodSugarAmount == 0))
                {
                    minBloodSugarAmount = maxBloodSugarAmount = 0;
                }
                if (amt > maxBloodSugarAmount)
                    maxBloodSugarAmount = amt;
                if (amt < minBloodSugarAmount)
                    minBloodSugarAmount = amt;

                model = new BloodSugarModel();
                model.setAmount(amt);
                model.setDateTested(date);
                model.setTimeTested(time);
                results.add(model);
            }while(c.moveToNext());
            avgBloodSugarAmount = totalBloodSugarAmount / results.size();
            c.close();
        }
        return results;
    }

    public boolean addTestResults(BloodSugarModel model)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DATE_TESTED, model.getTimeTested());
        values.put(TIME_TESTED, model.getTimeTested());
        values.put(AMOUNT, model.getAmount());
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

    public void purgeDB()
    {
        SQLiteDatabase db = getWritableDatabase();
        onUpgrade(db,DB_VERSION,DB_VERSION);

        // Since I am keeping List<BloodSugarModel> synced internally
        results.clear();
        maxBloodSugarAmount = minBloodSugarAmount = totalBloodSugarAmount = avgBloodSugarAmount = 0;
    }

    public int getMaxAmount() { return maxBloodSugarAmount; }
    public int getMinAmount() { return minBloodSugarAmount; }
    public int getAvgAmount() { return avgBloodSugarAmount; }
    public int getTotalBloodSugarAmount() { return totalBloodSugarAmount; }
    public int getResultCount() { return results.size(); }
}
