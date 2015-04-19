package com.exchange.ross.exchangeapp.core.entities;

import android.text.BoringLayout;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ross on 3/21/15.
 */
public class Event implements  Comparable<Event>{
    private String id;
    private String subject;
    private String location;
    private String startDate;
    private String endDate;
    private String body;
    private Boolean mute;
    private String accountName;
    private String modified;
    private String optionalAttendees;
    private String requiredAttendees;
    private String calendarName;

    public Event(String id,String subject, String startDate, String endDate, String body, String accountName) {
        String idValue = (id != null) ? (id) : ("");
        this.id = idValue;

        String subjectValue = (subject != null) ? (subject) : ("");
        this.subject = subjectValue;

        String startDateValue = (startDate != null) ? (startDate) : ("");
        this.startDate = startDateValue;

        String endDateValue = (endDate != null) ? (endDate) : ("");
        this.endDate = endDateValue;

        String bodyValue = (body != null) ? (body) : ("");
        this.body = bodyValue;

        String accountNameValue = (accountName != null) ? (accountName) : ("");
        this.accountName = accountNameValue;

        // by default all events mute the device
        this.mute = true;
    }

    public String getSubject() {
        return subject;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountName() {

        return accountName;
    }

    public Boolean getMute() {
        return mute;
    }

    public void setMute(Boolean mute) {
        this.mute = mute;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOptionalAttendees() {
        return optionalAttendees;
    }

    public String getRequiredAttendees() {
        return requiredAttendees;
    }

    public String getCalendarName() {
        return calendarName;
    }

    public void setOptionalAttendees(String optionalAttendees) {
        this.optionalAttendees = optionalAttendees;
    }

    public void setRequiredAttendees(String requiredAttendees) {
        this.requiredAttendees = requiredAttendees;
    }

    public void setCalendarName(String calendarName) {
        this.calendarName = calendarName;
    }

    @Override
    public int compareTo(Event event) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date date1 = formatter.parse(getStartDate());
            Date date2 = formatter.parse(event.getStartDate());
            return date2.compareTo(date1);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        Event event = (Event)o;
        Boolean result = id.equals(event.getId());
        return result;
    }

    public Date getStartDateInDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return formatter.parse(getStartDate());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public Date getEndDateInDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return formatter.parse(getEndDate());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
