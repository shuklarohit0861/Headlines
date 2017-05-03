package com.syniverse.headlines.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 *
 * Created by Rohit shukla on 4/26/2017.
 * sql lite data base
 */

public class NewsDbhelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "headlines.db";

    private static final int DATA_BASE_VERSION = 3;


    public NewsDbhelper(Context context) {
        super(context, DATABASE_NAME, null, DATA_BASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_TABLE_ARTICLES = " CREATE TABLE " + NewsContract.Article.TABLE_NAME +
                " ( " + NewsContract.Article.COLUMN_ID + " TEXT PRIMARY KEY, "
                + NewsContract.Article.COLUMN_AUTHOR + " TEXT , "
                + NewsContract.Article.COLUMN_TITLE + " TEXT NOT NULL, "
                + NewsContract.Article.COLUMN_DESCRIPTION + " TEXT NOT NULL, "
                + NewsContract.Article.COLUMN_URL + " TEXT NOT NULL, "
                + NewsContract.Article.COLUMN_URL_TO_IMAGE + " TEXT NOT NULL, "
                + NewsContract.Article.COLUMN_PUBLISHED_AT + " TEXT "
                +" );";

        Log.i("ARTICLE TABLE",CREATE_TABLE_ARTICLES);

        final String CREATE_TABLE_CATEGORY = "CREATE TABLE " + NewsContract.Category.TABLE_NAME
                + " ( "
                + NewsContract.Category.COLUMN_ID + " TEXT PRIMARY KEY, "
                + NewsContract.Category.COLUMN_CATEGORY + " TEXT NOT NULL, "
                + NewsContract.Category.COLUMN_ARTICLE_ID + " TEXT NOT NULL, "
                + NewsContract.Category.COLUMN_SOURCE_NAME + " TEXT NOT NULL, "
                + NewsContract.Category.COLUMN_SOURCE_URL + " TEXT NOT NULL, "
                + NewsContract.Category.COLUMN_SORT_BYS_AVAILABLE + " TEXT NOT NULL "
                +" );";

        Log.i("CATEGORY TABLE",CREATE_TABLE_CATEGORY);


        db.execSQL(CREATE_TABLE_ARTICLES);
        db.execSQL(CREATE_TABLE_CATEGORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + NewsContract.Article.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + NewsContract.Category.TABLE_NAME);
        onCreate(db);
    }
}
