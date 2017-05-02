package com.syniverse.headlines.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Rohit Shukla on 4/27/2017.
 */

public class NewsProvider extends ContentProvider {


    private static final int ARTICLES = 100 ;
    private static final int CATEGORY = 200;
    private static final int ARTICLES_WITH_ID = 300;
    private static final int CATEGORY_WITH_ID =400;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final SQLiteQueryBuilder mSQLiteQueryBuilder = new SQLiteQueryBuilder();
    private NewsDbhelper mDbHelper;

    private static UriMatcher buildUriMatcher() {

        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = NewsContract.AUTHORITY;

        uriMatcher.addURI(authority,NewsContract.PATH_ARTICLE,ARTICLES);
        uriMatcher.addURI(authority,NewsContract.PATH_CATEGORY,CATEGORY);
        uriMatcher.addURI(authority,NewsContract.PATH_ARTICLE+ "/#",ARTICLES_WITH_ID);
        uriMatcher.addURI(authority,NewsContract.PATH_CATEGORY+"/#",CATEGORY_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new NewsDbhelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Cursor cursor;
        final int match = sUriMatcher.match(uri);

        switch (match)
        {
            case ARTICLES:{
                cursor = mDbHelper.getReadableDatabase()
                        .query(
                                NewsContract.Article.TABLE_NAME,
                                projection,
                                selection,
                                selectionArgs,
                                null,
                                null,
                                sortOrder
                        );
                break;
            }

            case ARTICLES_WITH_ID :{
                String id = NewsContract.Article.getArticlesId(uri);

                selection = NewsContract.Article.COLUMN_ID + " = ?";

                selectionArgs = new String[]{id};

                cursor = mDbHelper.getReadableDatabase()
                        .query(
                                NewsContract.Article.TABLE_NAME,
                                projection,
                                selection,
                                selectionArgs,
                                null,
                                null,
                                sortOrder
                        );
                break;
            }

            case CATEGORY :{
                cursor = mDbHelper.getReadableDatabase()
                        .query(
                                NewsContract.Category.TABLE_NAME,
                                projection,
                                selection,
                                selectionArgs,
                                null,
                                null,
                                sortOrder

                        );
                break;
            }

            case CATEGORY_WITH_ID :{

                String id = NewsContract.Category.getCategoryId(uri);
                selection = NewsContract.Category.COLUMN_ID + " = ?";
                selectionArgs = new String[]{id};
                cursor = mDbHelper.getReadableDatabase()
                        .query(
                                NewsContract.Category.TABLE_NAME,
                                projection,
                                selection,
                                selectionArgs,
                                null,
                                null,
                                sortOrder
                        );
                break;
            }

            default: throw new UnsupportedOperationException( "Unknown Uri :" + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;


    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }


    /** inserting only in bulk so no need.
     * *
     * @param uri
     * @param values
     * @return
     */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }


    /**
     * delete is used every time user change the category.
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return
     */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int deleteRow;

        switch (match)
        {
            case ARTICLES:{
                deleteRow = db.delete(NewsContract.Article.TABLE_NAME,selection,selectionArgs);
            }
            break;
            case CATEGORY:{
                deleteRow = db.delete(NewsContract.Category.TABLE_NAME,selection,selectionArgs);
            }
            break;

            default:
                throw new UnsupportedOperationException("UnSupported Operation " + uri);
        }

        if (deleteRow != 0)
        {
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return deleteRow;
    }

    /**
     * not using update any where
     * @param uri
     * @param values
     * @param selection
     * @param selectionArgs
     * @return
     */

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        String rawQuery = "DELETE  FROM " + NewsContract.Article.TABLE_NAME;

        Cursor cursor;

        int returnCount = 0;

        switch (match){
            case ARTICLES:{
                db.beginTransaction();
                try{
                    db.rawQuery(rawQuery,null);
                    for (ContentValues value : values)
                    {
                        long _id = db.insert(
                                NewsContract.Article.TABLE_NAME,
                                null,
                                value
                        );

                        if(_id != -1)
                        {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                }

                finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri,null);
                return returnCount;
            }

            case CATEGORY:{
                db.beginTransaction();
                try{
                    for (ContentValues value : values){

                        long _id = db.insert(NewsContract.Category.TABLE_NAME,null,value);
                        if(_id != -1)
                        {
                            returnCount++;
                        }

                    }
                    db.setTransactionSuccessful();
                }
                finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri,null);
                return returnCount;
            }

            default:
            return super.bulkInsert(uri, values);

        }


    }
}
