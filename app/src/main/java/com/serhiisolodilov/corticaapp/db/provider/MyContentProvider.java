package com.serhiisolodilov.corticaapp.db.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

import com.serhiisolodilov.corticaapp.db.helper.DBHelper;
import com.serhiisolodilov.corticaapp.db.model.NoteEntity;
import com.serhiisolodilov.corticaapp.db.model.TransactionEntity;

public class MyContentProvider extends ContentProvider {
    public static final String AUTHORITY = "com.serhiisolodilov.corticaapp.provider";
    public static final Uri NOTES_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + NoteEntity.TABLE_NAME);
    public static final Uri TRANSACTIONS_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + TransactionEntity.TABLE_NAME);

    private static final int NOTE = 1;
    private static final int NOTE_ID = 2;
    private static final int TRANSACTION = 3;
    private static final int TRANSACTION_ID = 4;

    private final UriMatcher mUriMatcher;

    private DBHelper mDBHelper;

    public MyContentProvider() {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(AUTHORITY, NoteEntity.TABLE_NAME, NOTE);
        mUriMatcher.addURI(AUTHORITY, NoteEntity.TABLE_NAME + "/#", NOTE_ID);
        mUriMatcher.addURI(AUTHORITY, TransactionEntity.TABLE_NAME, TRANSACTION);
        mUriMatcher.addURI(AUTHORITY, TransactionEntity.TABLE_NAME + "/#", TRANSACTION_ID);
    }

    @Override
    public boolean onCreate() {
        mDBHelper = DBHelper.getInstance(getContext());
        return false;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public synchronized Uri insert(Uri uri, ContentValues values) {
        String tableName;
        switch (mUriMatcher.match(uri)) {
            case NOTE:
                tableName = NoteEntity.TABLE_NAME;
                break;

            case TRANSACTION:
                tableName = TransactionEntity.TABLE_NAME;
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase sqLiteDatabase = mDBHelper.getWritableDatabase();
        long rowId = sqLiteDatabase.insert(tableName, null, values);
        if (rowId > 0) {
            Uri noteUri = ContentUris.withAppendedId(uri, rowId);
            getContext().getContentResolver().notifyChange(noteUri, null);
            return noteUri;
        }
        throw new SQLException("Failed to insert row into " + uri);
    }


    @Override
    public synchronized Cursor query(Uri uri, String[] projection, String selection,
                                     String[] selectionArgs, String sortOrder) {
        String table;
        switch (mUriMatcher.match(uri)) {
            case NOTE:
                table = NoteEntity.TABLE_NAME;
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = BaseColumns._ID + " ASC";
                }
                break;
            case NOTE_ID:
                table = NoteEntity.TABLE_NAME;
                String noteId = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = BaseColumns._ID + " = " + noteId;
                } else {
                    selection = selection + " AND " + BaseColumns._ID + " = " + noteId;
                }
                break;
            case TRANSACTION:
                table = TransactionEntity.TABLE_NAME;
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = BaseColumns._ID + " ASC";
                }
                break;
            case TRANSACTION_ID:
                table = TransactionEntity.TABLE_NAME;
                String transactrionId = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = BaseColumns._ID + " = " + transactrionId;
                } else {
                    selection = selection + " AND " + BaseColumns._ID + " = " + transactrionId;
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        Cursor cursor = mDBHelper.getReadableDatabase().query(table, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public synchronized int update(Uri uri, ContentValues values, String selection,
                                   String[] selectionArgs) {
        String tableName;
        switch (mUriMatcher.match(uri)) {
            case NOTE_ID:
                tableName = NoteEntity.TABLE_NAME;
                break;
            case TRANSACTION_ID:
                tableName = TransactionEntity.TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        String id = uri.getLastPathSegment();
        selection = BaseColumns._ID + " = " + id;
        return mDBHelper.getWritableDatabase().update(tableName, values, selection, selectionArgs);
    }
}
