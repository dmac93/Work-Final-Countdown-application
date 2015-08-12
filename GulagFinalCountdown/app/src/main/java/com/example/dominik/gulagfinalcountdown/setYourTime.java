package com.example.dominik.gulagfinalcountdown;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;


public class setYourTime extends Activity {

    SharedPreferences userPreferences;
    SharedPreferences.Editor userPreferencesEditor;
    TimePicker tp;
    DatePicker dp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_set_your_time);

        userPreferences = getSharedPreferences("Data", 0);
        tp = (TimePicker) this.findViewById(R.id.timePicker);
        dp = (DatePicker) this.findViewById(R.id.datePicker);
        tp.setIs24HourView(true);
        clear();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_set_your_time, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void clear(){
        userPreferencesEditor = userPreferences.edit();

        userPreferencesEditor.putInt("hour",0);
        userPreferencesEditor.putInt("minute", 0);

        userPreferencesEditor.putInt("year", 0);
        userPreferencesEditor.putInt("month", 0);
        userPreferencesEditor.putInt("day", 0);

        userPreferencesEditor.commit();
    }

    public void onEnterClick(View view) {

        userPreferencesEditor = userPreferences.edit();

        userPreferencesEditor.putInt("hour",tp.getCurrentHour());
        userPreferencesEditor.putInt("minute",tp.getCurrentMinute());

        userPreferencesEditor.putInt("year",dp.getYear());
        userPreferencesEditor.putInt("month",dp.getMonth());
        userPreferencesEditor.putInt("day", dp.getDayOfMonth());

        userPreferencesEditor.commit();

        Intent intent = new Intent(this, Countdown.class);
        startActivity(intent);
        finish();
    }
}
