
package com.tinyandfriend.project.friendstrip.adapter;


import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;

import com.astuetz.PagerSlidingTabStrip;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tinyandfriend.project.friendstrip.R;
import com.tinyandfriend.project.friendstrip.fragment.FragmentFriendList;
import com.tinyandfriend.project.friendstrip.fragment.FragmentJoin;
import com.tinyandfriend.project.friendstrip.fragment.FragmentNotification;
import com.tinyandfriend.project.friendstrip.fragment.FragmentRoomDefault;
import com.tinyandfriend.project.friendstrip.fragment.FragmentRoomHost;
import com.tinyandfriend.project.friendstrip.fragment.FragmentRoomJoiner;


/**
 * Created by StandAlone on 10/10/2559.
 */


public class ContentFragmentPagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider{

    private String userUid;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private final int PAGE_COUNT = 4;
    public static String tabTitles[] = new String[]{"จัดการทริป","เข้าร่วมทริป", "การแจ้งเตือน", "คำร้องขอ"};
    private int tabIcons[] = {R.drawable.ic_directions_walk_black_18dp,R.drawable.ic_filter_hdr_black_18dp, R.drawable.ic_notifications_black_18dp, R.drawable.ic_group_black_18dp};
    private int joinType;
    private AppCompatActivity activity;

    public ContentFragmentPagerAdapter(FragmentManager fm, Activity activity)  {
        super(fm);
        this.activity = (AppCompatActivity) activity;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    @Override

    public int getCount() {
//        if(checkJoined == false)
//            return modelJoinedTrips.get(0).getCount()-1;
        return PAGE_COUNT;
    }

    @Override

    public Fragment getItem(int position) {
        if (userUid != null && !userUid.isEmpty()) {
            switch (position) {
                case 0:
                    switch (joinType){
                        case 0:
                            return new FragmentRoomHost();
                        case 1:
                            return new FragmentRoomJoiner();
                        case 2:
                            return new FragmentRoomDefault();
                    }
                    return new FragmentRoomDefault();
                case 1:
                    return new FragmentJoin();
                case 2:
                    return FragmentNotification.newInstance(userUid);
                case 3:
                    return FragmentFriendList.newInstance(userUid);
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


    public int getJoinType() {
        return joinType;
    }

    public void setJoinType(int joinType) {
        this.joinType = joinType;
    }
}