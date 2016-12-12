package com.tinyandfriend.project.friendstrip.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;

import com.tinyandfriend.project.friendstrip.adapter.ContentFragmentPagerAdapter;

import java.io.Serializable;

/**
 * Created by StandAlone on 4/12/2559.
 */

public class MainViewPager extends ViewPager implements Serializable {
    private AppCompatActivity activity;

    public MainViewPager(Context context) {
        super(context);
        activity = (AppCompatActivity) context;
    }

    public MainViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        activity = (AppCompatActivity) context;
    }


    @Override
    protected void onPageScrolled(int position, float offset, int offsetPixels) {
        super.onPageScrolled(position, offset, offsetPixels);
        activity.setTitle(ContentFragmentPagerAdapter.tabTitles[position]);
    }
}
