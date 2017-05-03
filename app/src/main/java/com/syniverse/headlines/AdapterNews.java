package com.syniverse.headlines;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.syniverse.headlines.database.NewsContract;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rohit shukla on 4/27/2017.
 *
 */

public class AdapterNews extends RecyclerView.Adapter<AdapterNews.ViewHolder> {

    private Cursor cursor;
    private Context mContext;
    private int url;


    AdapterNews(Context context)
    {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_news,parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        cursor.moveToPosition(position);

        int title = cursor.getColumnIndex(NewsContract.Article.COLUMN_TITLE);

        int urlToImage = cursor.getColumnIndex(NewsContract.Article.COLUMN_URL_TO_IMAGE);

        String sUrlToImage = cursor.getString(urlToImage);

        String headline = cursor.getString(title);
        holder.textViewHeadline.setText(headline);

        Glide.with(mContext).load(sUrlToImage)
                    .centerCrop()
                    .into(holder.imageViewUrlToImage);
    }

    @Override
    public int getItemCount() {
        if (null == cursor) return 0;

        return cursor.getCount();
    }

    public void swapCursor(Cursor cursor) {
        this.cursor = cursor;
        notifyDataSetChanged();


    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.textView_headline)
        TextView textViewHeadline;
        @BindView(R.id.imageView_urlToImage)
        ImageView imageViewUrlToImage;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            cursor.moveToPosition(position);
            url = cursor.getColumnIndex(NewsContract.Article.COLUMN_URL);
            String urlString = cursor.getString(url);

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
            mContext.startActivity(intent);


        }
    }


}
