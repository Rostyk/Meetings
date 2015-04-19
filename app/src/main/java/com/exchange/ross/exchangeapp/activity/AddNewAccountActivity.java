package com.exchange.ross.exchangeapp.activity;

import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.exchange.ross.exchangeapp.APIs.GoogleWebService;
import com.exchange.ross.exchangeapp.APIs.operations.OperationCompleted;
import com.exchange.ross.exchangeapp.R;
import com.exchange.ross.exchangeapp.core.entities.Event;
import com.exchange.ross.exchangeapp.db.AccountsProxy;
import com.exchange.ross.exchangeapp.db.EventsProxy;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.CalendarScopes;


import java.util.ArrayList;
import java.util.Collections;


public class AddNewAccountActivity extends ActionBarActivity implements View.OnClickListener {
    private ProgressDialog progress;
    private GoogleWebService service;
    static final int GOOGLE_CHOOSE_ACCOUNT_ACTIVITY = 1;
    private GoogleAccountCredential credential;
    private final int exGetEventsOperation = 11;
    String accountName;
    final HttpTransport transport = AndroidHttp.newCompatibleTransport();
    final com.google.api.client.json.JsonFactory jsonFactory = GsonFactory.getDefaultInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_account);

        setupView();
    }

    public void setupView() {
        TextView singInTextView = (TextView)findViewById(R.id.singInTextView);
        Typeface robotoFaceLight = Typeface.createFromAsset(getAssets(),"fonts/robotolight.ttf");
        singInTextView.setTypeface(robotoFaceLight);


        ImageButton exchangeButton = (ImageButton) findViewById(R.id.exchangeButton);
        exchangeButton.setOnClickListener(this);

        ImageButton googleButtonButton = (ImageButton) findViewById(R.id.googleButton);
        googleButtonButton.setOnClickListener(this);

        ImageButton office365Button = (ImageButton) findViewById(R.id.office365Button);
        office365Button.setOnClickListener(this);
    }

    public void onClick(View view) {
        if(view.getId() == R.id.exchangeButton || view.getId() == R.id.office365Button) {
            Intent exchangeLoginIntent = new Intent(AddNewAccountActivity.this, ExchangeLoginActivity.class);
            AddNewAccountActivity.this.startActivity(exchangeLoginIntent);
        }
        if(view.getId() == R.id.googleButton) {
            googleLogin();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_new_account, menu);
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

    public void googleLogin() {
        // Google Accounts
        credential = GoogleAccountCredential.usingOAuth2(getApplicationContext(), Collections.singleton(CalendarScopes.CALENDAR));
        startActivityForResult(credential.newChooseAccountIntent(), GOOGLE_CHOOSE_ACCOUNT_ACTIVITY);
    }

    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == GOOGLE_CHOOSE_ACCOUNT_ACTIVITY && resultCode == RESULT_OK) {
            accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);

            getEvents();
        }
    }

    public void getEvents() {
        progress = new ProgressDialog(this);
        progress.setTitle("Google Calendar");
        progress.setMessage("Syncing events");
        progress.show();

        service = new GoogleWebService("", accountName, "", "", getApplicationContext(), AddNewAccountActivity.this);
        service.getEvents(new OperationCompleted() {
            @Override
            public void onOperationCompleted(Object result, int id) {
                if(id == exGetEventsOperation) {
                    ArrayList<Event> events = (ArrayList<Event> )result;

                    for (Event event : events) {
                        Log.d("EX", event.getBody());
                        Log.d("EX", event.getSubject());
                        Log.d("EX", event.getStartDate());
                        Log.d("EX", event.getEndDate());
                        Log.d("EX", "----------");
                    }

                    saveEvents(events);
                    saveAccount(service);
                    openEventsActivity();
                }

            }
        }, exGetEventsOperation);
    }

    public void saveAccount(GoogleWebService service) {
        AccountsProxy proxy = AccountsProxy.sharedProxy();
        proxy.addAccount(service);

        progress.dismiss();
    }

    public void saveEvents(ArrayList<com.exchange.ross.exchangeapp.core.entities.Event> events) {
        EventsProxy proxy = EventsProxy.sharedProxy();
        proxy.insertEvents(events);
    }

    public void openEventsActivity() {
        Intent eventsActivityIntent = new Intent(AddNewAccountActivity.this, EventsActivity.class);
        AddNewAccountActivity.this.startActivity(eventsActivityIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        service.terminate();
    }
}
