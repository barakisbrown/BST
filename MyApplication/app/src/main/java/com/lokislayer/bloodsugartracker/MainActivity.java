package com.lokislayer.bloodsugartracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lokislayer.bloodsugartracker.DB.DatabaseHelper;
import com.lokislayer.bloodsugartracker.Fragments.DatePickerFragment;
import com.lokislayer.bloodsugartracker.Fragments.TimePickerFragment;
import com.lokislayer.bloodsugartracker.Model.BloodSugarModel;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener,DatePickerFragment.DataReturnedListener, TimePickerFragment.TimeEnteredListener
{
    private String dateStr,timeStr;
    private TextView minText;
    private TextView maxText;
    private TextView avgText;
    private EditText amountTested;
    private Button dateTested;
    private Button timeTested;
    private Button submitResults;
    private DatabaseHelper db;
    private BloodSugarModel model;
    private int amountBlood;

    // NEED THESE TWO FRAGMENTS SO THAT I CAN ACCESS THEM THROUGHOUT THE APP
    DatePickerFragment dateFragment;
    TimePickerFragment timeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // INITIALIZE DB FIELD
        db = DatabaseHelper.getInstance(this);
        LoadOrUpdateValues();

        // BUTTONS THAT NEED BE INITIATED AND BOUND TO THE CLICK LISTENER
        dateTested = (Button)findViewById(R.id.dateEnteredBtn);
        dateTested.setOnClickListener(this);

        timeTested = (Button)findViewById(R.id.timeEnteredBtn);
        timeTested.setOnClickListener(this);

        submitResults = (Button)findViewById(R.id.submitResult);
        submitResults.setOnClickListener(this);

        // INITIALIZE FRAGMENTS
        dateFragment = new DatePickerFragment();
        timeFragment = new TimePickerFragment();

        // MIGHT REMOVE
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        //

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //I NEED TO CHECK IF THE DB WAS PURGED BECAUSE ALL VALUES WILL BE WIPED CLEAN
        if (id == R.id.action_settings) {
            Intent it = new Intent(this,SettingsActivity.class);
            startActivityForResult(it,1);
        }
        else if (id == R.id.debug_settings)
        {
            Intent it = new Intent(this,DebugSettingsActivity.class);
            startActivityForResult(it,2);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGetDataEntered(String str)
    {
        dateStr = str;
        // DEBUG
        Log.d("Date[Main Activity] =",dateStr);
        //
        dateTested = (Button)findViewById(R.id.dateEnteredBtn);
        dateTested.setText(str);
    }

    @Override
    public void onClick(View v)
    {
        minText = (TextView)findViewById(R.id.minValue);
        maxText = (TextView)findViewById(R.id.maxValue);
        avgText = (TextView)findViewById(R.id.avgValue);

        if (v.getId() == R.id.submitResult)
        {
            // VALIDATE FIRST
            if (validation()) {
                amountTested = (EditText) findViewById(R.id.bloodAmount);
                amountBlood = Integer.parseInt(amountTested.getText().toString());
                Log.d("Submit Results", "");
                Log.d("Blood AMount(int)", String.valueOf(amountBlood));
                Log.d("Date Tested : ", dateStr);
                Log.d("Time Tested : ", timeStr);
                InsertTestResults();

                // UPDATE THE UI SINCE INSERT HAS BEEN DONE
                dateTested = (Button) findViewById(R.id.dateEnteredBtn);
                timeTested = (Button) findViewById(R.id.timeEnteredBtn);
                amountTested.setText("");
                dateTested.setText(R.string.date_btn_text);
                timeTested.setText(R.string.time_btn_text);
                dateFragment.setDefault(true);
            }
        }
        else if (v.getId() == R.id.dateEnteredBtn) {
            dateFragment.show(getSupportFragmentManager(), "datePicker");
        }
        else if (v.getId() == R.id.timeEnteredBtn)
        {
            timeFragment.show(getSupportFragmentManager(),"timePicker");
        }
    }

    @Override
    public void onTimeEntered(String str)
    {
        timeStr = str;
        // DEBUG
        Log.d("Time[Main Activity] =",timeStr);
        //
        timeTested = (Button)findViewById(R.id.timeEnteredBtn);
        timeTested.setText(str);
    }

    private void InsertTestResults()
    {
        model = new BloodSugarModel();
        model.setAmount(amountBlood);
        model.setDateTested(dateStr);
        model.setTimeTested(timeStr);
        db.addTestResults(model);
        LoadOrUpdateValues();
    }

    private void LoadOrUpdateValues()
    {
        // NOTE: Will be set to the actual values when the app launches
        minText = (TextView) findViewById(R.id.minValue);
        maxText = (TextView) findViewById(R.id.maxValue);
        avgText = (TextView) findViewById(R.id.avgValue);
        // display values
        minText.setText(String.valueOf(db.getMinAmount()));
        avgText.setText(String.valueOf(db.getAvgAmount()));
        maxText.setText(String.valueOf(db.getMaxAmount()));

    }

    public boolean validation()
    {
        StringBuilder build = new StringBuilder();
        amountTested = (EditText)findViewById(R.id.bloodAmount);

        if (amountTested.getText().toString().equals("")){
            build.append("Amount Tested is REQUIRED \n");
        }
        if (dateStr == null) {
            build.append("DATE IS REQUIRED \n");
        }
        if (timeStr == null){
            build.append("TIME IS REQUIRED \n");
        }

        if (!build.toString().isEmpty())
        {
            Context ctx = getApplicationContext();
            String errMsg = build.toString();
            int duration = Toast.LENGTH_LONG;
            Toast.makeText(ctx, errMsg, duration).show();
            return false;
        }
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 1)
        {
            if ((resultCode == RESULT_OK)&&(data != null)) {
                db.purgeDB();
            }
            LoadOrUpdateValues();
        } else if (requestCode == 2)
        {
            LoadOrUpdateValues();
        }
    }
}
