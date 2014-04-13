package com.giorgos.services.app;

import android.content.Intent;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    private EditText messageText;
    private Button startBasicButton;
    private Button getRandomButton;
    private EditText rangeText;


    private class RandomResultReceiver extends ResultReceiver {
        /**
         * Create a new ResultReceive to receive results.  Your
         * {@link #onReceiveResult} method will be called from the thread running
         * <var>handler</var> if given, or from an arbitrary thread if null.
         *
         * @param handler
         */
        public RandomResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            Toast.makeText(MainActivity.this, Float.toString(resultData.getFloat(RandomNumbersService.EXTRAS_RESULT)), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messageText = (EditText) findViewById(R.id.editText);
        startBasicButton = (Button) findViewById(R.id.basicServiceButton);
        rangeText = (EditText) findViewById(R.id.rangeText);
        getRandomButton = (Button) findViewById(R.id.randomServiceBtn);

        getRandomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Float range = Float.valueOf(rangeText.getText().toString());
                Intent intent = RandomNumbersService.GetIntentForActionRand(MainActivity.this, range);
                intent.putExtra(RandomNumbersService.EXTRAS_RECEIVER, getRandomNumberReceiver());
                startService(intent);
            }
        });

        startBasicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent basicServiceIntent = new Intent(MainActivity.this, ToastService.class);
                basicServiceIntent.putExtra(ToastService.EXTRA_MESSAGE, messageText.getText().toString());
                startService(basicServiceIntent);
            }
        });
    }

    private ResultReceiver getRandomNumberReceiver() {
        Handler handler = new Handler(getMainLooper());
        return new RandomResultReceiver(handler);
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

}
