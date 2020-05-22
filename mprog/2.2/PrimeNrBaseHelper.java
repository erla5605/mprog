package com.example.sqlitedb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PrimeNrBaseHelper  extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "primeNr.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE = "primenumbers";
    public static final String PRIME_NR = "primenumber";
    public static final String FOUND_ON = "found_on";

    // Constructor for the helper
    public PrimeNrBaseHelper(Context context){
        super(context, DATABASE_NAME, null ,DATABASE_VERSION);
    }

    // Executes the SQL statement to set up the database.
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table " + TABLE + "(" +
                PRIME_NR + " integer primary key not null, " +
                FOUND_ON + " timestamp not null default current_timestamp" + ");" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // Not used.
    }
}
