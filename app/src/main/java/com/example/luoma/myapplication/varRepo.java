package com.example.luoma.myapplication;

import android.provider.BaseColumns;

/**
 * Created by LuoMa on 09/25/2017.
 */

public final class varRepo {

    private varRepo() {}

    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_RESTAURANT = "restaurant";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_NAME_TITLE + " TEXT," +
                    FeedEntry.COLUMN_NAME_RESTAURANT + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;


}
