package com.giorgos.contentprovider.ui;

import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.giorgos.contentprovider.CarsContract;
import com.giorgos.contentprovider.R;


public class MainActivity extends ActionBarActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG = "MainActivity";

    private SimpleCursorAdapter cursorAdapter;

    private ListView listView;

    private String[] projections = {
            CarsContract.Car.Cols.BRAND,
            CarsContract.Car.Cols.MODEL,
            CarsContract.Car.Cols.YEAR,
            "_id",
    };

    private static final int LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);

        addInitialData();

        getLoaderManager().initLoader(LOADER_ID, null, this);

        findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertRandomCar();
                refreshListView();
            }
        });

        findViewById(R.id.btnDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshListView();
            }
        });

        findViewById(R.id.btnRefresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshListView();
            }
        });
    }

    private void refreshListView() {
        getLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    private void insertRandomCar() {
        ContentValues values = new ContentValues();
        values.put(CarsContract.Car.Cols.BRAND, "Honda");
        values.put(CarsContract.Car.Cols.MODEL, "Civic");
        values.put(CarsContract.Car.Cols.YEAR, "1998");
        values.put(CarsContract.Car.Cols.TYPE, "Sedan");
        values.put(CarsContract.Car.Cols.DOORS, 5);
        getContentResolver().insert(CarsContract.Car.CONTENT_URI, values);
        Log.i("main", "inserted");

    }


    private void addInitialData() {
        ContentValues values = new ContentValues();
        values.put(CarsContract.Car.Cols.BRAND, "Honda");
        values.put(CarsContract.Car.Cols.MODEL, "CR-Z");
        values.put(CarsContract.Car.Cols.YEAR, "2008");
        values.put(CarsContract.Car.Cols.TYPE, "Hatchback");
        values.put(CarsContract.Car.Cols.DOORS, 3);

        getContentResolver().insert(CarsContract.Car.CONTENT_URI, values);
        Log.i("main", "inserted");
    }

    private void populateListView(Cursor cursor) {
        if (cursorAdapter == null) {
            cursorAdapter = new SimpleCursorAdapter(this, R.layout.cars_list, cursor, projections, new int[]{
                    R.id.textBrand, R.id.textModel, R.id.textYear
            }, 0);
        } else {
            cursorAdapter.swapCursor(cursor);
        }
        listView.setAdapter(cursorAdapter);
        cursorAdapter.notifyDataSetChanged();
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
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Log.i(LOG_TAG, "Started loading...");
        return new CursorLoader(this, CarsContract.Car.CONTENT_URI, projections, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.i(LOG_TAG, "Finished loading...");
        populateListView(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        populateListView(null);
    }
}
