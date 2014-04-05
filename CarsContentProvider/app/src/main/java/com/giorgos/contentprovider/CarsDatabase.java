package com.giorgos.contentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Handles the creation and upgrade of the database.
 *
 * This is the component that issues the actual SQL statements for the creation
 * of the database tables.
 */
public class CarsDatabase extends SQLiteOpenHelper {

    public static final int VERSION = 2;

    public CarsDatabase(Context context) {
        super(context, "cars.db", null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + CarsContract.Car.NAME+ " ( " +
                        CarsContract.Car.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        CarsContract.Car.Cols.BRAND + " TEXT NOT NULL, " +
                        CarsContract.Car.Cols.DOORS   + " INTEGER , " +
                        CarsContract.Car.Cols.MODEL + " TEXT, " +
                        CarsContract.Car.Cols.TYPE + " TEXT, " +
                        CarsContract.Car.Cols.YEAR + " INTEGER, " +
                        "UNIQUE (" +
                        CarsContract.Car.Cols.ID +
                        ") ON CONFLICT REPLACE)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion < newVersion){
            db.execSQL("DROP TABLE IF EXISTS " + CarsContract.Car.NAME);
            onCreate(db);
        }
    }
}
