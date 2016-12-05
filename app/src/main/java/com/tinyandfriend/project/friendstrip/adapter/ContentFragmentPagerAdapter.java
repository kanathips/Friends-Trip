
package com.tinyandfriend.project.friendstrip.adapter;


import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;

import android.support.v4.app.FragmentPagerAdapter;


import com.astuetz.PagerSlidingTabStrip;


import com.tinyandfriend.project.friendstrip.R;
import com.tinyandfriend.project.friendstrip.fragment.FragmentFriendList;
import com.tinyandfriend.project.friendstrip.fragment.FragmentFriendNotification;
import com.tinyandfriend.project.friendstrip.fragment.FragmentJoin;
import com.tinyandfriend.project.friendstrip.fragment.FragmentNotification;


/**
 * Created by StandAlone on 10/10/2559.
 */


public class ContentFragmentPagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider {

    private String userUid;

    private String tabTitles[] = new String[]{"Current Trip","Find Trip", "Notification", "Friend Request"};

    private int tabIcons[] = {R.drawable.ic_directions_walk_black_18dp, R.drawable.ic_notifications_black_18dp,R.drawable.ic_notifications_black_18dp, R.drawable.ic_group_black_18dp};


    public ContentFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    @Override
    public int getCount() {

        return 4;

    }


    @Override

    public Fragment getItem(int position) {

        if (userUid != null && !userUid.isEmpty()) {
            switch (position) {
                case 0:
                    return FragmentJoin.newInstance(userUid);
                case 1:
                    return FragmentFriendList.newInstance(userUid);
                case 2:
                    return FragmentNotification.newInstance(userUid);
                case 3:
                    return FragmentFriendNotification.newInstance(userUid);
            }
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