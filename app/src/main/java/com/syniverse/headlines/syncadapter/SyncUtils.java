package com.syniverse.headlines.syncadapter;

import android.content.Context;

import com.syniverse.headlines.database.NewsContract;

/**
 * Created by Rohit shukla on 5/1/2017.
 */

public class SyncUtils {
    private static final long SYNC_FREQUENCY = 60*60*3 ; // 3 hour (in seconds)
    private static final String CONTENT_AUTHORITY = NewsContract.AUTHORITY;
    private static final String PREF_SETUP_COMPLETE = "setup_complete";
    public static final String ACCOUNT_TYPE = "com.syniverse.headlines.account";

    public static void CreateSyncAccount(Context context)
    {
        boolean newAccount = false;
    }

}
