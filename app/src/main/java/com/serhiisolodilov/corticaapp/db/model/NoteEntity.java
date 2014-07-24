package com.serhiisolodilov.corticaapp.db.model;

import android.provider.BaseColumns;

public class NoteEntity implements BaseColumns {

    public static final String TABLE_NAME = "notes";

    public static final String COLUMN_LAT = "lat";
    public static final String COLUMN_LON = "lon";
    public static final String COLUMN_MEDIA_PATH = "path";
    public static final String COLUMN_TID = "tid";

    public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            _ID + " INTEGER PRIMARY KEY," +
            COLUMN_LAT + " TEXT," +
            COLUMN_LON + " TEXT," +
            COLUMN_TID + " TEXT," +
            COLUMN_MEDIA_PATH + " TEXT" + " )";

}
