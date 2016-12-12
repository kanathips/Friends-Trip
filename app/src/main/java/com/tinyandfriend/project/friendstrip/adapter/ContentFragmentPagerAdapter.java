
package com.tinyandfriend.project.friendstrip.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.astuetz.PagerSlidingTabStrip;
import com.tinyandfriend.project.friendstrip.R;
import com.tinyandfriend.project.friendstrip.fragment.FragmentFindTrip;
import com.tinyandfriend.project.friendstrip.fragment.FragmentFriendNotification;
import com.tinyandfriend.project.friendstrip.fragment.FragmentRoomDefault;


public class ContentFragmentPagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider{

    private String userUid;
    public static String tabTitles[] = new String[]{"จัดการทริป","เข้าร่วมทริป", "การแจ้งเตือน"};
    private int tabIcons[] = {R.drawable.ic_directions_walk_black_18dp,R.drawable.ic_filter_hdr_black_18dp, R.drawable.ic_notifications_black_18dp};
    private FragmentRoomDefault tripRoom;

    public ContentFragmentPagerAdapter(FragmentManager fm)  {
        super(fm);
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    @Override

    public int getCount() {
        return 3;
    }

    @Override

    public Fragment getItem(int position) {
        if (userUid != null && !userUid.isEmpty()) {
            switch (position) {
                case 0:
                    tripRoom = FragmentRoomDefault.newInstance(userUid);
                    return tripRoom;
                case 1:
                    return FragmentFindTrip.newInstance(userUid);
                case 2:
                    return FragmentFriendNotification.newInstance(userUid);
            }
        }

        return null;

    }


    @Override

    public CharSequence getPageTitle(int position) {
        return tabTitles[position];

    }


    @Override

    public int getPageIconResId(int position) {
        return tabIcons[position];
    }

    public String getTripId(){
        return  tripRoom.getTripId();
    }
}