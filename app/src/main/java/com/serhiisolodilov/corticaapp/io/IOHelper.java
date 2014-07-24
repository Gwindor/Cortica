package com.serhiisolodilov.corticaapp.io;

import android.net.http.AndroidHttpClient;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.serhiisolodilov.corticaapp.io.model.request.NoteRequest;
import com.serhiisolodilov.corticaapp.io.model.request.TransactionRequest;
import com.serhiisolodilov.corticaapp.io.model.response.NoteResponse;
import com.serhiisolodilov.corticaapp.io.model.response.TransactionResponse;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicHeader;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class IOHelper {
    private static final String TAG = IOHelper.class.getSimpleName();

    private static final String HOST = "http://api.cortica.com:8081/";
    private static final String NOTES = HOST + "notes";
    private static final String TRANSACTIONS = HOST + "transactions/%s";

    private static final String LOGIN = "notediphoneapp";
    private static final String PASSWORD = "cortica123";

    private static HttpClient sAndroidHttpClient = AndroidHttpClient.newInstance(null);

    private static String AUTH_HEADER;

    private static String getAuthHeader(String login, String password) {
        return "Basic " + Base64.encodeToString((login + ":" + password).getBytes(), Base64.NO_WRAP);
    }

//    private static Header[] getNoteHeaders() {
//        List<Header> headers = new ArrayList<Header>();
//        Header contentHeader = new BasicHeader("Content-Type", "multipart/form-data");
//        headers.add(contentHeader);
//        if (AUTH_HEADER == null) {
//            AUTH_HEADER = getAuthHeader(LOGIN, PASSWORD);
//        }
//        Header authHeader = new BasicHeader("Authorization", AUTH_HEADER);
//        headers.add(authHeader);
//        return headers.toArray(new Header[headers.size()]);
//    }

    private static Header[] getTransactionHeaders() {
        List<Header> headers = new ArrayList<Header>();
        Header contentHeader = new BasicHeader("Content-Type", "application/json");
        headers.add(contentHeader);
        if (AUTH_HEADER == null) {
            AUTH_HEADER = getAuthHeader(LOGIN, PASSWORD);
        }
        Header authHeader = new BasicHeader("Authorization", AUTH_HEADER);
        headers.add(authHeader);
        return headers.toArray(new Header[headers.size()]);
    }

    public static TransactionResponse sendNoteMime(NoteRequest noteRequest) {
        if (noteRequest == null) {
            return null;
        }
        final String attachmentName = "media";
        final String attachmentFileName = "image.jpg";
        final String crlf = "\r\n";
        final String twoHyphens = "--";
        final String boundary = "*****";

        HttpURLConnection httpUrlConnection;

        URL url = null;
        try {
            if (noteRequest.getLatitude() == null || noteRequest.getLongitude() == null) {
                url = new URL(NOTES);
            } else {
                url = new URL(NOTES + "?latitude=" + noteRequest.getLatitude() + ",longitude=" + noteRequest.getLongitude());
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
        if (url == null) {
            return null;
        }
        try {
            httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setRequestProperty("Authorization", getAuthHeader(LOGIN, PASSWORD));

            httpUrlConnection.setUseCaches(false);
            httpUrlConnection.setDoOutput(true);

            httpUrlConnection.setRequestMethod("POST");
            httpUrlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            DataOutputStream request = new DataOutputStream(httpUrlConnection.getOutputStream());
            //media
            request.writeBytes(twoHyphens + boundary + crlf);
            request.writeBytes("Content-Disposition: form-data; name=\"" + attachmentName + "\";filename=\"" + attachmentFileName + "\"" + crlf);
            request.writeBytes("Content-Type: image/jpeg" + crlf);
            request.writeBytes("Content-Transfer-Encoding: base64" + crlf);

            request.writeBytes(crlf);

            request.write(noteRequest.getMedia());

            request.writeBytes(crlf);
            request.writeBytes(twoHyphens + boundary + twoHyphens + crlf);

            request.flush();
            request.close();

            int responseCode = httpUrlConnection.getResponseCode();
            if (responseCode == 200) {
                InputStream inputStream = httpUrlConnection.getInputStream();
                Gson gson = new Gson();
                Reader reader = new InputStreamReader(inputStream, "UTF-8");
                try {
                    TransactionResponse transactionResponse = gson.fromJson(reader, TransactionResponse.class);
                    Log.d(TAG, transactionResponse.getTid());
                    return transactionResponse;
                } finally {
                    reader.close();
                }
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static NoteResponse getTransaction(TransactionRequest transactionRequest) {
        HttpGet httpGet = new HttpGet(String.format(TRANSACTIONS, transactionRequest.getTid()));
        httpGet.setHeaders(getTransactionHeaders());
        HttpResponse response;
        try {
            response = sAndroidHttpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                return null;
            }
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return null;
            }
            Gson gson = new Gson();
            Reader reader = new InputStreamReader(entity.getContent(), "UTF-8");
            try {
                return gson.fromJson(reader, NoteResponse.class);
            } finally {
                reader.close();
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
