package com.exchange.ross.exchangeapp.db.dao;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.exchange.ross.exchangeapp.db.DatabaseManager;
import com.exchange.ross.exchangeapp.db.WHDatabaseHelper;

/**
 * Created by ross on 6/5/15.
 */
public class SettingsContentProvider extends ContentProvider{
    WHDatabaseHelper dbHelper;
    public static final String SETTINGS = "AppInternalSettings";//specific for our our app, will be specified in maninfed
    public static final Uri CONTENT_URI = Uri.parse("content://" + SETTINGS);

    @Override
    public boolean onCreate() {
        dbHelper = DatabaseManager.getInstance(getContext()).helper();
        return true;
    }

    @Override
    public int delete(Uri uri, String where, String[] args) {
        String table = getTableName(uri);
        SQLiteDatabase dataBase=dbHelper.getWritableDatabase();
        return dataBase.delete(table, where, args);
    }

    @Override
    public String getType(Uri arg0) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        String table = getTableName(uri);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        long value = database.insert(table, null, initialValues);
        return Uri.withAppendedPath(CONTENT_URI, String.valueOf(value));
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        String table =getTableName(uri);
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor =database.query(table,  projection, selection, selectionArgs, null, null, sortOrder);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String whereClause,
                      String[] whereArgs) {
        String table = getTableName(uri);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        return database.update(table, values, whereClause, whereArgs);
    }

    public static String getTableName(Uri uri){
        String value = uri.getPath();
        value = value.replace("/", "");//we need to remove '/'
        return value;
    }
}