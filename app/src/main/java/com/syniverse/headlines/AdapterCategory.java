package com.syniverse.headlines;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.syniverse.headlines.netutil.UtilsNetwork;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rohit shukla on 4/28/2017.
 */

public class AdapterCategory extends RecyclerView.Adapter<AdapterCategory.ViewHolder> {

    List<String> category =new ArrayList<>();
    private Context mContext;


    public AdapterCategory(ArrayList<String> category, Context context) {
        this.category = category;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_category,parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.textViewCategory.setText(category.get(position));

    }

    @Override
    public int getItemCount() {
        return category.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.textViewCategory)
        TextView textViewCategory;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int possition = getAdapterPosition();
            String categoryString = category.get(possition);
            Intent intent = new Intent(mContext, ListOfSource.class);
            intent.putExtra("LIST", categoryString);
            if (new UtilsNetwork().checkInternet(mContext)) {
                mContext.startActivity(intent);
            } else {
                Toast.makeText(mContext, "Oooops no internet !!!!", Toast.LENGTH_LONG).show();
            }

        }
    }


}
