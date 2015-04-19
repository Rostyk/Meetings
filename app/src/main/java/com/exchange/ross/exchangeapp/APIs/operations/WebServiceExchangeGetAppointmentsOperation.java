package com.exchange.ross.exchangeapp.APIs.operations;

import android.os.AsyncTask;
import android.util.Log;

import com.exchange.ross.exchangeapp.core.entities.Event;
import com.exchange.ross.exchangeapp.db.EventsProxy;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

import microsoft.exchange.webservices.data.Appointment;
import microsoft.exchange.webservices.data.AppointmentSchema;
import microsoft.exchange.webservices.data.AppointmentType;
import microsoft.exchange.webservices.data.Attendee;
import microsoft.exchange.webservices.data.AttendeeCollection;
import microsoft.exchange.webservices.data.BasePropertySet;
import microsoft.exchange.webservices.data.CalendarFolder;
import microsoft.exchange.webservices.data.CalendarView;
import microsoft.exchange.webservices.data.ExchangeCredentials;
import microsoft.exchange.webservices.data.ExchangeService;
import microsoft.exchange.webservices.data.FindFoldersResults;
import microsoft.exchange.webservices.data.FindItemsResults;
import microsoft.exchange.webservices.data.Folder;
import microsoft.exchange.webservices.data.FolderSchema;
import microsoft.exchange.webservices.data.FolderTraversal;
import microsoft.exchange.webservices.data.FolderView;
import microsoft.exchange.webservices.data.MessageBody;
import microsoft.exchange.webservices.data.PropertySet;
import microsoft.exchange.webservices.data.SearchFilter;
import microsoft.exchange.webservices.data.WebCredentials;
import microsoft.exchange.webservices.data.WellKnownFolderName;


/**
 * Created by ross on 3/21/15.
 */
public class WebServiceExchangeGetAppointmentsOperation extends AsyncTask<OperationCredentials, Float, ArrayList<Event>> {
    private OperationCompleted listener;
    private URI uri;
    private int id;

    public WebServiceExchangeGetAppointmentsOperation(OperationCompleted listener, int id){
        this.listener = listener;
        this.id = id;
    }

    protected ArrayList<Event> doInBackground(OperationCredentials... credentialses) {
        OperationCredentials credential = credentialses[0];
        ArrayList<Event> events = new ArrayList<>();
        try {
             ArrayList<String> calendarNames = new ArrayList<String>();
             ExchangeService service = new ExchangeService();
             ExchangeCredentials credentials = new WebCredentials("oleksandr.gorbenko", "#WolF_stu123456789", "eleks-software");
             service.setCredentials(credentials);
             try {
                    service.setUrl(new URI("https://webmail.eleks.com/EWS/Exchange.asmx"));
                 }
             catch (URISyntaxException e) {
                    e.printStackTrace();
                 }
             SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
             Date startDate = formatter.parse("2015-01-01 12:00:00");
             Date endDate = formatter.parse("2015-07-01 13:00:00");
             CalendarFolder cf=CalendarFolder.bind(service, WellKnownFolderName.Calendar);
             FindItemsResults<Appointment> findResults = cf.findAppointments(new CalendarView(startDate, endDate));
             for (Appointment appt : findResults.getItems()) {
                    appt.load();
                    if(appt.getBody() != null)  {
                        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String eventStartDateUTC = dateFormatter.format(appt.getStart());
                        String eventEndDateStrUTC = dateFormatter.format(appt.getEnd());

                        Date eventStartDate = getDate(eventStartDateUTC);
                        Date eventEndDate = getDate(eventEndDateStrUTC);

                        String eventStartDateLocal = dateFormatter.format(eventStartDate);
                        String eventEndDateLocal = dateFormatter.format(eventEndDate);

                        String id = appt.getId().toString();
                        String modified = dateFormatter.format(appt.getLastModifiedTime());
                        String body = MessageBody.getStringFromMessageBody(appt.getBody());
                        String subject = appt.getSubject();
                        String location = appt.getLocation();
                        AttendeeCollection requiredGuys = appt.getRequiredAttendees();
                        AttendeeCollection optionalGuys = appt.getOptionalAttendees();

                        String requiredGuysStr = "";
                        String optionalGuysStr = "";
                        if(requiredGuys != null) {
                            for (int i=0; i<requiredGuys.getCount(); i++) {
                                Attendee attendee = requiredGuys.getPropertyAtIndex(i);
                                String delimiter = (i==requiredGuys.getCount() -1 ) ? "" : ";";
                                requiredGuysStr = requiredGuysStr + attendee.getName() + delimiter;
                            }
                        }

                        if(optionalGuys != null) {
                            for (int i=0; i<optionalGuys.getCount(); i++) {
                                Attendee attendee = optionalGuys.getPropertyAtIndex(i);
                                String delimiter = (i==optionalGuys.getCount() -1 ) ? "" : ";";
                                optionalGuysStr = optionalGuysStr + attendee.getName() + delimiter;
                            }
                        }

                        Event event = new Event(id, subject, eventStartDateLocal, eventEndDateLocal,  body, credential.getUser());
                        event.setModified(modified);
                        event.setLocation(location);
                        event.setCalendarName("Main");
                        event.setRequiredAttendees(requiredGuysStr);
                        event.setOptionalAttendees(optionalGuysStr);

                        events.add(event);
                    }
             }
        }
        catch(Exception e) {
              events = EventsProxy.sharedProxy().getAllEvents();
              e.printStackTrace();
        }

        return events;
     }

    private Date getDate(String date) throws ParseException{
        Date tempDate = null;
        try {
            DateFormat formatter =
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            tempDate = formatter.parse(date);
        } catch (Exception e) {
            DateFormat formatter = new SimpleDateFormat(
                    "yyyy-MM-dd'T'HH:mm:ss.SSS");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            tempDate = formatter.parse(date);
        }
        return tempDate;
    }



    @Override
    protected void onPostExecute(ArrayList<Event> events) {
        super.onPostExecute(events);
        listener.onOperationCompleted(events, this.id);
    }
}
