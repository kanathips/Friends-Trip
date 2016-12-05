
package com.tinyandfriend.project.friendstrip.adapter;


import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tinyandfriend.project.friendstrip.R;
import com.tinyandfriend.project.friendstrip.fragment.FragmentFindTrip;
import com.tinyandfriend.project.friendstrip.fragment.FragmentFriendList;
import com.tinyandfriend.project.friendstrip.fragment.FragmentNotification;
import com.tinyandfriend.project.friendstrip.fragment.FragmentRoomDefault;
import com.tinyandfriend.project.friendstrip.fragment.FragmentRoomHost;
import com.tinyandfriend.project.friendstrip.fragment.FragmentRoomJoiner;
import com.tinyandfriend.project.friendstrip.fragment.FragmentTripFriendJoined;


/**
 * Created by StandAlone on 10/10/2559.
 */


public class ContentFragmentPagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider{

    private String userUid;
    private final int PAGE_COUNT = 4;
    public static String tabTitles[] = new String[]{"จัดการทริป","เข้าร่วมทริป", "การแจ้งเตือน", "คำร้องขอ"};
    private int tabIcons[] = {R.drawable.ic_directions_walk_black_18dp,R.drawable.ic_filter_hdr_black_18dp, R.drawable.ic_notifications_black_18dp, R.drawable.ic_group_black_18dp};
    private int joinType;

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    private String tripId;
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
        return PAGE_COUNT;
    }

    @Override

    public Fragment getItem(int position) {
        if (userUid != null && !userUid.isEmpty()) {
            switch (position) {
                case 0:
                    Toast.makeText(activity, "Join Type : " + joinType, Toast.LENGTH_SHORT).show();
                    switch (joinType){
                        case 0:
                            return FragmentRoomHost.newInstance(userUid, tripId);
                        case 1:
                            return FragmentRoomJoiner.newInstance(userUid, tripId);
                        case 2:
//                            return FragmentRoomDefault.newInstance(userUid, tripId);
                    }
                    return new FragmentRoomDefault();
                case 1:
                    return FragmentFindTrip.newInstance(userUid);
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
        Toast.makeText(activity, "In Type : " + joinType, Toast.LENGTH_SHORT).show();
        this.joinType = joinType;
    }

}