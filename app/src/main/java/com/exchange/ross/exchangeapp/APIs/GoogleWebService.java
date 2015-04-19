package com.exchange.ross.exchangeapp.APIs;

import android.accounts.Account;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.exchange.ross.exchangeapp.APIs.operations.OperationCompleted;
import com.exchange.ross.exchangeapp.APIs.operations.OperationCredentials;
import com.exchange.ross.exchangeapp.APIs.operations.WebServiceGoogleGetEventsOperation;
import com.exchange.ross.exchangeapp.Utils.ApplicationContextProvider;
import com.exchange.ross.exchangeapp.core.entities.Event;
import com.exchange.ross.exchangeapp.db.EventsProxy;
import com.exchange.ross.exchangeapp.db.ServiceType;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.accounts.GoogleAccountManager;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ross on 3/21/15.
 */
public class GoogleWebService extends WebService {
    private  Boolean terminated = false;
    private final String STATUS_CANCELLED = "cancelled";
    private GoogleAccountCredential credential;
    private final int exGetEventsOperation = 11;
    private String accountName;
    private Activity activity;
    private OperationCompleted completed;
    private int id;
    final HttpTransport transport = AndroidHttp.newCompatibleTransport();
    final com.google.api.client.json.JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
    private Context context;
    private com.google.api.services.calendar.Calendar client;

    public GoogleWebService(String url, String user, String password, String domain, Context applicationContext, Activity activity) {
        super(url, user, password, domain);
        setServiceType(ServiceType.GOOGLE_CALENDAR);
        this.context = applicationContext;
        this.activity = activity;
    }

    public void getEvents(OperationCompleted completed, int id) {
        accountName = getCredentials().getUser();
        this.completed = completed;
        this.id = id;

        if(!terminated) {
            GoogleAccountManager gam = new GoogleAccountManager(context);
            getAndUseAuthTokenInAsyncTask(gam.getAccountByName(accountName));
        }
    }

