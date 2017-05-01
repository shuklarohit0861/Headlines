package com.syniverse.headlines.syncadapter;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by g801845 on 5/1/2017.
 */

public class SyncService extends Service {

    private static final String TAG = "syncService";

    private static final Object sSyncAdapterLock = new Object();
    private static SyncAdapter sSyncAdapter = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"service created");
        synchronized (sSyncAdapterLock){
            if(sSyncAdapter == null){
                sSyncAdapter = new SyncAdapter(getApplicationContext(),true);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}
