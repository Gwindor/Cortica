package com.serhiisolodilov.corticaapp.io.model.response;

import com.google.gson.annotations.SerializedName;
import com.serhiisolodilov.corticaapp.io.model.response.note.NoteObject;

import java.util.ArrayList;


public class NoteResponse {
    @SerializedName("tid")
    private String mTid;
    @SerializedName("notes")
    private ArrayList<NoteObject> mNotes;

    public String getTid() {
        return mTid;
    }

    public ArrayList<NoteObject> getNotes() {
        return mNotes;
    }
}
