package com.exchange.ross.exchangeapp.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.exchange.ross.exchangeapp.APIs.ExchangeWebService;
import com.exchange.ross.exchangeapp.APIs.GoogleWebService;
import com.exchange.ross.exchangeapp.APIs.WebService;
import com.exchange.ross.exchangeapp.Utils.ApplicationContextProvider;
import com.exchange.ross.exchangeapp.db.DatabaseManager;

import java.util.ArrayList;

/**
 * Created by ross on 6/4/15.
 */
public class SettingsDAO {
    private SQLiteDatabase database;
    private String DATABASE_TABLE = "Settings";

    public SettingsDAO() {
        insertInitialRecord();
    }

    private void insertInitialRecord() {
        initDB();
        ContentValues cv = new ContentValues();
        cv.put("_id", 1);
        cv.put("sound", 0);
        cv.put("vibration", 1);
        cv.put("status_busy", 0);
        cv.put("ignore_allday", 1);
        cv.put("day_meetings", 1);
        cv.put("timer", 1);

        database.insert(DATABASE_TABLE, null, cv);
        closeDB();
    }

    public Boolean getSound() {
        initDB();
        Boolean sound = false;
        Cursor cursor = database.rawQuery("select * from " + DATABASE_TABLE,null);
        if (cursor.moveToFirst()) {
            sound = cursor.getInt(cursor
                    .getColumnIndex("sound")) > 0;
        }
        cursor.close();
        closeDB();
        return sound;
    }

    public void setSound(Boolean sound) {
        initDB();
        String filter = "_id = " + 1;
        ContentValues args = new ContentValues();
        args.put("sound", sound);
        database.update(DATABASE_TABLE, args, filter, null);
        closeDB();
    }

    public Boolean getVibro() {
        initDB();
        Boolean vibro = false;
        Cursor cursor = database.rawQuery("select * from " + DATABASE_TABLE,null);
        if (cursor.moveToFirst()) {
            vibro = cursor.getInt(cursor
                    .getColumnIndex("vibration")) > 0;
        }
        cursor.close();
        closeDB();
        return vibro;
    }

    public void setVibro(Boolean vibro) {
        initDB();
        String filter = "_id = " + 1;
        ContentValues args = new ContentValues();
        args.put("vibro", vibro);
        database.update(DATABASE_TABLE, args, filter, null);
        closeDB();
    }

    public Boolean getStatusBusy() {
        initDB();
        Boolean statusBusy = false;
        Cursor cursor = database.rawQuery("select * from " + DATABASE_TABLE,null);
        if (cursor.moveToFirst()) {
            statusBusy = cursor.getInt(cursor
                    .getColumnIndex("status_busy")) > 0;
        }
        cursor.close();
        closeDB();
        return statusBusy;
    }

    public void setStatusBusy(Boolean statusBusy) {
        initDB();
        String filter = "_id = " + 1;
        ContentValues args = new ContentValues();
        args.put("status_busy", statusBusy);
        database.update(DATABASE_TABLE, args, filter, null);
        closeDB();
    }

    public Boolean getIgnoreAllDay() {
        initDB();
        Boolean ignoreAllDay = false;
        Cursor cursor = database.rawQuery("select * from " + DATABASE_TABLE,null);
        if (cursor.moveToFirst()) {
            ignoreAllDay = cursor.getInt(cursor
                    .getColumnIndex("ignore_allday")) > 0;
        }
        cursor.close();
        closeDB();
        return ignoreAllDay;
    }

    public void setIgnoreAllday(Boolean ignoreAllDay) {
        initDB();
        String filter = "_id = " + 1;
        ContentValues args = new ContentValues();
        args.put("ignore_allday", ignoreAllDay);
        database.update(DATABASE_TABLE, args, filter, null);
        closeDB();
    }

    public Boolean getListMeetingsForDay() {
        initDB();
        Boolean ignoreAllDay = false;
        Cursor cursor = database.rawQuery("select * from " + DATABASE_TABLE,null);
        if (cursor.moveToFirst()) {
            ignoreAllDay = cursor.getInt(cursor
                    .getColumnIndex("day_meetings")) > 0;
        }
        cursor.close();
        closeDB();
        return ignoreAllDay;
    }

    public void setListMeetingsForDay(Boolean listMeetingsForDay) {
        initDB();
        String filter = "_id = " + 1;
        ContentValues args = new ContentValues();
        args.put("day_meetings", listMeetingsForDay);
        database.update(DATABASE_TABLE, args, filter, null);
        closeDB();
    }

    public Boolean getTimer() {
        initDB();
        Boolean timer = false;
        Cursor cursor = database.rawQuery("select * from " + DATABASE_TABLE,null);
        if (cursor.moveToFirst()) {
            timer = cursor.getInt(cursor
                    .getColumnIndex("timer")) > 0;
        }
        cursor.close();
        closeDB();
        return timer;
    }

    public void setTimer(Boolean timer) {
        initDB();
        String filter = "_id = " + 1;
        ContentValues args = new ContentValues();
        args.put("timer", timer);
        database.update(DATABASE_TABLE, args, filter, null);
        closeDB();
    }

    public Boolean getInvolvesEvensListReloadByChangingStatusBusy() {
        initDB();
        Boolean involvesEvensListReloadByChangingStatusBusy = false;
        Cursor cursor = database.rawQuery("select * from " + DATABASE_TABLE,null);
        if (cursor.moveToFirst()) {
            involvesEvensListReloadByChangingStatusBusy = cursor.getInt(cursor
                    .getColumnIndex("reload_events_by_changing_status_busy")) > 0;
        }
        cursor.close();
        closeDB();
        return involvesEvensListReloadByChangingStatusBusy;
    }

    public void setInvolvesEvensListReloadByChangingStatusBusy(Boolean involvesEvensListReloadByChangingStatusBusy) {
        initDB();
        String filter = "_id = " + 1;
        ContentValues args = new ContentValues();
        args.put("timer", involvesEvensListReloadByChangingStatusBusy);
        database.update(DATABASE_TABLE, args, filter, null);
        closeDB();
    }

    public Boolean getInvolvesEvensListReloadByChangingIgnoreAllDayEvents() {
        initDB();
        Boolean involvesEvensListReloadByChangingIgnoreAllDayEvents = false;
        Cursor cursor = database.rawQuery("select * from " + DATABASE_TABLE,null);
        if (cursor.moveToFirst()) {
            involvesEvensListReloadByChangingIgnoreAllDayEvents = cursor.getInt(cursor
                    .getColumnIndex("reload_events_by_changing_ignore_allday")) > 0;
        }
        cursor.close();
        closeDB();
        return involvesEvensListReloadByChangingIgnoreAllDayEvents;
    }

    public void setInvolvesEvensListReloadByChangingIgnoreAllDayEvents(Boolean involvesEvensListReloadByChangingIgnoreAllDayEvents) {
        initDB();
        String filter = "_id = " + 1;
        ContentValues args = new ContentValues();
        args.put("reload_events_by_changing_ignore_allday", involvesEvensListReloadByChangingIgnoreAllDayEvents);
        database.update(DATABASE_TABLE, args, filter, null);
        closeDB();
    }

    private void initDB() {
        database = DatabaseManager.getInstance().openDatabase();
    }
    private void closeDB() {
        //database.close();
    }
}
