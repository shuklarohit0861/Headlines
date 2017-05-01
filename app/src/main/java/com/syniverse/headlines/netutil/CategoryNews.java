package com.syniverse.headlines.netutil;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Rohit Shukla on 4/30/2017.
 */

public class CategoryNews {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("sources")
    @Expose
    public List<SourceNews> sources = null;
}
