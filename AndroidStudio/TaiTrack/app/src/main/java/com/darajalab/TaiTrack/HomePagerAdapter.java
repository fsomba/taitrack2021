package com.darajalab.TaiTrack;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class HomePagerAdapter extends FragmentPagerAdapter {
    int tabCount;

    public HomePagerAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.tabCount = numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        //identify the page number being requested and return a corresponding fragment instance
        switch (position) {
            case 0:
                return new FragmentMyUnits();
            case 1:
                return new FragmentAddUnits();
            case 2:
                return new FragmentMyProfile();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
