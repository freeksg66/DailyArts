package com.github.dailyarts.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by legao005426 on 2018/5/3.
 */

public class TabFragmentPagerAdapter extends FragmentPagerAdapter {
    private static String TAG = "TabFragmentPagerAdapter";

    private FragmentManager mfragmentManager;
    private List<Fragment> mFragmentList;

    public TabFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        mFragmentList = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
