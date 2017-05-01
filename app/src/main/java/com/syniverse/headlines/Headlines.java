package com.syniverse.headlines;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Headlines extends AppCompatActivity {

    @BindView(R.id.viewPagerHeadline)
    ViewPager viewPagerHeadline;
    @BindView(R.id.tabLayoutHeadline)
    TabLayout tabLayoutHeadline;

    NewsFragment newsFragment;
    CategoryFragment categoryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healines);
        ButterKnife.bind(this);
        viewPagerHeadline.setOffscreenPageLimit(2);

        setupViewPager(viewPagerHeadline);

        tabLayoutHeadline.setupWithViewPager(viewPagerHeadline);
        viewPagerHeadline.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            viewPagerHeadline.setCurrentItem(position,true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        newsFragment = new NewsFragment();
        categoryFragment = new CategoryFragment();

        viewPagerAdapter.addFragment(newsFragment,"Latest");
        viewPagerAdapter.addFragment(categoryFragment,"Category");

        viewPager.setAdapter(viewPagerAdapter);



    }
}
