package com.syniverse.headlines.syncadapter;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.syniverse.headlines.database.NewsContract;
import com.syniverse.headlines.netutil.ArticlesNews;
import com.syniverse.headlines.netutil.GetNews;
import com.syniverse.headlines.netutil.GetResponseInterfaceArticle;
import com.syniverse.headlines.netutil.NewsApiCall;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Rohit shukla on 5/1/2017.
 */

public class SyncAdapter extends AbstractThreadedSyncAdapter {
    public static final String TAG = "SYncAdapter";

    private static final String APIKEY ="37e5a5c4982346e082cd8476900cce98";

    private final ContentResolver mContentResolver;

    Call<GetNews> callArticles;


    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {


        GetResponseInterfaceArticle getResponseInterfaceArticle = NewsApiCall
                .getRetrofit()
                .create(GetResponseInterfaceArticle.class);

        callArticles = getResponseInterfaceArticle.getNews("google-news","top",APIKEY);

        callArticles.enqueue(new Callback<GetNews>() {
            @Override
            public void onResponse(Call<GetNews> call, Response<GetNews> response) {

                GetNews getNews = response.body();

                String status = getNews.getStatus();

                Log.d("Status", status);

                List<ArticlesNews> articlesNewses = response.body().getArticles();

                List<ContentValues> contentValuesList = new ArrayList<ContentValues>();

                for(ArticlesNews news : articlesNewses)
                {
                    ContentValues values = new ContentValues();
                    values.put(NewsContract.Article.COLUMN_SOURCE, getNews.getSource());
                    values.put(NewsContract.Article.COLUMN_TITLE,news.getTitle());
                    values.put(NewsContract.Article.COLUMN_DESCRIPTION,news.getDescription());
                    values.put(NewsContract.Article.COLUMN_URL,news.getUrl());
                    values.put(NewsContract.Article.COLUMN_URL_TO_IMAGE,news.getUrlToImage());
                    values.put(NewsContract.Article.COLUMN_PUBLISHED_AT,news.getPublishedAt());

                    contentValuesList.add(values);
                }

                ContentValues[] news = new ContentValues[contentValuesList.size()];

                contentValuesList.toArray(news);

                if(!news.equals(null))
                {
                    mContentResolver.delete(NewsContract.Article.CONTENT_URI, null, null);
                    mContentResolver.bulkInsert(NewsContract.Article.CONTENT_URI,news);
                }
            }

            @Override
            public void onFailure(Call<GetNews> call, Throwable t) {

            }
        });






    }
}
