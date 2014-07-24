package com.serhiisolodilov.corticaapp.db.model;

import android.provider.BaseColumns;

public class TransactionEntity implements BaseColumns {

    public static final String TABLE_NAME = "transactions";

    public static final String COLUMN_TID = "tid";
    public static final String COLUMN_CONCEPT_ID = "conceptId";
    public static final String COLUMN_NOTE_ID = "noteId";
    public static final String COLUMN_NOTE_TEXT = "noteText";
    public static final String COLUMN_LOCATION_ON_INIMAGE_X = "locationOnInImage_x";
    public static final String COLUMN_LOCATION_ON_INIMAGE_Y = "locationOnInImage_y";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_DEVICE_WIDTH = "device_width";
    public static final String COLUMN_DEVICE_HEIGHT = "device_height";


    public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            _ID + " INTEGER PRIMARY KEY," +
            COLUMN_TID + " TEXT," +
            COLUMN_CONCEPT_ID + " TEXT," +
            COLUMN_NOTE_ID + " TEXT," +
            COLUMN_NOTE_TEXT + " TEXT," +
            COLUMN_LOCATION_ON_INIMAGE_X + " REAL," +
            COLUMN_LOCATION_ON_INIMAGE_Y + " REAL," +
            COLUMN_LATITUDE + " REAL," +
            COLUMN_LONGITUDE + " REAL," +
            COLUMN_DEVICE_WIDTH + " INTEGER," +
            COLUMN_DEVICE_HEIGHT + " INTEGER" + " )";
}
