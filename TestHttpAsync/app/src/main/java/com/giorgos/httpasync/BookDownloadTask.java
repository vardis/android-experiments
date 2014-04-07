package com.giorgos.httpasync;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by giorgos on 06/04/14.
 */
public class BookDownloadTask extends AsyncTask<String, Float, String> {
    private final static String LOG_TAG = BookDownloadTask.class.getName();

    private final static Integer BOOK_SIZE = 704139;
    private final DownloadListener<Float, String> listener;

    public BookDownloadTask(DownloadListener<Float, String> listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection conn = null;
        try {
            URL bookUrl = new URL(params[0]);

            // we don't know the size of the book...
            conn = (HttpURLConnection) bookUrl.openConnection();
            conn.setChunkedStreamingMode(0);

            return readStream(conn.getInputStream());
        } catch (java.io.IOException e) {
            Log.e(LOG_TAG, "Failed to download book", e);
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    @Override
    protected void onPostExecute(String s) {
        listener.onDownloadComplete(s);
    }

    @Override
    protected void onProgressUpdate(Float... values) {
        listener.onProgressUpdate(values[0]);
    }

    /**
     * Will publish a progress update for every 1KB of book data downloaded.
     *
     * @param in
     * @return
     * @throws IOException
     */
    private String readStream(InputStream in) throws IOException {
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        float totalBytes = 0.0f;
        int bytesTransferred = 0;
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                bytesTransferred += line.length();
                if (bytesTransferred > 1024) {
                    totalBytes += bytesTransferred;
                    bytesTransferred = 0;
                    publishProgress(100.0f * totalBytes / BOOK_SIZE);
                }
                sb.append(line);
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return sb.toString();
    }
}
