package com.tinyandfriend.project.friendstrip;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.astuetz.PagerSlidingTabStrip;

/**
 * Created by StandAlone on 10/10/2559.
 */

public class FragmentPagerAdapter_Content extends FragmentPagerAdapter  implements PagerSlidingTabStrip.IconTabProvider {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] { "Tab1", "Tab2", "Tab3" };
    private int tabIcons[] = {R.drawable.ic_directions_walk_white_36dp, R.drawable.ic_location_city_white_36dp, R.drawable.ic_group_white_36dp};

    public FragmentPagerAdapter_Content(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return PageFragment.newInstance(position + 1);
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