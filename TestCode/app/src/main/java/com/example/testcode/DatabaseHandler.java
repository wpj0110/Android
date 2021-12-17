package com.example.testcode;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHandlerTAG";

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    // DB Name
    private static final String DATABASE_NAME = "CovidDB";

    // DB Table Name
    private static final String TABLE_NAME = "CovidTable";

    ///DB Columns
    private static final String COUNTRY = "CountryName";
    private static final String CONFIRMED = "ConfirmedCount";
    private static final String RECOVERED = "RecoveredCount";
    private static final String CRITICAL = "CriticalCount";
    private static final String DEATHS = "DeathsCount";
    private static final String LASTCHANGE = "LastChange";
    private static final String LASTUPDATE = "LastUpdate";
    private final SQLiteDatabase database;

    // DB Table Create Code
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    CONFIRMED + " TEXT not null," +
                    COUNTRY + " TEXT not null unique," +
                    RECOVERED + "TEXT not null," +
                    CRITICAL + "TEXT not null," +
                    DEATHS + "TEXT not null," +
                    LASTCHANGE + "TEXT not null," +
                    LASTUPDATE + "TEXT not null)";

    DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = getWritableDatabase(); // Inherited from SQLiteOpenHelper
        Log.d(TAG, "DatabaseHandler: C'tor DONE");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // onCreate is only called is the DB does not exist
        Log.d(TAG, "onCreate: Making New DB");
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade: ");
    }

    public ArrayList<ArrayList<String>> loadCovid() {
        Log.d(TAG, "loadCovid: ");

        ArrayList<ArrayList<String>> stocks = new ArrayList<>();

        Cursor cursor = database.query(
                TABLE_NAME,  // The table to query
                new String[]{CONFIRMED, COUNTRY, RECOVERED, CRITICAL, DEATHS, LASTCHANGE, LASTUPDATE}, // The columns to return
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null); // The sort order

        if (cursor != null) {
            cursor.moveToFirst();

            for (int i = 0; i < cursor.getCount(); i++) {
                String confirmed = cursor.getString(0);
                String country = cursor.getString(1);
                String recovered = cursor.getString(2);
                String critical = cursor.getString(3);
                String deaths = cursor.getString(4);
                String lastChange = cursor.getString(5);
                String lastUpdate = cursor.getString(6);
                ArrayList<String> lst = new ArrayList<String>(){
                    {
                        add(confirmed);
                        add(country);
                        add(recovered);
                        add(critical);
                        add(deaths);
                        add(lastChange);
                        add(lastUpdate);
                    }
                };
                stocks.add(lst);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return stocks;
    }

    void addCountry(Covid countryCovid) {
        Log.d(TAG, "addCountry: ");
        try {
            ContentValues values = new ContentValues();

            values.put(CONFIRMED, countryCovid.getConfirmed());
            values.put(COUNTRY, countryCovid.getCountry());
            values.put(RECOVERED, countryCovid.getRecovered());
            values.put(CRITICAL, countryCovid.getCritical());
            values.put(DEATHS, countryCovid.getDeaths());
            values.put(LASTCHANGE, countryCovid.getLastChange());
            values.put(LASTUPDATE, countryCovid.getLastUpdate());

            long key = database.insert(TABLE_NAME, null, values);
        }catch (SQLiteConstraintException e){
            Log.d(TAG, "addStock: exception caught");
        }
    }

    void updateCountry(Covid countryCovid) {
        Log.d(TAG, "updateCountry: ");
        ContentValues values = new ContentValues();

        values.put(CONFIRMED, countryCovid.getConfirmed());
        values.put(COUNTRY, countryCovid.getCountry());
        values.put(RECOVERED, countryCovid.getRecovered());
        values.put(CRITICAL, countryCovid.getCritical());
        values.put(DEATHS, countryCovid.getDeaths());
        values.put(LASTCHANGE, countryCovid.getLastChange());
        values.put(LASTUPDATE, countryCovid.getLastUpdate());

        long numRows = database.update(TABLE_NAME, values, CONFIRMED + " = ?",
                new String[]{countryCovid.getCountry()} //is this an issue? remember
        );

    }

    void deleteCountry(String symbol) {
        Log.d(TAG, "deleteCountry: ");
        int cnt = database.delete(TABLE_NAME, CONFIRMED + " = ?",
                new String[]{symbol}
        );
    }

    void dumpDbToLog() {
        Log.d(TAG, "dumpDbToLog: ");
        Cursor cursor = database.rawQuery("select * from " + TABLE_NAME, null);
        if (cursor != null) {
            cursor.moveToFirst();

            Log.d(TAG, "dumpDbToLog: vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
            /*
            for (int i = 0; i < cursor.getCount(); i++) {
                String confirmed = cursor.getString(0);
                String country = cursor.getString(1);
                String recovered = cursor.getString(2);
                String critical = cursor.getString(3);
                String deaths = cursor.getString(4);
                String lastChange = cursor.getString(5);
                String lastUpdate = cursor.getString(6);
                Log.d(TAG, "dumpDbToLog: " +
                        String.format("%s %-18s", COUNTRY + ":", country) +
                        String.format("%s %-18s", CONFIRMED + ":", confirmed) +
                        String.format("%s %-18s", RECOVERED + ":", recovered) +
                        String.format("%s %-18s", CRITICAL + ":", critical) +
                        String.format("%s %-18s", DEATHS + ":", deaths) +
                        String.format("%s %-18s", LASTCHANGE + ":", lastChange) +
                        String.format("%s %-18s", LASTUPDATE + ":", lastUpdate));
                cursor.moveToNext();
            }

             */
            cursor.close();
        }

        Log.d(TAG, "dumpDbToLog: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
    }

    void shutDown() {
        database.close();
    }
}

