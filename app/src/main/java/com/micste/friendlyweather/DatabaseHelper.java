package com.micste.friendlyweather;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


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
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = weatherToContentValues(weather);

        String selection = DatabaseContract.LocationsEntry.COLUMN_NAME_PLACE + " LIKE ?";
        String[] selectionArgs =  { weather.getPlace() };

        int count = db.update(
                DatabaseContract.LocationsEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );

        Log.i(TAG, "Updated weather where location name is: " + weather.getPlace());
    }

    public void deleteWeather(Weather weather) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = DatabaseContract.LocationsEntry.COLUMN_NAME_PLACE + " LIKE ?";
        String[] selectionArgs = { weather.getPlace() };

        db.delete(DatabaseContract.LocationsEntry.TABLE_NAME, selection, selectionArgs);
    }

    public List<Weather> getWeather() {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(DatabaseContract.LocationsEntry.TABLE_NAME, null, null, null, null, null, null);
        Log.i(TAG, "Loaded " + cursor.getCount() + " locations...");

        List<Weather> weatherList = new ArrayList<>();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Weather weather = cursorToWeather(cursor);
                weatherList.add(weather);
                cursor.moveToNext();
            }
            Log.i(TAG, "Weather locations loaded successfully...");
        }

        cursor.close();
        db.close();
        return weatherList;


    }

    private Weather cursorToWeather(Cursor cursor) {
        Weather weather = new Weather();
        weather.setPlace(cursor.getString(cursor.getColumnIndex(DatabaseContract.LocationsEntry.COLUMN_NAME_PLACE)));
        weather.setLatitude(cursor.getString(cursor.getColumnIndex(DatabaseContract.LocationsEntry.COLUMN_NAME_LATITUDE)));
        weather.setLongitude(cursor.getString(cursor.getColumnIndex(DatabaseContract.LocationsEntry.COLUMN_NAME_LONGITUDE)));
        weather.setSummary(cursor.getString(cursor.getColumnIndex(DatabaseContract.LocationsEntry.COLUMN_NAME_SUMMARY)));
        weather.setIcon(cursor.getString(cursor.getColumnIndex(DatabaseContract.LocationsEntry.COLUMN_NAME_ICON)));
        weather.setTemperature(cursor.getString(cursor.getColumnIndex(DatabaseContract.LocationsEntry.COLUMN_NAME_TEMPERATURE)));
        weather.setHumidity(cursor.getString(cursor.getColumnIndex(DatabaseContract.LocationsEntry.COLUMN_NAME_HUMIDITY)));
        weather.setWindSpeed(cursor.getString(cursor.getColumnIndex(DatabaseContract.LocationsEntry.COLUMN_NAME_WINDSPEED)));

        return weather;
    }

    private ContentValues weatherToContentValues(Weather weather) {
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

    private boolean exists(String valueToCheck) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {DatabaseContract.LocationsEntry.COLUMN_NAME_PLACE };
        String selection = DatabaseContract.LocationsEntry.COLUMN_NAME_PLACE + " =?";
        String[] selectionArgs = { valueToCheck };
        String limit = "1";

        Cursor cursor = db.query(DatabaseContract.LocationsEntry.TABLE_NAME, columns, selection, selectionArgs, null, null,
                null, limit);
        if (cursor.moveToFirst()) {
            cursor.close();
            db.close();
            return true;
        } else {
            cursor.close();
            db.close();
            return false;
        }
    }
}
