package com.micste.friendlyweather;

import android.provider.BaseColumns;

public final class DatabaseContract {

    private DatabaseContract() {
        //private to prevent accidental instantiation
    }

    public static class LocationsEntry implements BaseColumns {
        public static final String TABLE_NAME = "locations";
        public static final String COLUMN_NAME_PLACE = "place";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
        public static final String COLUMN_NAME_SUMMARY = "summary";
        public static final String COLUMN_NAME_ICON = "icon";
        public static final String COLUMN_NAME_TEMPERATURE = "temperature";
        public static final String COLUMN_NAME_HUMIDITY = "humidity";
        public static final String COLUMN_NAME_WINDSPEED = "windspeed";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY," +
                        COLUMN_NAME_PLACE + " TEXT," +
                        COLUMN_NAME_LATITUDE + " TEXT," +
                        COLUMN_NAME_LONGITUDE + " TEXT," +
                        COLUMN_NAME_SUMMARY + " TEXT," +
                        COLUMN_NAME_ICON + " TEXT," +
                        COLUMN_NAME_TEMPERATURE + " TEXT," +
                        COLUMN_NAME_HUMIDITY + " TEXT," +
                        COLUMN_NAME_WINDSPEED + " TEXT)";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
