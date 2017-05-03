package com.syniverse.headlines;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.syniverse.headlines.database.NewsContract;
import com.syniverse.headlines.netutil.UtilsNetwork;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListOfSource extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.recyclerViewSources)
    RecyclerView recyclerViewSources;
    String categoryString = null;

    AdaptorSource mAdapterSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_source);
        ButterKnife.bind(this);

        // categoryString = "entertainment";

        mAdapterSource = new AdaptorSource(getApplicationContext());
        recyclerViewSources.setHasFixedSize(true);

        recyclerViewSources.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.LayoutManager mLayoutManger = new GridLayoutManager(getApplicationContext(), 2);

        recyclerViewSources.setLayoutManager(mLayoutManger);

        categoryString = " null ";
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            String bundleString = bundle.getString("LIST");
            if (bundleString != null) {
                categoryString = UtilsNetwork.category.get(bundleString);
                getSupportLoaderManager().initLoader(4, null, this);
                Log.i("bundle String", bundleString + categoryString);

            }
        }

        // categoryString = "entertainment";
        new UtilsNetwork().grabCategory(categoryString, getApplicationContext());


        recyclerViewSources.setAdapter(mAdapterSource);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String whereCondition = NewsContract.Category.COLUMN_CATEGORY + " = ? ";

        return new CursorLoader(getApplicationContext(),
                NewsContract.Category.CONTENT_URI,
                new String[]{
                        NewsContract.Category.COLUMN_ID,
                        NewsContract.Category.COLUMN_CATEGORY,
                        NewsContract.Category.COLUMN_ARTICLE_ID,
                        NewsContract.Category.COLUMN_SOURCE_NAME,
                        NewsContract.Category.COLUMN_SOURCE_URL,
                        NewsContract.Category.COLUMN_SORT_BYS_AVAILABLE
                },
                whereCondition,
                new String[]{categoryString},
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            mAdapterSource.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapterSource.swapCursor(null);
    }
}
