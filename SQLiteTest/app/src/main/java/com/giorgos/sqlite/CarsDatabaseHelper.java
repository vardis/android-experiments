package com.giorgos.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by giorgos on 20/03/14.
 */
public class CarsDatabaseHelper extends SQLiteOpenHelper {

    public static final String CARS_TABLE = "Cars";
    public static final String COL_BRAND = "brand";
    public static final String COL_MODEL = "model";
    public static final String COL_YEAR = "year";
    public static final String COL_INTERNAL = "internal";

    private static final String DATABASE_FILE = "cars.db";

    private static final String CREATE_TABLES = "CREATE TABLE " + CARS_TABLE + " (" +
            "_id integer primary key autoincrement, " +
            COL_BRAND + " varchar not null, " +
            COL_MODEL + " varchar not null, " +
            COL_YEAR + " integer not null, " +
            COL_INTERNAL + " integer default 0);";

    private static final int VERSION = 2;

    public CarsDatabaseHelper(Context context) {
        super(context, DATABASE_FILE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("Cars", "Called onCreate");
        doCreateLatestSchema(db);
    }

    private void doCreateLatestSchema(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("Cars", "Called onUpgrade " + oldVersion + " -> " + newVersion);
        db.execSQL("DROP TABLE " + CARS_TABLE);
        doCreateLatestSchema(db);
    }
}
