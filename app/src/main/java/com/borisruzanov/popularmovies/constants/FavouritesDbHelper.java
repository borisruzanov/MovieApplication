package com.borisruzanov.popularmovies.constants;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FavouritesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favouritesDB";
    private static final int DATABASE_VERSION = 1;

    public FavouritesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(Contract.TAG_WORK_PROCESS_CHECKING, "FavouritesDbHelper - onCreate");

        final String SQL_CREATE_FAVOURITES_TABLE = "CREATE TABLE " +

                        Contract.TableInfo.TABLE_NAME + " (" +
                        Contract.TableInfo.COLUMN_ID + " TEXT NOT NULL, " +
                        Contract.TableInfo.COLUMN_TITLE + " TEXT NOT NULL, " +
                        Contract.TableInfo.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                        Contract.TableInfo.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                        Contract.TableInfo.COLUMN_RATING + " TEXT NOT NULL, " +
                        Contract.TableInfo.COLUMN_OVERVIEW + " TEXT NOT NULL " +
                        "); ";
        db.execSQL(SQL_CREATE_FAVOURITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /**
         * Just drop the old version of DB and create a new
         */
        db.execSQL("DROP TABLE IF EXISTS " + Contract.TableInfo.TABLE_NAME);
        onCreate(db);
    }
}
