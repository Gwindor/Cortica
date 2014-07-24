package com.serhiisolodilov.corticaapp.io.model.request;


public class NoteRequest {
    private static final String TAG = NoteRequest.class.getSimpleName();

    private static final String POST_LAT = "latitude";
    private String mLatitude;
    private static final String POST_LON = "longitude";
    private String mLongitude;
    private static final String POST_MEDIA = "media";
    private byte[] mMedia;

    public String getLatitude() {
        return mLatitude;
    }

    public void setLatitude(String latitude) {
        mLatitude = latitude;
    }

    public String getLongitude() {
        return mLongitude;
    }

    public void setLongitude(String longitude) {
        mLongitude = longitude;
    }

    public byte[] getMedia() {
        return mMedia;
    }

    public void setMedia(byte[] media) {
        mMedia = media;
    }
}
