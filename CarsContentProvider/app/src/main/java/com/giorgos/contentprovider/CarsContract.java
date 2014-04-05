package com.giorgos.contentprovider;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Provides metadata information regarding the cars database. There's an inner
 * static class for metadata specific to each table.
 *
 * Useful types of metadata
 * to provide for client code include:
 * <ul>
 *     <li>The symbolic name or authority for the whole content provider</li>
 *     <li>The content Uri for each data entity</li>
 *     <li>The column names</li>
 *     <li>The content type for single and multiple items</li>
 *     <li>A UriMatcher instance configured to map Uri to content types</li>
 * </ul>
 */
public final class CarsContract {
    public static final String AUTHORITY = "vnd.com.giorgos.cars";
    private static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);
    public static final UriMatcher URI_MATCHER = buildUriMatcher();

    private CarsContract(){}

    private static  UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = AUTHORITY;

        matcher.addURI(authority, Car.PATH, Car.PATH_TOKEN);
        matcher.addURI(authority, Car.PATH_FOR_ID, Car.PATH_FOR_ID_TOKEN);

        return matcher;
    }

    public static class Car {
        public static final String NAME = "car";

        public static final String PATH = "cars";
        public static final int PATH_TOKEN = 100;
        public static final String PATH_FOR_ID = "cars/#";
        public static final int PATH_FOR_ID_TOKEN = 200;

        public static final Uri CONTENT_URI = BASE_URI.buildUpon().appendPath(PATH).build();

        public static final String CONTENT_TYPE_DIR = "vnd.android.cursor.dir/car";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/car";

        public static class Cols {
            public static final String ID = BaseColumns._ID; // convention
            public static final String YEAR = "year";
            public static final String TYPE  = "type";
            public static final String BRAND = "brand";
            public static final String MODEL = "model";
            public static final String DOORS = "numdoors";
        }

    }

}
