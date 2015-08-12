package com.example.dominik.gulagfinalcountdown;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;


public class Countdown extends Activity {


    SharedPreferences userPreferences;
    int min, hour, day, month, year, k;
    long time, finaly;
    TextView textView;
    SharedPreferences.Editor userPreferencesEditor;
    private MediaPlayer mySound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_countdown);
        userPreferences = getSharedPreferences("Data", 0);
        textView = (TextView) findViewById(R.id.textView3);
        k = 1;

        if(userPreferences.getInt("day", 0)!=0){
            timeLeft();

            if(finaly<=0){
                Context context = getApplicationContext();
                Toast toast = Toast.makeText(context,R.string.toast, Toast.LENGTH_SHORT);
                toast.show();
                Intent intent = new Intent(this,setYourTime.class);
                startActivity(intent);
                finish();
            }
            else{
                final CounterClass timer = new CounterClass(finaly, 1000);
                timer.start();
            }
        }else{
            textView.setText("KONIEC!1");
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_countdown, menu);
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

    public void timeLeft() {
        min = userPreferences.getInt("minute", 0);
        hour = userPreferences.getInt("hour", 0);
        day = userPreferences.getInt("day", 0);
        month = userPreferences.getInt("month", 0);
        year = userPreferences.getInt("year", 0);

        Calendar CurrentDateTime = Calendar.getInstance();
        int csec, cmin, chour, cday, cmonth, cyear;

        cmin = CurrentDateTime.get(Calendar.MINUTE);
        chour = CurrentDateTime.get(Calendar.HOUR_OF_DAY);
        cday = CurrentDateTime.get(Calendar.DAY_OF_MONTH);
        cmonth = CurrentDateTime.get(Calendar.MONTH);
        cyear = CurrentDateTime.get(Calendar.YEAR);
        csec = CurrentDateTime.get(Calendar.SECOND);


        int pom = 0; //liczy ile bylo lat przestepnych aby dodac odpowiednia ilosc dni
        int dayss = 0;

        //liczenie lat przestepnych
        for (int i = cyear; i <= year; i++) {
            if ((i % 4) == 0) pom++;
        }

        int dayofmonth[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        if ((year - cyear) == 0) {
            for (int i = cmonth; i <= month; i++) {
                dayss = dayss + dayofmonth[i];
            }
            dayss = dayss - cday - (dayofmonth[month] - day) + pom;
        } else {
            for (int i = cmonth; i <= 11; i++) {
                dayss = dayss + dayofmonth[i];
            }
            dayss = dayss - cday;
            for (int i = 0; i <= month; i++) {
                dayss = dayss + dayofmonth[i];
            }
            dayss = dayss - (dayofmonth[month] - day) + pom;
        }


        long milliseconds = TimeUnit.DAYS.toMillis((long) dayss); //dni na milisekundy

        //uwzglednienie godziny, minut i sekund
        int timer = (hour - chour) * 60 + (min - cmin);

        time = TimeUnit.MINUTES.toMillis((long) timer) - TimeUnit.SECONDS.toMillis((long) csec);

        finaly = milliseconds + time;

    }

    public void changeTime(View view) {

        Intent intent = new Intent(this, setYourTime.class);
        startActivity(intent);
        finish();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void notificate() {
        Intent intent = new Intent(this, Countdown.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);


        Notification n = new Notification.Builder(this)
                .setContentTitle("Work Final Countdown")
                        .setContentText("KONIEC!")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentIntent(pIntent)
                        .setAutoCancel(true).build();


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, n);
        final Vibrator vibe = (Vibrator) Countdown.this.getSystemService(Context.VIBRATOR_SERVICE);
        mySound = MediaPlayer.create(this, R.raw.song);
        mySound.start();
        vibe.vibrate(2000);
    }


    public class CounterClass extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public CounterClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

            long millis = millisUntilFinished;
            String hms = String.format("%02d godzin %02d minut %02d sekund", TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
            textView.setText(hms);
            if (k == 0) {
                textView.setTextColor(Color.RED);
                k = 1;
            } else {
                textView.setTextColor(Color.BLACK);
                k = 0;
            }
        }

        @Override
        public void onFinish() {

            textView.setText(R.string.end);

            notificate();

            userPreferencesEditor = userPreferences.edit();

            userPreferencesEditor.putInt("hour",0);
            userPreferencesEditor.putInt("minute", 0);

            userPreferencesEditor.putInt("year", 0);
            userPreferencesEditor.putInt("month", 0);
            userPreferencesEditor.putInt("day", 0);

            userPreferencesEditor.commit();




        }


    }


}
