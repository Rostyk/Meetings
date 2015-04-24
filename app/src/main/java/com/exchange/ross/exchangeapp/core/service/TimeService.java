package com.exchange.ross.exchangeapp.core.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.exchange.ross.exchangeapp.APIs.WebService;
import com.exchange.ross.exchangeapp.APIs.operations.OperationCompleted;
import com.exchange.ross.exchangeapp.Utils.ApplicationContextProvider;
import com.exchange.ross.exchangeapp.core.entities.Event;
import com.exchange.ross.exchangeapp.Utils.EventsManager;
import com.exchange.ross.exchangeapp.db.AccountsProxy;
import com.exchange.ross.exchangeapp.db.DatabaseManager;
import com.exchange.ross.exchangeapp.db.EventsProxy;
import com.exchange.ross.exchangeapp.db.WHDatabaseHelper;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TimeService extends Service {
    private ScheduledExecutorService worker =
            Executors.newSingleThreadScheduledExecutor();
    private final int exGetEventsOperation = 19;
    private static Timer syncTimer = new Timer();
    private static Timer timer = new Timer();
    private Context ctx;
    public static final String TIMER_BR = "com.ross.exchangeapp.timer";
    public static final String SYNC_NEW_EVENTS_BR = "com.ross.exchnageapp.new_events";
    Intent bi = new Intent(TIMER_BR);
    Intent newEventsIntent = new Intent(SYNC_NEW_EVENTS_BR);

    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        ctx = this;
        startService();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent.getBooleanExtra("ForceSync", false)) {
            syncEvents();
        }
        return START_STICKY;
    }

    private void startService() {
        syncTimer.scheduleAtFixedRate(new syncEventsTask(), 10000, 60000);
        timer.scheduleAtFixedRate(new checkEventsTask(), 1000, 90000);
    }

    private class checkEventsTask extends TimerTask {
        public void run() {
            checkEvents();
        }

    }
    private void checkEvents() {
        ApplicationContextProvider.setApplicationContext(getApplicationContext());
        DatabaseManager.initializeInstance(new WHDatabaseHelper(getApplicationContext()));

        EventsManager.sharedManager().countOngoingEvents();
        LocalBroadcastManager.getInstance(ApplicationContextProvider.getContext()).sendBroadcast(bi);
    }

    private class syncEventsTask extends TimerTask {
        public void run() {
            syncEvents();
        }
    }

    private void syncEvents() {

        ApplicationContextProvider.setApplicationContext(getApplicationContext());
        DatabaseManager.initializeInstance(new WHDatabaseHelper(getApplicationContext()));

        ArrayList<WebService> services = AccountsProxy.sharedProxy().getAllAccounts();
        if(services.size() > 0) {
            WebService service = services.get(0);

            service.getEvents(new OperationCompleted() {
                @Override
                public void onOperationCompleted(Object result, int id) {
                    if(id == exGetEventsOperation) {
                        ArrayList<Event> events = (ArrayList<Event> )result;

                        //Save new events which are not in the database
                        EventsProxy proxy = EventsProxy.sharedProxy();


                        ArrayList<ArrayList<Event>> syncEvents = EventsProxy.sharedProxy().getSyncEvents(events);

                        ArrayList<Event> itemsToInsert = syncEvents.get(0);
                        ArrayList<Event> itemsToUpdate = syncEvents.get(1);
                        ArrayList<Event> itemsToRemove = syncEvents.get(2);


                        sync(itemsToInsert, itemsToUpdate, itemsToRemove);

                        for(ArrayList<Event> s : syncEvents) {
                            s.clear();
                        }
                        events.clear();
                    }

                }
            }, exGetEventsOperation);
        }
    }

    public void sync(ArrayList<Event> itemsToInsert, ArrayList<Event> itemsToUpdate, ArrayList<Event> itemsToRemove) {
        EventsProxy proxy = EventsProxy.sharedProxy();
        proxy.sync(itemsToInsert, itemsToUpdate, itemsToRemove);
        updateUI();
    }

    public void updateUI() {
        worker = Executors.newSingleThreadScheduledExecutor();
        Runnable task = new Runnable() {
            public void run() {
                EventsManager.sharedManager().countOngoingEvents();
                LocalBroadcastManager.getInstance(ApplicationContextProvider.getContext()).sendBroadcast(newEventsIntent);
            }
        };
        worker.schedule(task, 1, TimeUnit.SECONDS);
    }

    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        syncTimer.cancel();
    }

}
