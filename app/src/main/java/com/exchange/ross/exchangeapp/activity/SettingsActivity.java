package com.exchange.ross.exchangeapp.activity;

import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.exchange.ross.exchangeapp.R;
import com.exchange.ross.exchangeapp.Utils.Settings;
import com.exchange.ross.exchangeapp.Utils.Typefaces;

public class SettingsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Typeface thin = Typefaces.get(getApplicationContext(), "robotothin");
        TextView settingsTopTextView = (TextView)findViewById(R.id.settingsTopTextView);
        settingsTopTextView.setTypeface(thin);

        ImageButton backButton = (ImageButton)findViewById(R.id.settingsBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setupSwitchesHandlers();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public void setupSwitchesHandlers() {
         Typeface light = Typefaces.get(getApplicationContext(), "robotolight");

         Switch soundSwitch = (Switch)findViewById(R.id.soundSwitch);
         soundSwitch.setTypeface(light);

         Switch vibrationSwitch = (Switch)findViewById(R.id.vibrationSwitch);
         vibrationSwitch.setTypeface(light);

         Switch statusBusySwitch = (Switch)findViewById(R.id.statusBusySwitch);
         statusBusySwitch.setTypeface(light);

         Switch ignoreAllDaySwitch = (Switch)findViewById(R.id.ignoreAllDaySwitch);
         ignoreAllDaySwitch.setTypeface(light);

         Switch timerSwitch = (Switch)findViewById(R.id.timerSwitch);
         timerSwitch.setTypeface(light);

         Switch listSwitch = (Switch)findViewById(R.id.listSwitch);
         listSwitch.setTypeface(light);

         final Settings settings = Settings.sharedSettings();
         settings.setContext(getApplicationContext());
         soundSwitch.setChecked(settings.getSound());
         soundSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                 settings.setSound(isChecked);
             }
         });

         vibrationSwitch.setChecked(settings.getVibration());
         vibrationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setVibration(isChecked);
            }
         });

        statusBusySwitch.setChecked(settings.getSilentOnStatusBusy());
        statusBusySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setSilentOnStatusBusy(isChecked);
            }
        });

        ignoreAllDaySwitch.setChecked(settings.getIgnoreAllDayEvent());
        ignoreAllDaySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setIgnoreAllDayEvent(isChecked);
            }
        });

        listSwitch.setChecked(settings.getListMeetingsForDay());
        listSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setListMeetingsForDay(isChecked);
            }
        });

        timerSwitch.setChecked(settings.getTimer());
        timerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setTimer(isChecked);
            }
        });




         TextView soundTextView = (TextView)findViewById(R.id.settingSoundTextView);
         soundTextView.setTypeface(light);
         TextView meetingTextView = (TextView)findViewById(R.id.settingMeetingTextView);
         meetingTextView.setTypeface(light);
         TextView notificationTextView = (TextView)findViewById(R.id.settingNotificationTextView);
         notificationTextView.setTypeface(light);

    }
}
