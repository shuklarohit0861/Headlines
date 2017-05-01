package com.syniverse.headlines.netutil;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Rohit Shukla on 4/29/2017.
 */

public class NewsApiCall {
    public static final String BASE_URL_ARTICLE = "https://newsapi.org/v1/articles";

    public static final String BASE_URL_CATEGORY = "https://newsapi.org/v1/sources";

    private static Retrofit retrofitArticle = null;

    private static Retrofit retrofitCategory = null;

    static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .create();

    public static Retrofit getRetrofitArticle(){
        if(retrofitArticle == null){
            retrofitArticle = new Retrofit.Builder()
                    .baseUrl(BASE_URL_ARTICLE)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return retrofitArticle;
    }
    public static Retrofit getRetrofitCategory(){
        if(retrofitCategory == null){
            retrofitCategory = new Retrofit.Builder()
                    .baseUrl(BASE_URL_CATEGORY)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofitCategory;
    }


}
