package com.tloader;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class JsonDownloaderThread extends AsyncTask {

    private static final String TAG = JsonDownloaderThread.class.getName();
    private static final String requestURL = "https://api.github.com/repos/vmg/redcarpet/issues?state=closed";
    private JSONArray jsonArray;

    public OnResponseListener onResponseListener;

    @Override
    protected Object doInBackground(Object[] params) {
        String jsonData = null;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(requestURL)
                .build();
        Response response;

        try {
            response = client.newCall(request).execute();
            jsonData = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            jsonArray = new JSONArray(jsonData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Object result) {
        onResponseListener.onJsonDownloadComplete(jsonArray);
    }

    public interface OnResponseListener {
        void onJsonDownloadComplete(JSONArray jsonArray);
    }
}
