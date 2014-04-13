package com.giorgos.services.app;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.*;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

/**
 * A trivial service that just displays a Toast after a few seconds.
 *
 * The purpose is to exercise the creation of a service by extending android.app.Service
 * and managing your own Thread for the background processing.
 *
 * The service uses a HandlerThread instead of a plain Thread as it facilitates the usage
 * of a Handler.
 */
public class ToastService extends Service {
    public static final String EXTRA_MESSAGE = "msg";
    private static final String LOG_TAG = ToastService.class.getName();

    private ServiceHandler handler;

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            final Context context = getApplicationContext();
            final String message = (String) msg.obj;

            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            long endTime = System.currentTimeMillis() + 5*1000;
            while (System.currentTimeMillis() < endTime) {
                synchronized (this) {
                    try {
                        wait(endTime - System.currentTimeMillis());
                    } catch (Exception e) {
                    }
                }
            }
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();

            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            Log.i(LOG_TAG, "Stopping " + msg.what);
            stopSelf(msg.what);
        }
    }


    public ToastService() {
    }


    @Override
    public void onCreate() {
        HandlerThread thread = new HandlerThread(ToastService.class.getName(), Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        handler = new ServiceHandler(thread.getLooper());
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(LOG_TAG, "onStartCommand(" + startId + ")");
        Message message = handler.obtainMessage(startId);
        message.obj = intent.getStringExtra(EXTRA_MESSAGE);
        handler.sendMessage(message);
        return START_NOT_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        Log.i(LOG_TAG, "onBind");
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(LOG_TAG, "onUnbind");
        return super.onUnbind(intent);
    }


    @Override
    public void onDestroy() {
        Log.i(LOG_TAG, "onDestroy");
        super.onDestroy();
    }


    private void startBackgroundTask(Intent intent, final int startId) {
        final Context context = getApplicationContext();
        final String message = intent.getStringExtra(EXTRA_MESSAGE);
        Thread thread = new Thread() {
            @Override
            public void run() {
                long endTime = System.currentTimeMillis() + 5*1000;
                while (System.currentTimeMillis() < endTime) {
                    synchronized (this) {
                        try {
                            wait(endTime - System.currentTimeMillis());
                        } catch (Exception e) {
                        }
                    }
                }
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                ToastService.this.stopSelf(startId);
            }
        };
        thread.start();
    }

}
