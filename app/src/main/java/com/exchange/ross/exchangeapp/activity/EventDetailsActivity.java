package com.exchange.ross.exchangeapp.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.exchange.ross.exchangeapp.APIs.WebService;
import com.exchange.ross.exchangeapp.R;
import com.exchange.ross.exchangeapp.Utils.ApplicationContextProvider;
import com.exchange.ross.exchangeapp.Utils.DateUtils;
import com.exchange.ross.exchangeapp.Utils.Typefaces;
import com.exchange.ross.exchangeapp.core.entities.Event;
import com.exchange.ross.exchangeapp.Utils.EventsManager;
import com.exchange.ross.exchangeapp.db.AccountsProxy;
import com.exchange.ross.exchangeapp.db.EventsProxy;
import com.exchange.ross.exchangeapp.db.ServiceType;

import java.util.List;

public class EventDetailsActivity extends ActionBarActivity {

    private AccountsProxy accountsProxy;
    private List<WebService> accountList;
    private int number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        Intent intent = getIntent();
        number = intent.getIntExtra("number", 0);
        setupDetailsView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_details, menu);
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

    private void setupDetailsView() {
        this.accountsProxy = AccountsProxy.sharedProxy();
        this.accountList = accountsProxy.getAllAccounts();

        String id = EventsManager.sharedManager().getSelectedEventId();
        Event event = EventsProxy.sharedProxy().getEventBuId(id);

        Typeface light = Typefaces.get(getApplicationContext(), "robotolight");
        Typeface thin =  Typefaces.get(getApplicationContext(), "robotothin");

        TextView meetingSubjectTextView = (TextView)findViewById(R.id.meetingSubjectTextView);
        TextView meetingTimeTextView = (TextView)findViewById(R.id.meetingTimeTextView);
        TextView meetingLocationTextView = (TextView)findViewById(R.id.meetingLocationTextView);
        TextView meetingAttendeesTextView = (TextView)findViewById(R.id.meetingAttendeesTextView);
        TextView meetingCalendarTextView = (TextView)findViewById(R.id.meetingCalendarTextView);
        TextView meetingDescriptionTextView = (TextView)findViewById(R.id.meetingDescriptionTextView);
        meetingSubjectTextView.setTypeface(light);
        meetingTimeTextView.setTypeface(thin);
        meetingLocationTextView.setTypeface(thin);
        meetingAttendeesTextView.setTypeface(thin);
        meetingCalendarTextView.setTypeface(thin);
        meetingDescriptionTextView.setTypeface(thin);


        View topView = findViewById(R.id.descriptionTopView);

        int bgResourceId = ApplicationContextProvider.getsContext().getResources().getIdentifier("descriptiontop" + (number%6 + 1), "drawable", ApplicationContextProvider.getsContext().getPackageName());
        topView.setBackgroundResource(bgResourceId);

        int buttonResourceId =  ApplicationContextProvider.getsContext().getResources().getIdentifier("description_button" + (number%6 + 1) + "_off", "drawable", ApplicationContextProvider.getsContext().getPackageName());
        ImageButton muteBtton = (ImageButton)findViewById(R.id.descriptionMuteButton);
        muteBtton.setBackgroundResource(buttonResourceId);

        final ImageButton descriptionMuteButton = (ImageButton)findViewById(R.id.descriptionMuteButton);
        descriptionMuteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Clicked",
                        Toast.LENGTH_LONG).show();
            }
        });
        meetingSubjectTextView.setText(event.getSubject());
        meetingTimeTextView.setText(DateUtils.meetingTimeFromEvent(event));
        meetingLocationTextView.setText(event.getLocation());

        String attendees = "";
        if(event.getRequiredAttendees() != null)
            attendees = event.getRequiredAttendees();
        if(event.getOptionalAttendees() != null)
            if(event.getOptionalAttendees().length() > 1)
              attendees = attendees + "\n" + event.getOptionalAttendees();

        meetingAttendeesTextView.setText(attendees);
        meetingCalendarTextView.setText(event.getCalendarName());
        meetingDescriptionTextView.setText(event.getBody());

        ServiceType type = getServiceType(event);

        Boolean hideBodyView = false;
        if(type == ServiceType.MICROSOFT_EXCHANGE) {
            String bodyText = Html.fromHtml(event.getBody().toString().replaceAll("<!--.+?>", "")).toString();

            bodyText = bodyText.replaceAll("(\\r|\\n)", "");
            meetingDescriptionTextView.setText(bodyText);

            if(bodyText.length() == 0)
                hideBodyView = true;
        }
        else {
            meetingDescriptionTextView.setText(event.getBody());
            if(event.getBody().length() < 2)
                hideBodyView = true;
        }

        if(hideBodyView)
            meetingDescriptionTextView.setVisibility(View.GONE);
    }

    private ServiceType getServiceType(Event event) {
        for(WebService account : accountList) {
            if(event.getAccountName().equalsIgnoreCase(account.getCredentials().getUser())) {
                return account.getServiceType();
            }
        }

        return ServiceType.SERVICE_UNKNOWN;
    }

}
