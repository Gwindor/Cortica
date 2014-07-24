package com.serhiisolodilov.corticaapp.io.model.response.note;

import android.content.ContentValues;

import com.google.gson.annotations.SerializedName;
import com.serhiisolodilov.corticaapp.db.model.TransactionEntity;


public class NoteObject {

    @SerializedName("conceptId")
    private String mConceptId;
    @SerializedName("noteId")
    private String mNoteId;
    @SerializedName("noteText")
    private String mNoteText;
    @SerializedName("locationOnInImage_x")
    private String mLocationOnInImageX;
    @SerializedName("locationOnInImage_y")
    private String mLocationOnInImageY;
    @SerializedName("latitude")
    private String mLatitude;
    @SerializedName("longitude")
    private String mLongitude;
    @SerializedName("device_width")
    private String mDeviceWidth;
    @SerializedName("device_height")
    private String mDeviceHeight;

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TransactionEntity.COLUMN_CONCEPT_ID, mConceptId);
        contentValues.put(TransactionEntity.COLUMN_NOTE_ID, mNoteId);
        contentValues.put(TransactionEntity.COLUMN_NOTE_TEXT, mNoteText);
        contentValues.put(TransactionEntity.COLUMN_LOCATION_ON_INIMAGE_X, mLocationOnInImageX);
        contentValues.put(TransactionEntity.COLUMN_LOCATION_ON_INIMAGE_Y, mLocationOnInImageY);
        contentValues.put(TransactionEntity.COLUMN_LATITUDE, mLatitude);
        contentValues.put(TransactionEntity.COLUMN_LONGITUDE, mLongitude);
        contentValues.put(TransactionEntity.COLUMN_DEVICE_WIDTH, mDeviceWidth);
        contentValues.put(TransactionEntity.COLUMN_DEVICE_HEIGHT, mDeviceHeight);
        return contentValues;
    }
}
