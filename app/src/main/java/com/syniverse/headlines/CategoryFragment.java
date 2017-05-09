package com.syniverse.headlines;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CategoryFragment extends Fragment {

    AdapterCategory mAdapter;

    @BindView(R.id.recyclerViewCategory)
    RecyclerView recyclerViewCategory;

    @BindView(R.id.adView)
    AdView mAdView;

    ArrayList<String> category;

    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        category = new ArrayList<>();
        category.add("business");
        category.add("entertainment");
        category.add("gaming");
        category.add("general");
        category.add("music");
        category.add("politics");
        category.add("science and nature");
        category.add("sport");
        category.add("technology");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.bind(this,view);
        Configuration configuration = getResources().getConfiguration();

        MobileAds.initialize(getContext(), "ca-app-pub-3940256099942544~3347511713");


        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        mAdapter = new AdapterCategory(category, getContext());
        recyclerViewCategory.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), numberOfColumns());
        recyclerViewCategory.setHasFixedSize(true);
        recyclerViewCategory.setLayoutManager(layoutManager);


        recyclerViewCategory.setAdapter(mAdapter);
        return view;
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }



}
