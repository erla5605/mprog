package com.example.sqlitedb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class PrimeNrDatabase {
    public static final long SMALLEST_PRIME_NR = 1;

    private static PrimeNrDatabase primeNrDatabase;

    private SQLiteDatabase database;

    // Returns an instance of the database, if it is null creates a new Database.
    public static PrimeNrDatabase getInstance(Context context) {
        if(primeNrDatabase == null) {
            primeNrDatabase = new PrimeNrDatabase(context);
        }

        return primeNrDatabase;
    }

    // Constructor for the database.
    private PrimeNrDatabase(Context context){
        database = new PrimeNrBaseHelper(context).getWritableDatabase();
    }

    // Gets all the primer numbers from the databse and returns them in a list.
    public List<PrimeNr> getAllPrimeNrs(){
        Cursor cursor = database.query(PrimeNrBaseHelper.TABLE,null, null, null, null
                , null, PrimeNrBaseHelper.PRIME_NR);

        List<PrimeNr> primeNrList = new ArrayList<>();

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                Long pnr = cursor.getLong(cursor.getColumnIndex(PrimeNrBaseHelper.PRIME_NR));
                String foundOn = cursor.getString(cursor.getColumnIndex(PrimeNrBaseHelper.FOUND_ON));
                primeNrList.add(new PrimeNr(pnr, foundOn));
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return primeNrList;
    }

    // Inserts a prime number to the database.
    public void insert(long primeNr){
        database.insert(PrimeNrBaseHelper.TABLE, null, getContentValues(primeNr));
    }

    // Creates the content values to be placed in the database.
    private ContentValues getContentValues(long primeNr){
        ContentValues values = new ContentValues();
        values.put(PrimeNrBaseHelper.PRIME_NR, primeNr);
        return values;
    }

    // Gets the highest saved primeNr from the database
    public long getHighestPrimeNr(){
        Cursor cursor = database.query(PrimeNrBaseHelper.TABLE, new String[] {"MAX("+PrimeNrBaseHelper.PRIME_NR+") AS MAX"},null,null,null
                ,null,null);

        long pnr = 0;

        try {
            cursor.moveToFirst();
            pnr = cursor.getLong(cursor.getColumnIndex("MAX"));
        } finally {
            cursor.close();
        }

        return (pnr > 0) ? pnr : SMALLEST_PRIME_NR;
    }
}
