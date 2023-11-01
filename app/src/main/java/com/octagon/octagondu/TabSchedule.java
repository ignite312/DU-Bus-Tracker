package com.octagon.octagondu;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;

public class TabSchedule extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    TabAdapter myViewPagerAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_tab);

        /*Got the Bus Name and Flag*/
        String busName = getIntent().getStringExtra("busName");
        String flag = getIntent().getStringExtra("flag");

        /* Create a Bundle with the busName and flag */
        Bundle args = new Bundle();
        args.putString("busName", busName);
        args.putString("flag", flag);

        /*Toolbar*/
        MaterialToolbar detailsBusToolbar = findViewById(R.id.toolbar);
        detailsBusToolbar.setTitle(busName);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            Drawable blackArrow = ContextCompat.getDrawable(this, R.drawable.baseline_arrow_back_24);
            actionBar.setHomeAsUpIndicator(blackArrow);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 =findViewById(R.id.view_pager);
        myViewPagerAdapter = new TabAdapter(this, busName, flag);
        viewPager2.setAdapter(myViewPagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });


    }
}