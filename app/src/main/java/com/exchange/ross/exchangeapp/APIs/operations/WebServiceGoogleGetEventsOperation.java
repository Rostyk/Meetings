package com.exchange.ross.exchangeapp.APIs.operations;

import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;

import com.exchange.ross.exchangeapp.Utils.ApplicationContextProvider;
import com.exchange.ross.exchangeapp.core.entities.Event;
import com.exchange.ross.exchangeapp.db.EventsProxy;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import microsoft.exchange.webservices.data.Attendee;


/**
 * Created by ross on 3/27/15.
 */
public class WebServiceGoogleGetEventsOperation extends AsyncTask<OperationCredentials, Float, ArrayList<Event>> {
    private OperationCompleted listener;
    private URI uri;
    private int id;
    private final String STATUS_CANCELLED = "cancelled";


    public WebServiceGoogleGetEventsOperation(OperationCompleted listener, int id){
        this.listener = listener;
        this.id = id;
    }

    protected ArrayList<Event> doInBackground(OperationCredentials... credentialses) {
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
                    ArrayList<Event> events = getEventsFromCalendarById(calendarId, client, credentialses[0]);
                    allEvents.addAll(events);
                }
                cpageToken = calendarList.getNextPageToken();
            } while (cpageToken != null);
        }
        catch(Exception e) {
            allEvents = EventsProxy.sharedProxy().getAllEvents();
            e.printStackTrace();
        }

        return allEvents;
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
                    entityEvent.checkIfAllDayEvent();
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



    @Override
    protected void onPostExecute(ArrayList<Event> events) {
        super.onPostExecute(events);
        listener.onOperationCompleted(events, this.id);
    }
}
