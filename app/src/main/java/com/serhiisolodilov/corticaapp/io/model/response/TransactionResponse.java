package com.serhiisolodilov.corticaapp.io.model.response;

import com.google.gson.annotations.SerializedName;


public class TransactionResponse {
    @SerializedName("tid")
    private String mTid;

    public String getTid() {
        return mTid;
    }
}
