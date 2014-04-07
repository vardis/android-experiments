package com.giorgos.httpasync;

import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * This sample has a two-fold purpose. From one side it demonstrates the usage of the
 * HTTPUrlConnection for downloading resources from the network. On the other side, it
 * demonstrates the usage of AsyncTask and Handler for performing long-running operations
 * on a background thread and notifying the UI thread with the results of the operations.
 *
 * The sample works by downloading a book from project Gutenberg using an AsyncTask subclass.
 * Once the book is downloaded, a new async task is spawn but this time using a combination
 * of Thread and Handler. The job of this task is to count the distinct words that appear in
 * the book.
 *
 * The UI of the activity contains a single button that starts the process and a progress bar
 * for displaying the progress of the background tasks
 */
public class MainActivity extends ActionBarActivity implements DownloadListener<Float, String>, MessageHandler {

    private ProgressBar progressBar;
    private Button startButton;
    private TextView resultsText;
    private TextView statusText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        startButton = (Button) findViewById(R.id.button);
        resultsText = (TextView) findViewById(R.id.resultsText);
        statusText = (TextView) findViewById(R.id.statusText);

        final String bookUrl = getResources().getString(R.string.book_url);
        final String downloadStatus = getResources().getString(R.string.download_status);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusText.setText(downloadStatus);
                BookDownloadTask downloadTask = new BookDownloadTask(MainActivity.this);
                downloadTask.execute(bookUrl);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onProgressUpdate(Float p) {
        Log.i(MainActivity.class.getName(), "Got update " + p.intValue());
        progressBar.setProgress(p.intValue());
    }

    @Override
    public void onDownloadComplete(String res) {
        progressBar.setProgress(progressBar.getMax());

        final String countStatus = getResources().getString(R.string.counting_status);
        statusText.setText(countStatus);

        CountDistinctWordsInBookTask countTask = new CountDistinctWordsInBookTask(res, this);
        countTask.start();
    }

    @Override
    public void handleMessage(Message msg) {
        if (msg.what == CountDistinctWordsInBookTask.MESSAGE_TYPE) {
            Integer count = (Integer) msg.obj;
            resultsText.setText(count.toString());
        }
    }
}
