package com.giorgos.services.app;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.os.ResultReceiver;

import java.util.Random;

/**
 * A random number generator service.
 *
 * This service demonstrates how to pass a result to the caller. The communication
 * protocol is as follows:
 * <ul>
 *     <li>The client creates an instance of a android.os.ResultReceiver
 *     and stores it in the Intent, under the key RandomNumbersService.EXTRAS_RECEIVER
 *     </li>
 *     <li>The client can also specify the random range by passing a float number
 *     as an extra within the Intent
 *     </li>
 *     <li>
 *         Once the result has been generated, the service extracts the ResultReceiver
 *         instance from the calling Intent and send the results over by invoking the
 *         ResultReceiver.send(int, Bundle) method.
 *     </li>
 * </ul>
 */
public class RandomNumbersService extends IntentService {
    public static final String EXTRAS_RESULT = "com.giorgos.services.app.extra.EXTRAS_RESULT";
    public static final String EXTRAS_RECEIVER = "com.giorgos.services.app.extra.EXTRAS_RECEIVER";

    private static final String ACTION_RAND = "com.giorgos.services.app.action.RAND";
    private static final String RANGE = "com.giorgos.services.app.extra.RANGE";

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static Intent GetIntentForActionRand(Context context, float range) {
        Intent intent = new Intent(context, RandomNumbersService.class);
        intent.setAction(ACTION_RAND);
        intent.putExtra(RANGE, range);
        return intent;
    }

    public RandomNumbersService() {
        super("RandomNumbersService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_RAND.equals(action)) {
                final float range = intent.getFloatExtra(RANGE, 1.0f);
                float res = generateRandomNumber(range);

                Bundle bundle = new Bundle();
                bundle.putFloat(EXTRAS_RESULT, res);
                ResultReceiver receiver = (ResultReceiver) intent.getExtras().get(EXTRAS_RECEIVER);
                receiver.send(1, bundle);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private float generateRandomNumber(float range) {
        Random rand = new Random(System.currentTimeMillis());
        return range * rand.nextFloat();
    }

}
