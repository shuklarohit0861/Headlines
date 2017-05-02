package com.syniverse.headlines.netutil;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by g801845 on 4/30/2017.
 */

public interface GetResponseInterfaceCategory {

    @GET("sources")
    Call<CategoryNews> getCategory(@Query("category") String category);
}
