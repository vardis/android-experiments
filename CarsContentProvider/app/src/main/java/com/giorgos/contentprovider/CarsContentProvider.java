package com.giorgos.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

/**
 * The content provider provides basic CRUD operations for data exchanged between processes.
 *
 * This is an example of a SQLite based repository provider. It is not a requirement of course
 * to use a database for the content repository.
 *
 * All the methods of a ContentProvider, with the exception of onCreate, must be thread-safe.
 *
 * Client code needs to provide a Uri for specifying the target of the requested operation. T
 *
 * The content provider really acts as a thin layer between the clients and an instance of either
 * WritableDatabase or ReadableDatabase. This layer provides a form of validation (e.g. the Uri is
 * appropriate for the operation), add default values for columns that are missing in insert operations,
 * notify the context about an updated Uri (ie for insertions, updates, deletions) and possibly add
 * additional selection arguments or projections.
 *
 * Obviously for in-process access there are more convenient ways to access the data than
 * going through a ContentProvider.
 *
 *
 */
public class CarsContentProvider extends ContentProvider {

    private final static String LOG_TAG = "CarsProvider";

    private CarsDatabase carsDatabase;

    /**
     * It is important to avoid performing lengthy operations within this method. Defer the creation
     * of the actual database and the import of any initial data. Once a call to CarsDatase.getWriteableDatabase()
     * method is made, the CarsDatabase.onCreate() method will be triggered and these operations can
     * then take place.
     * @return
     */
    @Override
    public boolean onCreate() {
        carsDatabase = new CarsDatabase(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = carsDatabase.getReadableDatabase();
        final SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        final int match = CarsContract.URI_MATCHER.match(uri);
        switch (match) {
            case CarsContract.Car.PATH_FOR_ID_TOKEN:
                builder.appendWhere(CarsContract.Car.Cols.ID + " = " + uri.getLastPathSegment());
            case CarsContract.Car.PATH_TOKEN:
                builder.setTables(CarsContract.Car.NAME);
                return builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
            default:
                return null;
        }
    }

    @Override
    public String getType(Uri uri) {
        final int match = CarsContract.URI_MATCHER.match(uri);
        switch (match) {
            case CarsContract.Car.PATH_TOKEN:
                return CarsContract.Car.CONTENT_TYPE_DIR;
            case CarsContract.Car.PATH_FOR_ID_TOKEN:
                return CarsContract.Car.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Uri not supported");
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = carsDatabase.getWritableDatabase();
        final int match = CarsContract.URI_MATCHER.match(uri);
        switch (match) {
            case CarsContract.Car.PATH_TOKEN:
                // IDs are only auto-generated
                values.remove(CarsContract.Car.Cols.ID);

                final long rowId = db.insert(CarsContract.Car.NAME, null, values);
                if (rowId < 0) {
                    Log.d(LOG_TAG, "Failed to insert values at " + uri);
                    return null;
                }
                final Uri insertedID = ContentUris.withAppendedId(CarsContract.Car.CONTENT_URI, rowId);
                getContext().getContentResolver().notifyChange(insertedID, null);
                return insertedID;
            default:
                return null;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = carsDatabase.getWritableDatabase();
        final int match = CarsContract.URI_MATCHER.match(uri);
        switch (match) {
            case CarsContract.Car.PATH_FOR_ID_TOKEN:
                selection += CarsContract.Car.Cols.ID + " = " + uri.getLastPathSegment();
            case CarsContract.Car.PATH_TOKEN:
                int count = db.delete(CarsContract.Car.NAME, selection, selectionArgs);
                if (count > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return count;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = carsDatabase.getWritableDatabase();
        final int match = CarsContract.URI_MATCHER.match(uri);
        switch (match) {
            case CarsContract.Car.PATH_FOR_ID_TOKEN:
                selection += CarsContract.Car.Cols.ID + " = " + uri.getLastPathSegment();
                int count = db.update(CarsContract.Car.NAME, values, selection, selectionArgs);
                if (count > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return count;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }
}
