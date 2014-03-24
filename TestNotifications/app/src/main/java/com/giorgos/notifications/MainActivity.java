package com.giorgos.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * Notifications are created through the Notification.Builder object which returns a Notification
 * object.
 * They can have an associated action in the form of a pending Intent which usually
 * invokes one activity of the application. The intent is set through the method
 * Notification.Builder.setContentIntent.
 * <p/>
 * A Notification should have at least a title, a content text and an icon. These are set through
 * Notification.Builder.setContentTitle, Notification.Builder.setContentText and
 * Notification.Builder.setSmallIcon respectively.
 * <p/>
 * The text that appears on the top of the screen when a notification first appears is called a ticker.
 * You can set the ticker text through Notification.setTicker().
 * <p/>
 * Additional useful field is the content info, which appears at the right side of the notification
 * body, right under the time that the notification was issued.
 * <p/>
 * It is possible to remove a notification automatically once the user selects it. Use the method
 * Notification.Builder.setAutoCancel().
 * <p/>
 * When you start an Activity from a notification, you must preserve the user's expected navigation
 * experience. Clicking Back should take the user back through the application's normal work flow
 * to the Home screen, and clicking Recents should show the Activity as a separate task. To preserve
 * the navigation experience, you should start the Activity in a fresh task. This is demonstrated in
 * this sample.
 * <p/>
 * Notifications can also be expanded by the user revealing a bigger view, if it has been defined.
 * You can set the title of the big view through the Notification.Builder.setBigContentTitle() method.
 * It is also necessary to set the style of the big area. The following are supported:
 * <ul>
 *      <li>
 *      Big picture style
 *      The details area contains a bitmap up to 256 dp tall in its detail section.
 *      </li>
 *
 *      <li>
 *      Big text style
 *      Displays a large text block in the details section.
 *      </li>
 *
 *      <li>
 *      Inbox style
 *      Displays lines of text in the details section.
 *      </li>
 *  </ul>
 * <p/>
 * All of the big view styles also have the following content options that aren't available in normal view:
 * Big content title
 * Allows you to override the normal view's content title with a title that appears only in the expanded view.
 * <p/>
 * Summary text
 * Allows you to add a line of text below the details area.
 * <p/>
 * For Android versions older than 3, use the NotificationCompat.Builder class in the version 4
 * Support Library. For Android 3.0+m use the class Notification.Builder.
 */
public class MainActivity extends ActionBarActivity {

    private static final String NUM_RESULTS_KEY = "notifications";

    private int numResults = 0;

    private int notificationId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numResults++;
                issueNotification();
            }
        });
    }

    private void issueNotification() {

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // Add the parent activity, if any, to the back stack
        //stackBuilder.addParentStack(ParentActivity.class);

        // Adds the Intent to the top of the stack
        stackBuilder.addNextIntent(resultIntent);

        // Gets a PendingIntent containing the entire back stack
        PendingIntent pendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Sets options for the big view
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        // Sets a title for the Inbox style big view
        inboxStyle.setBigContentTitle("Results details:");
        for (int i=0; i < numResults; i++) {
            inboxStyle.addLine("Result #" + i);
        }

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.abc_ic_search)
                        .setTicker("Search results available!")
                        .setContentTitle("Search finished")
                        .setContentText("There are results for your search")
                        .setAutoCancel(true)
                        .setStyle(inboxStyle)
                        .setContentIntent(pendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // notificationId allows you to re-use and update the notification later on
        // without passing the same id value, the user would see distinct notifications
        // each time you were to call this method
        // The setNumber method below sets the number that indicates the amount of events
        // summed by this notification
        mBuilder.setNumber(numResults);
        mNotificationManager.notify(notificationId, mBuilder.build());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        numResults = savedInstanceState.getInt(NUM_RESULTS_KEY);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(NUM_RESULTS_KEY, numResults);
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
