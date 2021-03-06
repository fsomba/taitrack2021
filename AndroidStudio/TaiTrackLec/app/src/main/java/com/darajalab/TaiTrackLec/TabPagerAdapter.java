package com.darajalab.TaiTrackLec;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabPagerAdapter extends FragmentPagerAdapter {
    int tabCount;

    public TabPagerAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.tabCount = numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        //identify the page number being requested and return a corresponding fragment instance
        switch (position) {
            case 0:
                return new FragmentOverview();
            case 1:
                return new FragmentAttendance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}


