package com.tinyandfriend.project.friendstrip.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.astuetz.PagerSlidingTabStrip;
import com.tinyandfriend.project.friendstrip.fragment.FragmentNotification;
import com.tinyandfriend.project.friendstrip.fragment.FragmentFriends;
import com.tinyandfriend.project.friendstrip.fragment.FragmentJoin;
import com.tinyandfriend.project.friendstrip.R;

/**
 * Created by StandAlone on 10/10/2559.
 */

public class ContentFragmentPagerAdapter extends FragmentPagerAdapter  implements PagerSlidingTabStrip.IconTabProvider {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] { "JOIN", "CREATE", "FRIENDS" };
    private int tabIcons[] = {R.drawable.ic_directions_walk_white_36dp, R.drawable.ic_announcement_white_36dp, R.drawable.ic_group_white_36dp};

    public ContentFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0: return new FragmentJoin();
            case 1: return new FragmentNotification();
            case 2: return new FragmentFriends();
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }



    @Override
    public int getPageIconResId(int position) {
        return tabIcons[position];
    }
}