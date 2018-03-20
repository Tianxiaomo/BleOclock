package com.my.bleoclock.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class MainSectionsPagerAdapter extends FragmentPagerAdapter {


    List<Fragment> fragmentList;

    public MainSectionsPagerAdapter(FragmentManager fm,List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);

    }

    @Override
    public int getCount() {
        int count = 0;
        if (fragmentList==null||fragmentList.size()==0) {
            count = 0;
        }else {
            count = fragmentList.size();
        }

        return count;
    }
}
