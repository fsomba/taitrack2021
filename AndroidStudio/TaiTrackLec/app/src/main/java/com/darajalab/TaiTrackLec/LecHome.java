package com.darajalab.TaiTrackLec;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

public class LecHome extends AppCompatActivity {
    //initialize variables
    String username;
    public static String STAFFID;
    TextView txt_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lec_home);
        //reference views

        //get values passed from login activity
        username=getIntent().getStringExtra("username");
        STAFFID = getIntent().getStringExtra("STAFFID");

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        /* get TabLayout reference */
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        /* Add the needed tabs to the tablayout and their names */
        tabLayout.addTab(tabLayout.newTab().setText("My Units"));
        tabLayout.addTab(tabLayout.newTab().setText("Add Unit(s)"));
        //get the viewpager component
        final ViewPager viewPager = findViewById(R.id.pager);
        //call HomePageAdapter class and pass it the number of tabs created above
        final PagerAdapter adapter = new HomePagerAdapter(getSupportFragmentManager(),
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