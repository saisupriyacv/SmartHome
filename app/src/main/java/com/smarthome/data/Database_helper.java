package com.smarthome.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



public class Database_helper extends SQLiteOpenHelper {

    private String msg = "Database_helper";

    public Database_helper(Context context, String name,CursorFactory factory, int version) {

        super(context, name, factory, version);
        Log.d(msg, ":constructor callled");
    }

    // Called when no database exists in disk and the helper class needs
    // to create a new one.
    @Override
    public void onCreate(SQLiteDatabase _db) {
        Log.d(msg, ":onCreated called at Database");
        _db.execSQL(UserDatabase.getDatabaseStringHome());
    }

    // Called when there is a database version mismatch meaning that the version
    // of the database on disk needs to be upgraded to the current version.
    @Override
    public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {
        // Log the version upgrade.
        Log.w("TaskDBAdapter", "Upgrading from version " + _oldVersion + " to " + _newVersion + ", which will destroy all old data");
        Log.d(msg,"OnUpgrade....... called");
        // Upgrade the existing database to conform to the new version. Multiple
        // previous versions can be handled by comparing _oldVersion and _newVersion
        // values.
        // The simplest case is to drop the old table and create a new one.
        _db.execSQL("DROP TABLE IF EXISTS " + UserDatabase.getDatabaseStringHome());
        //_db.execSQL("DROP TABLE IF EXISTS " + "TEMPLATE");

        // Create a new one.
        onCreate(_db);
    }


}

