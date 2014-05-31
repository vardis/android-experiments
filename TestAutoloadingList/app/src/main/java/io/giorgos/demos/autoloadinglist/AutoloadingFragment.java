package io.giorgos.demos.autoloadinglist;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the auto-update of the list. The auto-update functionality is triggered when the user
 * is close to the end of the visible range of the list. The threshold is controlled by the THRESHOLD
 * constant. A threshold value of 5 means that the update action will be triggered when the user has
 * scrolled up to the 5th element from the end of the list.
 *
 * To avoid memory leaks and crashes upon changes in the device configuration, we avoid the usage
 * of a AsyncTask. Instead, we use a Handler with a custom Runnable that actually refreshes the
 * contents of the list. When the onStop callback is invoked, we clear from the Handler any callbacks
 * associated with the Runnable. Upon the onStart callback we re-create the Handler.
 *
 * The fragment is set to be retained in order to survive configuration changes.
 */
public class AutoloadingFragment extends ListFragment implements AbsListView.OnScrollListener {

    private final int THRESHOLD = 5;

    private ArrayAdapter<String> mAdapter;

    private Handler mHandler;
    private AddItemsRunnable mRunnable;

    // Store the state of operations before a device configuration change occurred
    private boolean mIsLoading;
    private boolean mWasLoading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mRunnable = new AddItemsRunnable();
    }

    @Override
    public void onStart() {
        super.onStart();

        // we are resuming operations, check if a load was in progress and resume it
        if (mWasLoading) {
            mIsLoading = true;
            mWasLoading = false;
            asyncAddItems();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        // store the current state of operations and de-associate ourselves from the Handler
        mWasLoading = mIsLoading;
        mIsLoading = false;
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mHandler = new Handler();

        // it is important to add the footer before setting the adapter, otherwise it won't be rendered
        View footer = LayoutInflater.from(getActivity()).inflate(R.layout.list_loading_progress, null);
        getListView().addFooterView(footer, null, false);

        mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
        addAPageOfTestData();
        setListAdapter(mAdapter);

        // we must listen to scrolling events in order to trigger the auto-update functionality
        getListView().setOnScrollListener(this);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (!mIsLoading) {
            // determines if the threshold condition holds and triggers the async update in that case
            int lastVisibleItem = firstVisibleItem + visibleItemCount;
            int remainingItems = totalItemCount - lastVisibleItem;
            if (remainingItems <= THRESHOLD) {
                asyncAddItems();
            }
        }

    }

    /**
     * Emulates the delay in loading new data by means of a Handler.postDelayed
     */
    private void asyncAddItems() {
        mIsLoading = true;
        mHandler.postDelayed(mRunnable, 2000);
    }

    /**
     * Adds dummy data to the adapter.
     */
    private class AddItemsRunnable implements Runnable {
        @Override
        public void run() {
            addAPageOfTestData();
            mIsLoading = false;
        }

    }

    /**
     * Generates a number of test data and appends them to the adapter.
     */
    private void addAPageOfTestData() {
        List<String> newItems = new ArrayList<String>();
        for (int i = 0; i < 4 * THRESHOLD; i++) {
            int idx = mAdapter.getCount() + i;
            newItems.add("item - " + idx);
        }
        mAdapter.addAll(newItems);
    }
}
