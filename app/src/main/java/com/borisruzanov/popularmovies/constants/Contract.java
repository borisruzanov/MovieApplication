package com.borisruzanov.popularmovies.constants;

import android.provider.BaseColumns;

public class Contract {
    private Contract() {
    }

    public static final class TableInfo implements BaseColumns{
        public static final String TABLE_NAME = "favourite_movies";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_OVERVIEW= "overview";
        public static final String COLUMN_POSTER_PATH= "poster_path";
    }
}
