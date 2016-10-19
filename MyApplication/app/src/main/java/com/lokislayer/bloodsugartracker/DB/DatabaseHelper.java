package com.lokislayer.bloodsugartracker.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lokislayer.bloodsugartracker.Model.BloodSugarModel;
import com.lokislayer.bloodsugartracker.Model.ReadingList;

import java.util.ArrayList;
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
    private ReadingList result;

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

        if (!isEmpty()) {
            SQLiteDatabase db = getReadableDatabase();
            String sql = "SELECT * FROM " + TABLE_NAME;
            Cursor c = db.rawQuery(sql, null);

            if (c.getCount() == 0) {
                return null;
            }

            if (c.getCount() == result.getNumReading())
                return result.getAllResults();
            else
            {
                c.moveToFirst();
                do {
                    String date = c.getString(c.getColumnIndex(DATE_TESTED));
                    String time = c.getString(c.getColumnIndex(TIME_TESTED));
                    int amt = c.getInt(c.getColumnIndex(AMOUNT));

                    model = new BloodSugarModel();
                    model.setAmount(amt);
                    model.setDateTested(date);
                    model.setTimeTested(time);
                    results.add(model);
                } while (c.moveToNext());
                c.close();
            }
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
        result.addReading(model);

        db.insert(TABLE_NAME,null,values);
        db.close();

        return true;
    }

    public void purgeDB()
    {
        SQLiteDatabase db = getWritableDatabase();
        onUpgrade(db,DB_VERSION,DB_VERSION);

        // Since I am keeping List<BloodSugarModel> synced internally
        result.resetReading();
    }

    public int getMaxAmount() { return result.getMax(); }
    public int getMinAmount() { return result.getMin(); }
    public int getAvgAmount() { return result.getAvg(); }
    public int getTotalBloodSugarAmount() { return result.getTotal(); }
    public int getResultCount() { return result.getNumReading(); }
}
