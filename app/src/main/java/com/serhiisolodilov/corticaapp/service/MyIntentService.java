package com.serhiisolodilov.corticaapp.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Handler;
import android.util.Base64;

import com.serhiisolodilov.corticaapp.db.model.NoteEntity;
import com.serhiisolodilov.corticaapp.db.model.TransactionEntity;
import com.serhiisolodilov.corticaapp.db.provider.MyContentProvider;
import com.serhiisolodilov.corticaapp.io.IOHelper;
import com.serhiisolodilov.corticaapp.io.model.request.NoteRequest;
import com.serhiisolodilov.corticaapp.io.model.request.TransactionRequest;
import com.serhiisolodilov.corticaapp.io.model.response.NoteResponse;
import com.serhiisolodilov.corticaapp.io.model.response.TransactionResponse;
import com.serhiisolodilov.corticaapp.io.model.response.note.NoteObject;
import com.serhiisolodilov.corticaapp.utils.ImageUtils;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MyIntentService extends IntentService {
    private static final String TAG = MyIntentService.class.getSimpleName();

    private static final String ACTION_NOTES = "com.serhiisolodilov.corticaapp.service.action.NOTES";
    private static final String EXTRA_PARAM_NOTES_IMAGE_PATH = "com.serhiisolodilov.corticaapp.service.extra.PARAM_NOTES_IMAGE_PATH";
    private static final String EXTRA_PARAM_NOTES_IMAGE_LOCATION = "com.serhiisolodilov.corticaapp.service.extra.PARAM_NOTES_IMAGE_LOCATION";

    private static final String ACTION_TRANSACTION = "com.serhiisolodilov.corticaapp.service.action.TRANSACTION";
    private static final String EXTRA_PARAM_TRANSACTION_TID = "com.serhiisolodilov.corticaapp.service.extra.PARAM_TRANSACTION_TID";


    private static final int REQUARED_WIDTH = 300;
    private static final int REQUARED_HEIGHT = 300;
    private static final int REQUARED_QUALITY = 100;

    private static final int TIMEOUT_BEFORE_GET_TRANSACTION = 5000;

    public static void startActionNotes(Context context, String imagePath, Location location) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_NOTES);
        intent.putExtra(EXTRA_PARAM_NOTES_IMAGE_PATH, imagePath);
        intent.putExtra(EXTRA_PARAM_NOTES_IMAGE_LOCATION, location);
        context.startService(intent);
    }

    public static void startTransactionNotes(Context context, String tid) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_TRANSACTION);
        intent.putExtra(EXTRA_PARAM_TRANSACTION_TID, tid);
        context.startService(intent);
    }

    private Handler mHandler;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mHandler = new Handler();
        return super.onStartCommand(intent, flags, startId);
    }

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_NOTES.equals(action)) {
                final String imagePath = intent.getStringExtra(EXTRA_PARAM_NOTES_IMAGE_PATH);
                final Location location = intent.getParcelableExtra(EXTRA_PARAM_NOTES_IMAGE_LOCATION);
                handleActionNotes(imagePath, location);
            } else if (ACTION_TRANSACTION.equals(action)) {
                final String tid = intent.getStringExtra(EXTRA_PARAM_TRANSACTION_TID);
                handleActionTransaction(tid);
            }
        }
    }

    private void handleActionNotes(String imagePath, Location location) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NoteEntity.COLUMN_MEDIA_PATH, imagePath);
        if (location != null) {
            contentValues.put(NoteEntity.COLUMN_LAT, String.valueOf(location.getLatitude()));
            contentValues.put(NoteEntity.COLUMN_LON, String.valueOf(location.getLongitude()));
        }
        Uri insertedUri = getContentResolver().insert(MyContentProvider.NOTES_CONTENT_URI, contentValues);
        NoteRequest noteRequest = new NoteRequest();
        if (location != null) {
            noteRequest.setLatitude(String.valueOf(location.getLatitude()));
            noteRequest.setLongitude(String.valueOf(location.getLongitude()));
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        options.inSampleSize = ImageUtils.calculateInSampleSize(options, REQUARED_WIDTH, REQUARED_HEIGHT);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, REQUARED_QUALITY, baos); //bm is the bitmap object
        byte[] bytes = baos.toByteArray();
        noteRequest.setMedia(Base64.encode(bytes, Base64.NO_WRAP));
        final TransactionResponse transactionResponse = IOHelper.sendNoteMime(noteRequest);
        if (transactionResponse != null) {
            ContentValues updateContentValues = new ContentValues();
            updateContentValues.put(NoteEntity.COLUMN_TID, transactionResponse.getTid());
            getContentResolver().update(insertedUri, updateContentValues, null, null);

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    MyIntentService.startTransactionNotes(getApplicationContext(), transactionResponse.getTid());
                }
            }, TIMEOUT_BEFORE_GET_TRANSACTION);

        }
    }

    private void handleActionTransaction(String tid) {
        TransactionRequest transactionRequest = new TransactionRequest(tid);
        final NoteResponse noteResponse = IOHelper.getTransaction(transactionRequest);
        if (noteResponse == null) {
            return;
        }
        if (noteResponse.getNotes().size() > 0) {
            List<ContentValues> contentValueses = new ArrayList<ContentValues>();
            for (NoteObject noteObject : noteResponse.getNotes()) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(TransactionEntity.COLUMN_TID, tid);
                contentValues.putAll(noteObject.toContentValues());
                contentValueses.add(contentValues);
            }
            ContentValues[] contentValuesesArray = new ContentValues[contentValueses.size()];
            getContentResolver().bulkInsert(MyContentProvider.TRANSACTIONS_CONTENT_URI, contentValueses.toArray(contentValuesesArray));
        }

    }

}
