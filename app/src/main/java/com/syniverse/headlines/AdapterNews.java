package com.syniverse.headlines;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rohit shukla on 4/27/2017.
 *
 */

public class AdapterNews extends RecyclerView.Adapter<AdapterNews.ViewHolder> {

    private final Context mContext;
    String headlines = "this is the headline line for today.  i am a great android developer. Yo";

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
            holder.textViewHeadline.setText(headlines);

            Glide.with(mContext).load("https://cdn3.tnwcdn.com/wp-content/blogs.dir/1/files/2017/04/Samsung-Wemogee.jpg")
                    .centerCrop()
                    .into(holder.imageViewUrlToImage);
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textView_headline)
        TextView textViewHeadline;
        @BindView(R.id.imageView_urlToImage)
        ImageView imageViewUrlToImage;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }


}
