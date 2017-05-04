package com.syniverse.headlines;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.syniverse.headlines.database.NewsContract;
import com.syniverse.headlines.netutil.UtilsNetwork;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rohit shukla on 5/2/2017.
 * Adapter for List of Source.
 */

public class AdaptorSource extends RecyclerView.Adapter<AdaptorSource.ViewHolder> {

    private Context mContext;


    private Cursor mCursor;

    public AdaptorSource(Context context) {
        this.mContext = context;
    }

    public void swapCursor(Cursor cursor) {
        mCursor = cursor;
        notifyDataSetChanged();
    }


    @Override
    public AdaptorSource.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.source_card_list, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AdaptorSource.ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        int sourceName = mCursor.getColumnIndex(NewsContract.Category.COLUMN_SOURCE_NAME);
        int sourceLogo = mCursor.getColumnIndex(NewsContract.Category.COLUMN_SOURCE_URL);

        holder.textViewNewsSourceName.setText(mCursor.getString(sourceName));

        String url = "https://icons.better-idea.org/icon?url="
                + mCursor.getString(sourceLogo)
                + "&size=70..120..200";

        Glide.with(mContext)
                .load(url)
                .centerCrop()
                .into(holder.imageViewSourceImage);
        holder.imageViewSourceImage.setContentDescription(mCursor.getString(sourceName));
    }

    @Override
    public int getItemCount() {

        return mCursor == null ? 0 : mCursor.getCount();

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.imageView_sourceImage)
        ImageView imageViewSourceImage;
        @BindView(R.id.textView_newsSourceName)
        TextView textViewNewsSourceName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mCursor.moveToPosition(getAdapterPosition());
            int name = mCursor.getColumnIndex(NewsContract.Category.COLUMN_SOURCE_NAME);
            int sortby = mCursor.getColumnIndex(NewsContract.Category.COLUMN_SORT_BYS_AVAILABLE);
            int source = mCursor.getColumnIndex(NewsContract.Category.COLUMN_ARTICLE_ID);

            String nameString = mCursor.getString(name);
            String sortbyString = mCursor.getString(sortby);
            String sourceString = mCursor.getString(source);

            Intent intent = new Intent(mContext, CategorySelectionActivity.class);
            intent.putExtra(NewsContract.Category.COLUMN_SOURCE_NAME, nameString);
            intent.putExtra(NewsContract.Category.COLUMN_SORT_BYS_AVAILABLE, sortbyString);
            intent.putExtra(NewsContract.Category.COLUMN_ARTICLE_ID, sourceString);

            if (new UtilsNetwork().checkInternet(mContext)) {
                mContext.startActivity(intent);
            }
        }
    }
}
