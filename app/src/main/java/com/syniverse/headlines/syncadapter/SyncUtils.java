package com.syniverse.headlines.syncadapter;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.syniverse.headlines.account.AccountService;
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
        boolean setupCompleter = PreferenceManager
                .getDefaultSharedPreferences(context).getBoolean(PREF_SETUP_COMPLETE,false);

        Account account = AccountService.getAccount(ACCOUNT_TYPE);
        
        AccountManager accountManager = 
                (AccountManager)context.getSystemService(context.ACCOUNT_SERVICE);
        
        if(accountManager.addAccountExplicitly(account,null,null)){

            ContentResolver.setIsSyncable(account,CONTENT_AUTHORITY,1);
            
            ContentResolver.setSyncAutomatically(account,CONTENT_AUTHORITY,true);
            
            ContentResolver.addPeriodicSync(account,CONTENT_AUTHORITY,new Bundle(),SYNC_FREQUENCY);
            
            newAccount = true;
        }
        
        if(newAccount || !setupCompleter){
            TriggerRefresh();
            PreferenceManager.getDefaultSharedPreferences(context)
                    .edit()
                    .putBoolean(PREF_SETUP_COMPLETE,true)
                    .apply();
        }
    }

    /**
     * to trigger an immediate sync
     */


    private static void TriggerRefresh() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL,true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED,true);
        ContentResolver.requestSync(
                AccountService.getAccount(ACCOUNT_TYPE),   //Sync Account
                NewsContract.AUTHORITY,                    //Content Authority
                bundle                                     //Extras
        );

    }

}
