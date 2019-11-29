package com.example.lenovo.fragment_test;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


import java.util.List;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragmentList;

    public ViewPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    /**
     * 返回fragment
     *
     * @param i
     * @return
     */
    @Override
    public Fragment getItem(int i) {
        return fragmentList.get(i);
    }

    /**
     * 返回总数
     *
     * @return
     */
    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


}
