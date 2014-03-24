package com.giorgos.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import static com.giorgos.sqlite.CarsDatabaseHelper.*;

public class MainActivity extends ActionBarActivity {

    private CarsDatabaseHelper dbHelper;

    private SimpleCursorAdapter cursorAdapter;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);

        dbHelper = new CarsDatabaseHelper(this);
        addInitialData();

        findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertRandomCar();
                populateListView();
            }
        });

        findViewById(R.id.btnDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeNonInternal();
                populateListView();
            }
        });

        findViewById(R.id.btnRefresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateListView();
            }
        });
    }

    private void insertRandomCar() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db != null) {
            try {
                ContentValues values = new ContentValues();
                //values.put("_id", 1);
                values.put(COL_BRAND, "Honda");
                values.put(COL_MODEL, "Civic");
                values.put(COL_YEAR, "1998");
                values.put(COL_INTERNAL, 0);
                db.insert(CARS_TABLE, null, values);
                Log.i("main", "inserted");
            } finally {
                db.close();
            }
        }
    }

    private void removeNonInternal() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db != null) {
            try {
                db.delete(CARS_TABLE, COL_INTERNAL + " = 0", null);
                Log.i("main", "inserted");
            } finally {
                db.close();
            }
        }
    }

    private void addInitialData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db != null) {
            try {
                ContentValues values = new ContentValues();
                //values.put("_id", 1);
                values.put(COL_BRAND, "Honda");
                values.put(COL_MODEL, "CR-Z");
                values.put(COL_YEAR, "2008");
                values.put(COL_INTERNAL, 1);
                db.insert(CARS_TABLE, null, values);
                Log.i("main", "inserted");
            } finally {
                db.close();
            }
        }
    }

    private void populateListView() {
        String[] projections = {
                CarsDatabaseHelper.COL_BRAND,
                CarsDatabaseHelper.COL_MODEL,
                CarsDatabaseHelper.COL_YEAR,
                "_id",
        };

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if (db != null) {
            try {
                Cursor cursor = db.query(CARS_TABLE, projections, null, null, null, null, null);
                cursorAdapter = new SimpleCursorAdapter(this, R.layout.cars_list, cursor, projections, new int[]{
                        R.id.textBrand, R.id.textModel, R.id.textYear
                }, 0);
                Log.i("main", "Got " + cursor.getCount() + " results");
                listView.setAdapter(cursorAdapter);
                cursorAdapter.notifyDataSetChanged();
            } finally {
                db.close();
            }

        }
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
