package com.exchange.ross.exchangeapp.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ross on 4/20/15.
 */
public class Settings {
    private static Settings instance;

    private Boolean involvesEvensListReloadByChangingStatusBusy = false;
    private Boolean involvesEvensListReloadByChangingIgnoreAllDayEvents = false;

    private Boolean sound = false;
    private String kSound = "sound";
    private Boolean vibration  = false;
    private String kVibration = "vibration";
    private Boolean ignoreAllDayEvent = false;
    private String kIgnoreAllDayEvent = "ignoreAllDayEvent";
    private Boolean silentOnStatusBusy = false;
    private String kSilentOnStatusBusy = "silentOnStatusBusy";
    private Boolean listMeetingsForDay = false;
    private String kListMeetingsForDay = "listMeetingsForDay";
    private Boolean timer = false;
    private String kTimer = "timer";

    private Context context;
    private SharedPreferences preferences;

    public static synchronized Settings sharedSettings() {
        if (instance == null)
            instance = new Settings();
        return instance;
    }

    public Boolean getSound() {
        return sound;
    }

    public Boolean getVibration() {
        return vibration;
    }

    public Boolean getIgnoreAllDayEvent() {
        return ignoreAllDayEvent;
    }

    public Boolean getSilentOnStatusBusy() {
        return silentOnStatusBusy;
    }

    public Boolean getListMeetingsForDay() {
        return listMeetingsForDay;
    }

    public Boolean getTimer() {
        return timer;
    }

    public void setSound(Boolean sound) {
        this.sound = sound;
        preferences.edit().putBoolean(kSound, sound).apply();
    }

    public void setVibration(Boolean vibration) {
        this.vibration = vibration;
        preferences.edit().putBoolean(kVibration, vibration).apply();
    }

    public void setIgnoreAllDayEvent(Boolean ignoreAllDayEvent) {
        this.ignoreAllDayEvent = ignoreAllDayEvent;
        this.involvesEvensListReloadByChangingIgnoreAllDayEvents = true;
        preferences.edit().putBoolean(kIgnoreAllDayEvent, ignoreAllDayEvent).apply();
    }

    public void setSilentOnStatusBusy(Boolean silentOnStatusBusy) {
        this.silentOnStatusBusy = silentOnStatusBusy;
        this.involvesEvensListReloadByChangingStatusBusy = true;
        preferences.edit().putBoolean(kSilentOnStatusBusy, silentOnStatusBusy).apply();
    }

    public Boolean getInvolvesEvensListReloadByChangingStatusBusy() {
        return involvesEvensListReloadByChangingStatusBusy;
    }

    public void setInvolvesEvensListReloadByChangingStatusBusy(Boolean involvesEvensListReload) {
        this.involvesEvensListReloadByChangingStatusBusy = involvesEvensListReload;
    }

    public Boolean getInvolvesEvensListReloadByChangingIgnoreAllDayEvents() {
        return involvesEvensListReloadByChangingIgnoreAllDayEvents;
    }

    public void setInvolvesEvensListReloadByChangingIgnoreAllDayEvents(Boolean involvesEvensListReloadByChangingIgnoreAllDayEvents) {
        this.involvesEvensListReloadByChangingIgnoreAllDayEvents = involvesEvensListReloadByChangingIgnoreAllDayEvents;
    }

    public void setListMeetingsForDay(Boolean listMeetingsForDay) {
        this.listMeetingsForDay = listMeetingsForDay;
        preferences.edit().putBoolean(kListMeetingsForDay, listMeetingsForDay).apply();
    }

    public void setTimer(Boolean timer) {
        this.timer = timer;
        preferences.edit().putBoolean(kTimer, timer).apply();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;

        if(context != null) {
            Boolean readPreferences = false;
            if(preferences == null) {
                readPreferences = true;
            }
            preferences = context.getSharedPreferences(
                    "com.example.app", Context.MODE_PRIVATE);

            if(readPreferences) {
                getAllSettings();
            }
        }
    }

    private void getAllSettings() {
        this.sound = preferences.getBoolean(kSound, false);
        this.vibration = preferences.getBoolean(kVibration, true);
        this.ignoreAllDayEvent = preferences.getBoolean(kIgnoreAllDayEvent, true);
        this.silentOnStatusBusy = preferences.getBoolean(kSilentOnStatusBusy, false);
        this.listMeetingsForDay = preferences.getBoolean(kListMeetingsForDay, true);
        this.timer = preferences.getBoolean(kTimer, true);
    }
}
