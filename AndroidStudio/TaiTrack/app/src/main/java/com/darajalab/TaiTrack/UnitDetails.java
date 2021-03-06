package com.darajalab.TaiTrack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class UnitDetails extends AppCompatActivity {
    static String unit_code;
    static String unit_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_details);
        //get the items passed through the intent
        unit_code=getIntent().getStringExtra("UNIT_CODE");
        unit_name  = getIntent().getStringExtra("UNIT_NAME");

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        /* get TabLayout reference */
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        /* Add the needed tabs to the tablayout and their names */
        tabLayout.addTab(tabLayout.newTab().setText("Overview"));
        tabLayout.addTab(tabLayout.newTab().setText("Attendance"));
        //get the viewpager component
        final ViewPager viewPager = findViewById(R.id.pager);
        //call TabPageAdapter class and pass it the number of tabs created above
        final PagerAdapter adapter = new TabPagerAdapter(getSupportFragmentManager(),
                tabLayout.getTabCount());
        //bind the adapter to the viewpager
        viewPager.setAdapter(adapter);
        //set a listener on the viewpager
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new
           TabLayout.OnTabSelectedListener() {
               @Override
               public void onTabSelected(TabLayout.Tab tab) {
                   viewPager.setCurrentItem(tab.getPosition());
               }

               @Override
               public void onTabUnselected(TabLayout.Tab tab) {

               }

               @Override
               public void onTabReselected(TabLayout.Tab tab) {

               }

           });
    }

}