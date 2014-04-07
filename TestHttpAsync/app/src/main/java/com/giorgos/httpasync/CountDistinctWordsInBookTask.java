package com.giorgos.httpasync;

import android.os.Handler;
import android.os.Message;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by giorgos on 06/04/14.
 */
public class CountDistinctWordsInBookTask {

    public static final int MESSAGE_TYPE = 1;

    private String text;
    private MessageHandler messageHandler;

    public CountDistinctWordsInBookTask(String text, MessageHandler messageHandler) {
        this.text = text;
        this.messageHandler = messageHandler;
    }

    public void start() {

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                messageHandler.handleMessage(msg);
            }
        };

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Set<String> wordsSet = new HashSet<String>();
                for (String w : text.split(" ")) {
                    wordsSet.add(w);
                }
                Message msg = handler.obtainMessage(MESSAGE_TYPE, Integer.valueOf(wordsSet.size()));
                handler.sendMessage(msg);
            }
        };
        thread.start();
    }
}
