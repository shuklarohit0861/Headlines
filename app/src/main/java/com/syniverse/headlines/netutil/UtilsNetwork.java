package com.syniverse.headlines.netutil;

import android.content.ContentValues;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.syniverse.headlines.database.NewsContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Rohit shukla on 5/3/2017.
 */

public class UtilsNetwork {

    public static final String PREF_LOADING_SOURCE_DONE = "loading sources done nice work ethan";
    public static Map<String, String> category = new HashMap<>();

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

    public void grabCategory(String category, final Context context) {
        Boolean SourceIsLoaded = PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_LOADING_SOURCE_DONE, false);

        GetResponseInterfaceCategory getResponseInterfaceCategory = NewsApiCall.getRetrofit().create(GetResponseInterfaceCategory.class);

        Call<CategoryNews> call;

        call = getResponseInterfaceCategory.getCategory(category);

        if (checkInternet(context) && !SourceIsLoaded) {
            call.enqueue(new Callback<CategoryNews>() {
                @Override
                public void onResponse(Call<CategoryNews> call, Response<CategoryNews> response) {
                    CategoryNews categoryNews = response.body();
                    List<SourceNews> sourceNewses = categoryNews.sources;
                    List<ContentValues> contentValuesList = new ArrayList<ContentValues>();

                    for (SourceNews sourceNews : sourceNewses) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(NewsContract.Category.COLUMN_CATEGORY, sourceNews.category);
                        contentValues.put(NewsContract.Category.COLUMN_ARTICLE_ID, sourceNews.id);
                        contentValues.put(NewsContract.Category.COLUMN_SOURCE_NAME, sourceNews.name);
                        contentValues.put(NewsContract.Category.COLUMN_SOURCE_URL, sourceNews.url);
                        contentValues.put(NewsContract.Category.COLUMN_SORT_BYS_AVAILABLE,
                                sourceNews.sortBysAvailable.get(0));
                        contentValuesList.add(contentValues);
                    }

                    ContentValues[] Sources = new ContentValues[contentValuesList.size()];

                    contentValuesList.toArray(Sources);

                    if (Sources.equals(null)) {
                        context.getContentResolver().bulkInsert(NewsContract.Category.CONTENT_URI, Sources);
                        PreferenceManager.getDefaultSharedPreferences(context)
                                .edit()
                                .putBoolean(PREF_LOADING_SOURCE_DONE, true)
                                .apply();
                    }
                }

                @Override
                public void onFailure(Call<CategoryNews> call, Throwable t) {

                    Toast.makeText(context, " Ethan Hunt and his new team went rogue ", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(context, "Ooops no internet !!!", Toast.LENGTH_LONG).show();
        }
    }
}
