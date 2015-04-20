package com.exchange.ross.exchangeapp.Utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioManager;
import android.support.v4.app.NotificationCompat;

import com.exchange.ross.exchangeapp.core.entities.Event;
import com.exchange.ross.exchangeapp.db.EventsProxy;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import com.exchange.ross.exchangeapp.R;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by ross on 3/31/15.
 */
public class EventsManager {
    private Boolean listNeedsRefresh = false;
    private int index;
    private static EventsManager instance;
    private String selectedEventId;
    private ArrayList<Event> ongoingEvents = new ArrayList<Event>();
    private int volume;
    private Boolean muted = true;
    private ArrayList<Event> cachedEvents;

    public static synchronized EventsManager sharedManager() {
        if (instance == null)
            instance = new EventsManager();
        return instance;
    }

    private EventsManager() {

    }

    public ArrayList<Event> eventsForDaySinceNow(int daySinceToday, ArrayList<Event>events) {


        Date date = DateUtils.dateSinceToday(daySinceToday);

        ArrayList<Event> filteredEvents = new ArrayList<Event>();
        for(Event event : events) {
            if(isSameDay(date, event.getStartDateInDate())) {
                filteredEvents.add(event);
            }
        }

        return filteredEvents;
    }

    public ArrayList<Event> eventsByDate(Date date, ArrayList<Event>events) {
        ArrayList<Event> filteredEvents = new ArrayList<Event>();
        for(Event event : events) {
            if(isSameDay(date, event.getStartDateInDate())) {
                filteredEvents.add(event);
            }
        }
        return filteredEvents;
    }

    public void countOngoingEvents() {
        ArrayList<Event> events = EventsProxy.sharedProxy().getAllEvents();
        Date now = new Date();
        ongoingEvents = new ArrayList<Event>();
        for (Event event : events) {
            Date startDate = event.getStartDateInDate();
            Date endDate = event.getEndDateInDate();
            if (isWithinRange(now, startDate, endDate)) {
                ongoingEvents.add(event);
            }
        }
        notifyStatus(ongoingEvents);
        checkMuteState(ongoingEvents.size() > 0);
    }

    private void checkMuteState(Boolean mute) {
        Context context = ApplicationContextProvider.getContext();
        if(context != null) {
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            if(mute) {
                audioManager.setMode(AudioManager.MODE_IN_CALL);
                audioManager.setStreamSolo(AudioManager.STREAM_VOICE_CALL, true);
                muted = true;
            }
            else {
                if(muted) {
                    audioManager.setStreamSolo(AudioManager.STREAM_VOICE_CALL, false);
                    audioManager.setMode(AudioManager.MODE_NORMAL );
                    muted = false;
                }
            }
        }

    }

    public void notifyStatus(ArrayList<Event> events) {
        for(Event event : events) {
            int notificationId = event.getId().hashCode();
            index++;
            NotificationCompat.Builder builder = new NotificationCompat.Builder(ApplicationContextProvider.getContext());
            double minutesElapsed = getDateDiff(event.getStartDateInDate(), new Date(), TimeUnit.MINUTES);
            double duration = getDateDiff(event.getStartDateInDate(),event.getEndDateInDate(), TimeUnit.MINUTES);
            int drawable;

            double progress = (minutesElapsed*1.0 / duration*1.0);
            int res = 100 - (int)(progress * 100.0);
            int drawableResourceId = ApplicationContextProvider.getsContext().getResources().getIdentifier("progress" + res + "", "drawable", ApplicationContextProvider.getsContext().getPackageName());


            Notification notification = builder
                    .setSmallIcon(drawableResourceId)
                    .setContentTitle(event.getSubject())
                    .setContentText("Meeting started: " + minutesElapsed + " mintues elapsed of " + duration)
                    .build();
            NotificationManager notificationManager = (NotificationManager)ApplicationContextProvider.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(notificationId , notification);
        }
    }

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }


    public String getSelectedEventId() {
        return selectedEventId;
    }

    public void setSelectedEventId(String selectedEventId) {
        this.selectedEventId = selectedEventId;
    }

    public ArrayList<Event> ongoingEvents() {
        return ongoingEvents;
    }

    boolean isWithinRange(Date testDate, Date startDate, Date endDate) {
        return !(testDate.before(startDate) || testDate.after(endDate));
    }

    boolean isSameDay(Date date1, Date date2) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.format(date1).equals(format.format(date2));
    }

    public void setCachedEvents(ArrayList<Event> cachedEvents) {
        if(this.cachedEvents != null) {
            this.cachedEvents.clear();
        }
        this.cachedEvents = cachedEvents;
    }

    public Boolean getListNeedsRefresh() {
        return listNeedsRefresh;
    }

    public void setListNeedsRefresh(Boolean listNeedsRefresh) {
        this.listNeedsRefresh = listNeedsRefresh;
    }

    public ArrayList<Event> getCachedEvents() {
        return cachedEvents;
    }
}
