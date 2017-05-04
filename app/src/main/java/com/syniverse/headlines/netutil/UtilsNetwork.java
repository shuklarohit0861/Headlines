package com.syniverse.headlines.netutil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.syniverse.headlines.database.NewsContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * Created by Rohit shukla on 5/3/2017.
 */

public class UtilsNetwork {

    public static final String PREF_LOADING_SOURCE_DONE = "loading sources done nice work ethan";
    private static final String APIKEY = "37e5a5c4982346e082cd8476900cce98";
    public static Map<String, String> category = new HashMap<>();
    long time;
    List<ArticlesNews> articlesNewses = new ArrayList<>();

    public static void setCategory() {
        Map<String, String> category = new HashMap<>();
        category.put("business", "business");
        category.put("entertainment", "entertainment");
        category.put("gaming", "gaming");
        category.put("general", "general");
        category.put("music", "music");
        category.put("politics", "politics");
        category.put("science and nature", "science-and-nature");
        category.put("sport", "sport");
        category.put("technology", "technology");
        UtilsNetwork.category = category;
    }

    public boolean checkInternet(Context context) {
        setCategory();
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public void getCategory(@Nullable String category, final Context context) {
        Boolean SourceIsLoaded = getDefaultSharedPreferences(context)
                .getBoolean(PREF_LOADING_SOURCE_DONE, false);

        GetResponseInterfaceCategory getResponseInterfaceCategory = NewsApiCall.getRetrofit().create(GetResponseInterfaceCategory.class);

        Call<CategoryNews> call;

        call = getResponseInterfaceCategory.getCategory(null);

        if (checkInternet(context)) {
            if (!SourceIsLoaded) {
                call.enqueue(new Callback<CategoryNews>() {
                    @Override
                    public void onResponse(Call<CategoryNews> call, Response<CategoryNews> response) {
                        CategoryNews categoryNews = response.body();
                        List<SourceNews> sourceNewses = categoryNews.sources;
                        List<ContentValues> contentValuesList = new ArrayList<ContentValues>();

                        Log.d("checking", "this is running");

                        for (SourceNews sourceNews : sourceNewses) {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(NewsContract.Category.COLUMN_CATEGORY, sourceNews.category);
                            Log.d(NewsContract.Category.COLUMN_CATEGORY, sourceNews.category);
                            contentValues.put(NewsContract.Category.COLUMN_ARTICLE_ID, sourceNews.id);
                            contentValues.put(NewsContract.Category.COLUMN_SOURCE_NAME, sourceNews.name);
                            contentValues.put(NewsContract.Category.COLUMN_SOURCE_URL, sourceNews.url);
                            contentValues.put(NewsContract.Category.COLUMN_SORT_BYS_AVAILABLE,
                                    sourceNews.sortBysAvailable.get(0));
                            contentValuesList.add(contentValues);
                        }

                        ContentValues[] Sources = new ContentValues[contentValuesList.size()];

                        contentValuesList.toArray(Sources);

                        if (!contentValuesList.isEmpty()) {
                            context.getContentResolver().bulkInsert(NewsContract.Category.CONTENT_URI, Sources);
                            getDefaultSharedPreferences(context)
                                    .edit()
                                    .putBoolean(PREF_LOADING_SOURCE_DONE, true)
                                    .apply();
                        }
                    }

                    @Override
                    public void onFailure(Call<CategoryNews> call, Throwable t) {

                        Toast.makeText(context, " Ethan Hunt and his new team went rogue ", Toast.LENGTH_LONG).show();
                        getDefaultSharedPreferences(context)
                                .edit()
                                .putBoolean(PREF_LOADING_SOURCE_DONE, false)
                                .apply();
                    }
                });
            }
        } else {
            Toast.makeText(context, "Ooops no internet !!!", Toast.LENGTH_LONG).show();
        }
    }

    public void getNewsBySource(final String source, String shortBy, final Context context) {
        Cursor cursor = context.getContentResolver().query(NewsContract.Article.CONTENT_URI,
                new String[]{
                        NewsContract.Article.COLUMN_ID,
                        NewsContract.Article.COLUMN_TITLE,
                        NewsContract.Article.COLUMN_URL
                },
                NewsContract.Article.COLUMN_SOURCE + " = ? ",
                new String[]{source},
                null);
        Log.d("CURSOR GETCOUNT", Integer.toString(cursor.getCount()));
        if (cursor != null && cursor.getCount() == 0) {


            GetResponseInterfaceArticle getResponseInterfaceArticle = NewsApiCall.getRetrofit().create(GetResponseInterfaceArticle.class);

            Call<GetNews> callArticles = getResponseInterfaceArticle.getNews(source, shortBy, APIKEY);
            callArticles.enqueue(new Callback<GetNews>() {
                @Override
                public void onResponse(Call<GetNews> call, Response<GetNews> response) {
                    GetNews getNews = response.body();
                    List<ArticlesNews> articlesNewses = response.body().getArticles();

                    List<ContentValues> contentValuesList = new ArrayList<ContentValues>();

                    for (ArticlesNews news : articlesNewses) {
                        ContentValues values = new ContentValues();
                        values.put(NewsContract.Article.COLUMN_SOURCE, getNews.getSource());
                        values.put(NewsContract.Article.COLUMN_TITLE, news.getTitle());
                        values.put(NewsContract.Article.COLUMN_DESCRIPTION, news.getDescription());
                        values.put(NewsContract.Article.COLUMN_URL, news.getUrl());
                        values.put(NewsContract.Article.COLUMN_URL_TO_IMAGE, news.getUrlToImage());
                        values.put(NewsContract.Article.COLUMN_PUBLISHED_AT, news.getPublishedAt());

                        contentValuesList.add(values);
                }

                    ContentValues[] news = new ContentValues[contentValuesList.size()];

                    contentValuesList.toArray(news);

                    if (!contentValuesList.isEmpty()) {
                        // long timeEscapted = TimeUnit.MICROSECONDS.toHours(System.currentTimeMillis()-time);

                        if (false) {
                            context.getContentResolver().delete(NewsContract.Article.CONTENT_URI, NewsContract.Article.COLUMN_SOURCE + " = ? ", new String[]{source});
//                        // PreferenceManager.getDefaultSharedPreferences(context)
//                                 .edit().putLong(APIKEY,System.currentTimeMillis()).apply();
                        }

                        context.getContentResolver().bulkInsert(NewsContract.Article.CONTENT_URI, news);


                }
                }

                @Override
                public void onFailure(Call<GetNews> call, Throwable t) {
                    Log.e("getNewsBySource", "is not working");
                }
            });

    }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

    }



}
