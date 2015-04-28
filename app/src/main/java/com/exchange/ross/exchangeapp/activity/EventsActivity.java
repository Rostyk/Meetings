package com.exchange.ross.exchangeapp.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.exchange.ross.exchangeapp.R;
import com.exchange.ross.exchangeapp.Utils.ApplicationContextProvider;
import com.exchange.ross.exchangeapp.Utils.DateUtils;
import com.exchange.ross.exchangeapp.Utils.EventsManager;
import com.exchange.ross.exchangeapp.Utils.PurchaseManager;
import com.exchange.ross.exchangeapp.Utils.Typefaces;
import com.exchange.ross.exchangeapp.core.service.TimeService;
import com.exchange.ross.exchangeapp.core.model.MyPageAdapter;
import com.exchange.ross.exchangeapp.activity.Views.EventsFragment;

import java.util.Date;

public class EventsActivity extends ActionBarActivity implements EventsFragment.OnFragmentInteractionListener  {

    private Activity activity;
    private TextView topDateTextView;
    private TextView topMonthTextView;
    private TextView topDayOfWeekTextView;
    private MyPageAdapter pagerAdapter;
    private ViewPager mViewPager;
    private Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        this.activity = this;

        ApplicationContextProvider.setActivity(this);
        pagerAdapter = new MyPageAdapter(
                                getSupportFragmentManager());

        pagerAdapter.setActivity(this);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(8);

        Typeface light = Typefaces.get(getApplicationContext(), "robotolight");
        Typeface thin =  Typefaces.get(getApplicationContext(), "robotothin");

        //Top date
        topDateTextView = (TextView)findViewById(R.id.topDateTextView);
        topDateTextView.setTypeface(thin);

        //Month and year
        topMonthTextView = (TextView)findViewById(R.id.topMonthTextView);
        topMonthTextView.setTypeface(light);

        //Day of week
        topDayOfWeekTextView = (TextView)findViewById(R.id.topDayOfWeekTextView);
        topDayOfWeekTextView.setTypeface(thin);

        //mViewPager.setOffscreenPageLimit(100);
        mViewPager.setOnPageChangeListener(pagerAdapter);
        mViewPager.setAdapter(pagerAdapter);

        View settingsVew = findViewById(R.id.settingsButtonView);
        settingsVew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsActivityIntent = new Intent(EventsActivity.this, SettingsActivity.class);
                EventsActivity.this.startActivity(settingsActivityIntent);
            }
        });

        PurchaseManager manager = PurchaseManager.sharedManager();
        manager.init(getApplicationContext());


        ImageButton syncButton = (ImageButton)findViewById(R.id.syncButton);
        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast();

                PurchaseManager manager = PurchaseManager.sharedManager();
                manager.buy(activity);

                /*
                Intent serviceIntent = new Intent(activity, TimeService.class);
                serviceIntent.putExtra("ForceSync", true);
                activity.startService(serviceIntent);*/
            }
        });

        registerServiceBroadcast();
        //Start time service
        serviceIntent = new Intent(this, TimeService.class);
        startService(serviceIntent);
    }

    public void showToast() {
        Toast toast = Toast.makeText(this,"Events will be synced within a minute", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void registerServiceBroadcast() {
        IntentFilter filter = new IntentFilter(EventsManager.KILL_SERVICE);
        filter.addAction(EventsManager.FORCE_SYNC_EVENTS);
        filter.addAction(EventsManager.START_SERVICE);
        LocalBroadcastManager.getInstance(this.activity.getApplicationContext()).registerReceiver(serviceStateReceiver,filter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data)
    {
        PurchaseManager manager = PurchaseManager.sharedManager();
        if (!manager.mHelper.handleActivityResult(requestCode,
                resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void displayDate(int daySinceNow) {
        Date date = DateUtils.dateSinceToday(daySinceNow);

        String day = DateUtils.dayOfDate(date);
        topDateTextView.setText(day);

        String monthYear = DateUtils.monthYearOfDate(date);
        topMonthTextView.setText(monthYear);

        String dayOfWeek = DateUtils.dayOfWeekOfDate(date);
        topDayOfWeekTextView.setText(dayOfWeek);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_events, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this.activity.getApplicationContext()).unregisterReceiver(serviceStateReceiver);
        PurchaseManager manager = PurchaseManager.sharedManager();
        if (manager.mHelper != null) manager.mHelper.dispose();
          manager.mHelper = null;
        stopService(serviceIntent);
    }

    public void onBackPressed() {
        Intent i = new Intent();
        i.setAction(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        this.startActivity(i);
    }

    private BroadcastReceiver serviceStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(EventsManager.KILL_SERVICE)) {
                stopService(serviceIntent);
            }
            if(intent.getAction().equals(EventsManager.START_SERVICE)) {
                serviceIntent = new Intent(activity, TimeService.class);
                startService(serviceIntent);
            }
            if(intent.getAction().equals(EventsManager.FORCE_SYNC_EVENTS)) {
                Intent serviceIntent = new Intent(activity, TimeService.class);
                String account = intent.getStringExtra("Account");
                serviceIntent.putExtra("ForceSync", true);
                serviceIntent.putExtra("Account", account);
                activity.startService(serviceIntent);
            }
        }
    };

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onPause() {
        super.onPause();

    }
}
