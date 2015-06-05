package com.exchange.ross.exchangeapp.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

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
    private Context context;
    private String DATABASE_TABLE = "AppInternalSettings";
    private String[] projection = new String[] { "_id", "sound", "vibro",
            "status_busy", "ignore_allday", "day_meetings", "timer", "reload_events_by_changing_status_busy", "reload_events_by_changing_ignore_allday" };

    public SettingsDAO(Context context) {
        this.context = context;
        insertInitialRecord();
    }

    private void insertInitialRecord() {
        ContentValues cv = new ContentValues();
        cv.put("_id", 1);
        cv.put("sound", 0);
        cv.put("vibro", 1);
        cv.put("status_busy", 0);
        cv.put("ignore_allday", 1);
        cv.put("day_meetings", 1);
        cv.put("timer", 1);

        Uri contentUri = Uri.withAppendedPath(SettingsContentProvider.CONTENT_URI, DATABASE_TABLE);
        Uri resultUri = context.getContentResolver().insert(contentUri, cv);
    }

    public Boolean getSound() {
        Uri contentUri = Uri.withAppendedPath(SettingsContentProvider.CONTENT_URI, DATABASE_TABLE);
        Cursor cursor = context.getContentResolver().query(contentUri, projection, null, null, null);

        Boolean sound = false;
        if (cursor.moveToFirst()) {
            sound = cursor.getInt(cursor
                    .getColumnIndex("sound")) > 0;
        }
        return sound;
    }

    public void setSound(Boolean sound) {
        String filter = "_id = " + 1;
        ContentValues args = new ContentValues();
        args.put("sound", sound);
        Uri contentUri = Uri.withAppendedPath(SettingsContentProvider.CONTENT_URI, DATABASE_TABLE);
        context.getContentResolver().update(contentUri, args,null,null);
    }

    public Boolean getVibro() {
        Uri contentUri = Uri.withAppendedPath(SettingsContentProvider.CONTENT_URI, DATABASE_TABLE);
        Cursor cursor = context.getContentResolver().query(contentUri, projection, null, null, null);

        Boolean vibro = false;
        if (cursor.moveToFirst()) {
            vibro = cursor.getInt(cursor
                    .getColumnIndex("vibro")) > 0;
        }
        cursor.close();
        return vibro;
    }

    public void setVibro(Boolean vibro) {
        String filter = "_id = " + 1;
        ContentValues args = new ContentValues();
        args.put("vibro", vibro);
        Uri contentUri = Uri.withAppendedPath(SettingsContentProvider.CONTENT_URI, DATABASE_TABLE);
        context.getContentResolver().update(contentUri, args,null,null);
    }

    public Boolean getStatusBusy() {
        Uri contentUri = Uri.withAppendedPath(SettingsContentProvider.CONTENT_URI, DATABASE_TABLE);
        Cursor cursor = context.getContentResolver().query(contentUri, projection, null, null, null);
        Boolean statusBusy = false;
        if (cursor.moveToFirst()) {
            statusBusy = cursor.getInt(cursor
                    .getColumnIndex("status_busy")) > 0;
        }
        cursor.close();
        return statusBusy;
    }

    public void setStatusBusy(Boolean statusBusy) {
        String filter = "_id = " + 1;
        ContentValues args = new ContentValues();
        args.put("status_busy", statusBusy);
        Uri contentUri = Uri.withAppendedPath(SettingsContentProvider.CONTENT_URI, DATABASE_TABLE);
        context.getContentResolver().update(contentUri, args,null,null);
    }

    public Boolean getIgnoreAllDay() {
        Uri contentUri = Uri.withAppendedPath(SettingsContentProvider.CONTENT_URI, DATABASE_TABLE);
        Cursor cursor = context.getContentResolver().query(contentUri, projection, null, null, null);
        Boolean ignoreAllDay = false;
        if (cursor.moveToFirst()) {
            ignoreAllDay = cursor.getInt(cursor
                    .getColumnIndex("ignore_allday")) > 0;
        }
        cursor.close();
        return ignoreAllDay;
    }

    public void setIgnoreAllday(Boolean ignoreAllDay) {
        String filter = "_id = " + 1;
        ContentValues args = new ContentValues();
        args.put("ignore_allday", ignoreAllDay);
        Uri contentUri = Uri.withAppendedPath(SettingsContentProvider.CONTENT_URI, DATABASE_TABLE);
        context.getContentResolver().update(contentUri, args,null,null);
    }

    public Boolean getListMeetingsForDay() {
        Uri contentUri = Uri.withAppendedPath(SettingsContentProvider.CONTENT_URI, DATABASE_TABLE);
        Cursor cursor = context.getContentResolver().query(contentUri, projection, null, null, null);

        Boolean ignoreAllDay = false;
        if (cursor.moveToFirst()) {
            ignoreAllDay = cursor.getInt(cursor
                    .getColumnIndex("day_meetings")) > 0;
        }
        cursor.close();
        return ignoreAllDay;
    }

    public void setListMeetingsForDay(Boolean listMeetingsForDay) {
        String filter = "_id = " + 1;
        ContentValues args = new ContentValues();
        args.put("day_meetings", listMeetingsForDay);
        Uri contentUri = Uri.withAppendedPath(SettingsContentProvider.CONTENT_URI, DATABASE_TABLE);
        context.getContentResolver().update(contentUri, args,null,null);
    }

    public Boolean getTimer() {
        Uri contentUri = Uri.withAppendedPath(SettingsContentProvider.CONTENT_URI, DATABASE_TABLE);
        Cursor cursor = context.getContentResolver().query(contentUri, projection, null, null, null);
        Boolean timer = false;
        if (cursor.moveToFirst()) {
            timer = cursor.getInt(cursor
                    .getColumnIndex("timer")) > 0;
        }
        cursor.close();
        return timer;
    }

    public void setTimer(Boolean timer) {
        String filter = "_id = " + 1;
        ContentValues args = new ContentValues();
        args.put("timer", timer);
        Uri contentUri = Uri.withAppendedPath(SettingsContentProvider.CONTENT_URI, DATABASE_TABLE);
        context.getContentResolver().update(contentUri, args,null,null);
    }

    public Boolean getInvolvesEvensListReloadByChangingStatusBusy() {
        Uri contentUri = Uri.withAppendedPath(SettingsContentProvider.CONTENT_URI, DATABASE_TABLE);
        Cursor cursor = context.getContentResolver().query(contentUri, projection, null, null, null);
        Boolean involvesEvensListReloadByChangingStatusBusy = false;
        if (cursor.moveToFirst()) {
            involvesEvensListReloadByChangingStatusBusy = cursor.getInt(cursor
                    .getColumnIndex("reload_events_by_changing_status_busy")) > 0;
        }
        cursor.close();
        return involvesEvensListReloadByChangingStatusBusy;
    }

    public void setInvolvesEvensListReloadByChangingStatusBusy(Boolean involvesEvensListReloadByChangingStatusBusy) {
        String filter = "_id = " + 1;
        ContentValues args = new ContentValues();
        args.put("reload_events_by_changing_status_busy", involvesEvensListReloadByChangingStatusBusy);
        Uri contentUri = Uri.withAppendedPath(SettingsContentProvider.CONTENT_URI, DATABASE_TABLE);
        context.getContentResolver().update(contentUri, args,null,null);
    }

    public Boolean getInvolvesEvensListReloadByChangingIgnoreAllDayEvents() {
        Uri contentUri = Uri.withAppendedPath(SettingsContentProvider.CONTENT_URI, DATABASE_TABLE);
        Cursor cursor = context.getContentResolver().query(contentUri, projection, null, null, null);
        Boolean involvesEvensListReloadByChangingIgnoreAllDayEvents = false;
        if (cursor.moveToFirst()) {
            involvesEvensListReloadByChangingIgnoreAllDayEvents = cursor.getInt(cursor
                    .getColumnIndex("reload_events_by_changing_ignore_allday")) > 0;
        }
        cursor.close();
        return involvesEvensListReloadByChangingIgnoreAllDayEvents;
    }

    public void setInvolvesEvensListReloadByChangingIgnoreAllDayEvents(Boolean involvesEvensListReloadByChangingIgnoreAllDayEvents) {
        String filter = "_id = " + 1;
        ContentValues args = new ContentValues();
        args.put("reload_events_by_changing_ignore_allday", involvesEvensListReloadByChangingIgnoreAllDayEvents);
        Uri contentUri = Uri.withAppendedPath(SettingsContentProvider.CONTENT_URI, DATABASE_TABLE);
        context.getContentResolver().update(contentUri, args,null,null);
    }

    private void initDB() {
        //database = DatabaseManager.getInstance().openDatabase();
    }
    private void closeDB() {
        //database.close();
    }
}
