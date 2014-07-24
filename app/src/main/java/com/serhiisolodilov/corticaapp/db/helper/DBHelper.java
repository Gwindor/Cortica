package com.serhiisolodilov.corticaapp.db.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.serhiisolodilov.corticaapp.db.model.NoteEntity;
import com.serhiisolodilov.corticaapp.db.model.TransactionEntity;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "cortica";
    private static final int DB_VERSION = 12;

    private static DBHelper sDBHelper;

    public static synchronized DBHelper getInstance(Context context) {
        if (sDBHelper == null) {
            sDBHelper = new DBHelper(context);
        }
        return sDBHelper;
    }

    private DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(NoteEntity.SQL_CREATE_TABLE);
        db.execSQL(TransactionEntity.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NoteEntity.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TransactionEntity.TABLE_NAME);
        onCreate(db);
    }
}
