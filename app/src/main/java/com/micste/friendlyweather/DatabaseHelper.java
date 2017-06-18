package com.micste.friendlyweather;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



public class DatabaseHelper extends SQLiteOpenHelper {

    //TODO Finish up db test and put api data > table
    private static final String TAG = "DatabaseHelper";
    private static final String DATABASE_NAME = "FriendlyWeatherDB.db";
    private static final int DATABASE_VERSION = 1;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "Creating db [" + DATABASE_NAME + " v. " + DATABASE_VERSION + "]...");
        db.execSQL(DatabaseContract.LocationsEntry.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "Upgrading db [" + DATABASE_NAME + " v. " + oldVersion + "] to: [" + DATABASE_NAME + " v. " + newVersion + "]...");
        db.execSQL(DatabaseContract.LocationsEntry.SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void saveData(Weather weather) {
        if (exists(weather.getPlace())) {
            updateWeather(weather);
        } else {
            insertWeather(weather);
        }
    }

    public void insertWeather(Weather weather) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = weatherToContentValues(weather);

        long weatherId = db.insert(DatabaseContract.LocationsEntry.TABLE_NAME, null, values);
        Log.i(TAG, "Inserted new weather data into db with ID: " + weatherId);
        db.close();
    }

    public void updateWeather(Weather weather) {
        Log.d(TAG, "updateWeather method called");
    }

    private static ContentValues weatherToContentValues(Weather weather) {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.LocationsEntry.COLUMN_NAME_PLACE, weather.getPlace());
        values.put(DatabaseContract.LocationsEntry.COLUMN_NAME_LATITUDE, weather.getLatitude());
        values.put(DatabaseContract.LocationsEntry.COLUMN_NAME_LONGITUDE, weather.getLongitude());
        values.put(DatabaseContract.LocationsEntry.COLUMN_NAME_SUMMARY, weather.getSummary());
        values.put(DatabaseContract.LocationsEntry.COLUMN_NAME_ICON, weather.getIcon());
        values.put(DatabaseContract.LocationsEntry.COLUMN_NAME_TEMPERATURE, weather.getTemperature());
        values.put(DatabaseContract.LocationsEntry.COLUMN_NAME_HUMIDITY, weather.getHumidity());
        values.put(DatabaseContract.LocationsEntry.COLUMN_NAME_WINDSPEED, weather.getWindSpeed());

        return values;
    }

    public boolean exists(String valueToCheck) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {DatabaseContract.LocationsEntry.COLUMN_NAME_PLACE };
        String selection = DatabaseContract.LocationsEntry.COLUMN_NAME_PLACE + " =?";
        String[] selectionArgs = { valueToCheck };
        String limit = "1";

        Cursor cursor = db.query(DatabaseContract.LocationsEntry.TABLE_NAME, columns, selection, selectionArgs, null, null,
                null, limit);
        if (cursor.moveToFirst()) {
            db.close();
            return true;
        } else {
            db.close();
            return false;
        }
    }
}
