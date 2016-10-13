package com.tinyandfriend.project.friendstrip.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tinyandfriend.project.friendstrip.FragmentPager;

import java.util.ArrayList;

public class FragmentPagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<FragmentPager> fragments;

    public FragmentPagerAdapter(FragmentManager fm, ArrayList<FragmentPager> fragments) {
        super(fm);
        setFragments(fragments);
    }

    public void addFragment(FragmentPager fragment){
        fragments.add(fragment);
    }


    public ArrayList<FragmentPager> getFragments() {
        return fragments;
    }

    public void setFragments(ArrayList<FragmentPager> fragments) {
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return getFragments().get(position);
    }

    @Override
    public int getCount() {
        return getFragments().size();
    }
}