    void getAndUseAuthTokenInAsyncTask(final Account account) {

        if(Looper.myLooper() == Looper.getMainLooper()) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {

                    AsyncTask<Account, String, String> task = new AsyncTask<Account, String, String>() {
                        ProgressDialog progressDlg;
                        AsyncTask<Account, String, String> me = this;

                        @Override
                        protected void onPreExecute() {
                            if(activity != null) {
                                progressDlg = new ProgressDialog(activity, ProgressDialog.STYLE_SPINNER);
                                progressDlg.setMax(100);
                                progressDlg.setTitle("Validating...");
                                progressDlg.setMessage("Verifying the login data you entered...\n\nThis action will time out after 10 seconds.");
                                progressDlg.setCancelable(false);
                                progressDlg.setIndeterminate(false);
                                progressDlg.setOnCancelListener(new android.content.DialogInterface.OnCancelListener() {
                                    public void onCancel(DialogInterface d) {
                                        // progressDlg.dismiss();
                                        me.cancel(true);
                                    }
                                });
                                // progressDlg.show();
                            }
                        }

                        @Override
                        protected String doInBackground(Account... params) {
                            return getAccessToken(account);
                        }

                        @Override
                        protected void onPostExecute(String s) {
                            if (s == null) {
                                // Wait for the extra intent
                            } else {
                                fetchEvents();
                            }
                            if(activity != null)
                                progressDlg.dismiss();
                        }
                    };
                    task.execute(account);
                }

            });
        }
        else {
            getAccessToken(account);
            fetchEvents();
        }

    }

    private String getAccessToken(Account account) {
        try {
            return GoogleAuthUtil.getToken(context, account.name, "oauth2:" + CalendarScopes.CALENDAR);  // IMPORTANT: DriveScopes must be changed depending on what level of access you want
        } catch (UserRecoverableAuthException e) {
            // Start the Approval Screen intent, if not run from an Activity, add the Intent.FLAG_ACTIVITY_NEW_TASK flag.
            if(activity != null) {
                activity.startActivityForResult(e.getIntent(), 77);
            }
            e.printStackTrace();
            return null;
        } catch (GoogleAuthException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void fetchEvents() {
        if(!terminated) {
            credential = GoogleAccountCredential.usingOAuth2(context, Collections.singleton(CalendarScopes.CALENDAR));
            credential.setSelectedAccountName(accountName);
            // Calendar client
            client = ApplicationContextProvider.getClient();
            if(client == null) {
                client = new com.google.api.services.calendar.Calendar.Builder(
                        transport, jsonFactory, credential).setApplicationName("Google-CalendarAndroidSample/1.0")
                        .build();
                ApplicationContextProvider.setClient(client);
            }

            final OperationCredentials creds = this.getCredentials();
            if(Looper.myLooper() == Looper.getMainLooper()) {
                WebServiceGoogleGetEventsOperation operation = new WebServiceGoogleGetEventsOperation(completed, id);
                operation.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, creds);
            }
            else {
                ArrayList<Event> allEvents = new ArrayList<Event>();
                try{
                    com.google.api.services.calendar.Calendar client = ApplicationContextProvider.getClient();

                    // Iterate through entries in calendar list
                    String cpageToken = null;
                    do {
                        CalendarList calendarList = client.calendarList().list().setPageToken(cpageToken).execute();
                        List<CalendarListEntry> items = calendarList.getItems();

                        for (CalendarListEntry calendarListEntry : items) {
                            String calendarId = calendarListEntry.getId();
                            ArrayList<Event> events = getEventsFromCalendarById(calendarId, client, creds);
                            allEvents.addAll(events);
                        }
                        cpageToken = calendarList.getNextPageToken();
                    } while (cpageToken != null);
                }
                catch(Exception e) {
                    allEvents = EventsProxy.sharedProxy().getAllEvents();
                    e.printStackTrace();
                }

                completed.onOperationCompleted(allEvents,id);
            }
        }
    }

    private ArrayList<Event> getEventsFromCalendarById(String calendarId, com.google.api.services.calendar.Calendar client, OperationCredentials credentials) throws IOException {
        ArrayList<Event> events = new ArrayList<Event>();
        String pageToken = null;
        do {

            Events calendarEvents = client.events().list(calendarId).setSingleEvents(true).setPageToken(pageToken).execute();
            List<com.google.api.services.calendar.model.Event> items = calendarEvents.getItems();
            Event entityEvent = null;
            for (com.google.api.services.calendar.model.Event event : items) {
                String id = event.getId();
                Log.v("EVENTS_ID", id);
                if(!event.getStatus().equalsIgnoreCase(STATUS_CANCELLED)) {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    java.util.Date modifiedDate = new java.util.Date((long)event.getUpdated().getValue());

                    String modified = formatter.format(modifiedDate);
                    String subject = event.getSummary();
                    String body = event.getDescription();
                    String location = event.getLocation();

                    String startStr = getStartDateTimeStr(event);
                    String endStr = getEndDateTimeStr(event);
                    String start = startStr;
                    String end = endStr;

                    List<EventAttendee> attendees = event.getAttendees();
                    String requiredGuysStr = "";
                    String optionalGuysStr = "";
                    if(attendees != null) {
                        for (int i=0; i<attendees.size(); i++) {
                            EventAttendee attendee = attendees.get(i);
                            Boolean optional = false;

                            try {
                                attendee.getOptional();
                            } catch (Exception e) {
                                optional = true;
                            }

                            if(optional) {
                                String delimiter = (i==attendees.size() -1 ) ? "" : ";";
                                String displayName = (attendee.getDisplayName() != null) ? attendee.getDisplayName() : attendee.getEmail();
                                optionalGuysStr = optionalGuysStr + displayName + delimiter;
                            }
                            else {
                                String displayName = (attendee.getDisplayName() != null) ? attendee.getDisplayName() : attendee.getEmail();
                                String delimiter = (i==attendees.size() -1 ) ? "" : ";";
                                requiredGuysStr = requiredGuysStr + displayName + delimiter;
                            }
                        }
                    }
                    entityEvent = new Event(id, subject, start, end, body, credentials.getUser());
                    entityEvent.setModified(modified);
                    entityEvent.setLocation(location);
                    entityEvent.setCalendarName(calendarId);
                    entityEvent.setRequiredAttendees(requiredGuysStr);
                    entityEvent.setOptionalAttendees(optionalGuysStr);
                    events.add(entityEvent);
                }
            }
            pageToken = calendarEvents.getNextPageToken();
        } while (pageToken != null);

        return events;
    }

    String getStartDateTimeStr(com.google.api.services.calendar.model.Event event) {
        EventDateTime dateTime = event.getStart();
        com.google.api.client.util.DateTime __timeToStart = dateTime.getDateTime();
        if(__timeToStart == null) {
            __timeToStart = dateTime.getDate();
        }
        long value = __timeToStart.getValue();
        java.util.Date startDate = new java.util.Date(value);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(startDate);
    }

    String getEndDateTimeStr(com.google.api.services.calendar.model.Event event) {
        EventDateTime dateTime = event.getEnd();
        com.google.api.client.util.DateTime __timeToEnd = dateTime.getDateTime();
        if(__timeToEnd == null) {
            __timeToEnd = dateTime.getDate();
        }
        long value = __timeToEnd.getValue();
        java.util.Date endDate = new java.util.Date(value);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(endDate);
    }

    public void terminate() {
        activity = null;
        terminated = true;
    }
}
