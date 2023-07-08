package com.example.woofwisdomapplication;

import android.database.sqlite.SQLiteOpenHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "breedInfo.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // onCreate is called when the database is created for the first time
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BREEDS_TABLE = "CREATE TABLE breedInfo ("
                + "breedName TEXT PRIMARY KEY,"
                + "breedDescription TEXT,"
                + "breedType TEXT"
                // Add more columns for other properties of the breed
                + ")";
        db.execSQL(CREATE_BREEDS_TABLE);
    }

    // onUpgrade is called when the database needs to be upgraded,
    // such as when you make changes to the database schema
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS breedInfo"); // Delete the existing table
        onCreate(db); // Call onCreate to recreate the table
    }
}


