package com.exchange.ross.exchangeapp.db;
import android.content.ContentValues;
import android.database.Cursor;

import com.exchange.ross.exchangeapp.db.dao.SettingsDAO;

/**
 * Created by ross on 6/4/15.
 */
public class SettingsProxy {
    private static SettingsProxy instance;
    private SettingsDAO settingsDAO;

    public static synchronized SettingsProxy sharedProxy() {
        if (instance == null)
            instance = new SettingsProxy();
        return instance;
    }

    private SettingsProxy() {
        if(settingsDAO == null) {
            settingsDAO = new SettingsDAO();
        }
    }

    public Boolean getSound() {
        return settingsDAO.getSound();
    }

    public void setSound(Boolean sound) {
        settingsDAO.setSound(sound);
    }

    public Boolean getVibro() {
        return settingsDAO.getVibro();
    }

    public void setVibro(Boolean vibro) {
        settingsDAO.setVibro(vibro);
    }

    public Boolean getStatusBusy() {
        return settingsDAO.getStatusBusy();
    }

    public void setStatusBusy(Boolean statusBusy) {
        setInvolvesEvensListReloadByChangingStatusBusy(true);
        settingsDAO.setStatusBusy(statusBusy);
    }

    public Boolean getIgnoreAllDay() {
        return settingsDAO.getIgnoreAllDay();
    }

    public void setIgnoreAllday(Boolean ignoreAllDay) {
       setInvolvesEvensListReloadByChangingIgnoreAllDayEvents(true);
       settingsDAO.setIgnoreAllday(ignoreAllDay);
    }

    public Boolean getListMeetingsForDay() {
        return settingsDAO.getListMeetingsForDay();
    }

    public void setListMeetingsForDay(Boolean listMeetingsForDay) {
        settingsDAO.setListMeetingsForDay(listMeetingsForDay);
    }

    public Boolean getTimer() {
        return settingsDAO.getTimer();
    }

    public void setTimer(Boolean timer) {
        settingsDAO.setTimer(timer);
    }

    public Boolean getInvolvesEvensListReloadByChangingStatusBusy() {
        return settingsDAO.getInvolvesEvensListReloadByChangingStatusBusy();
    }

    public void setInvolvesEvensListReloadByChangingStatusBusy(Boolean involvesEvensListReloadByChangingStatusBusy) {
        settingsDAO.setInvolvesEvensListReloadByChangingStatusBusy(involvesEvensListReloadByChangingStatusBusy);
    }

    public Boolean getInvolvesEvensListReloadByChangingIgnoreAllDayEvents() {
        return settingsDAO.getInvolvesEvensListReloadByChangingIgnoreAllDayEvents();
    }

    public void setInvolvesEvensListReloadByChangingIgnoreAllDayEvents(Boolean involvesEvensListReloadByChangingIgnoreAllDayEvents) {
        settingsDAO.setInvolvesEvensListReloadByChangingIgnoreAllDayEvents(involvesEvensListReloadByChangingIgnoreAllDayEvents);
    }

}
