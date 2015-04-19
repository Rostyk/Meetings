package com.exchange.ross.exchangeapp.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.exchange.ross.exchangeapp.R;
import com.exchange.ross.exchangeapp.Utils.ApplicationContextProvider;
import com.exchange.ross.exchangeapp.Utils.DateUtils;
import com.exchange.ross.exchangeapp.Utils.Typefaces;
import com.exchange.ross.exchangeapp.core.service.TimeService;
import com.exchange.ross.exchangeapp.core.model.MyPageAdapter;
import com.exchange.ross.exchangeapp.activity.Views.EventsFragment;

import java.util.Date;

public class EventsActivity extends ActionBarActivity implements EventsFragment.OnFragmentInteractionListener  {

    private TextView topDateTextView;
    private TextView topMonthTextView;
    private TextView topDayOfWeekTextView;
    private MyPageAdapter pagerAdapter;
    private ViewPager mViewPager;
    Boolean dontLoadList = false;
    private int positionCurrent = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

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
        //Start time service
        startService(new Intent(this, TimeService.class));
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsActivityIntent = new Intent(EventsActivity.this, SettingsActivity.class);
            EventsActivity.this.startActivity(settingsActivityIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, TimeService.class));
    }

    public void onBackPressed() {
        Intent i = new Intent();
        i.setAction(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        this.startActivity(i);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onPause() {
        super.onPause();

    }
}
