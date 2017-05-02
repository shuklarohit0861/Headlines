package com.syniverse.headlines.netutil;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Rohit shukla on 4/29/2017.
 */

public interface GetResponseInterfaceArticle {

    @GET("articles")
    Call<GetNews> getNews(@Query("source") String source,
                          @Query("sortBY") String sortBy,
                          @Query("apiKey") String apiKey);


}
