package com.example.dominik.gulagfinalcountdown;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


public class WelcomeActivity extends Activity {
    SharedPreferences userPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);

        userPreferences = getSharedPreferences("Data", 0);
        changeFont();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_welcome, menu);
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

    public void changeFont(){
        Typeface myTypeface = Typeface.createFromAsset(getAssets(),"fonts/font.ttf");
        Button ButtonText   = (Button)findViewById(R.id.button);
        TextView textView = (TextView)findViewById(R.id.textView);
        ButtonText.setTypeface(myTypeface);
        textView.setTypeface(myTypeface);
    }

    public void onClick(View view){

        if(userPreferences.getInt("day",0)==0){
            Intent intent = new Intent(this, setYourTime.class);
            startActivity(intent);
            finish();
        }
        else{
            Intent intent = new Intent(this, Countdown.class);
            startActivity(intent);
            finish();
        }
    }
}
