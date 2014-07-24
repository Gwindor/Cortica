package com.serhiisolodilov.corticaapp.io.model.request;


public class TransactionRequest {
    private String mTid;

    public TransactionRequest(String tid) {
        mTid = tid;
    }

    public String getTid() {
        return mTid;
    }
}
