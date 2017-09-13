package com.tloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.ArrayList;


public class ImageDownloaderThread extends AsyncTask {

    private static final String TAG = ImageDownloaderThread.class.getName();
    private ArrayList chosenAvatarUrls;
    private String[] imagePaths;
    public OnResponseListener onResponseListener;

    public ImageDownloaderThread(ArrayList chosenAvatarUrls) {
        this.chosenAvatarUrls = chosenAvatarUrls;
        imagePaths = new String[chosenAvatarUrls.size()];
    }

    @Override
    protected Object doInBackground(Object[] params) {
        // Download and save the images if the external storage is available.
        if (isExternalStorageWritable()) {
            for (int i = 0; i < chosenAvatarUrls.size(); i++) {
                InputStream inputStream = null;
                java.net.URL url = null;
                try {
                    url = new java.net.URL(chosenAvatarUrls.get(i).toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                try {
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    inputStream = connection.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                FileOutputStream out = null;
                try {
                    String filePath = Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DOWNLOADS) + "/avatar" + i + ".png";
                    out = new FileOutputStream(filePath);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

                    imagePaths[i] = filePath;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Object result) {
        onResponseListener.onImagesInteractionComplete(imagePaths);
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public interface OnResponseListener {
        void onImagesInteractionComplete(String[] imagePaths);
    }
}
