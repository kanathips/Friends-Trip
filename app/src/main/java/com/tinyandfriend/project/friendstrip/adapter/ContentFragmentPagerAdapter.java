
package com.tinyandfriend.project.friendstrip.adapter;



        import android.support.v4.app.Fragment;

        import android.support.v4.app.FragmentManager;

        import android.support.v4.app.FragmentPagerAdapter;



        import com.astuetz.PagerSlidingTabStrip;


        import com.tinyandfriend.project.friendstrip.R;
        import com.tinyandfriend.project.friendstrip.fragment.FragmentFriendList;
        import com.tinyandfriend.project.friendstrip.fragment.FragmentJoin;
        import com.tinyandfriend.project.friendstrip.fragment.FragmentNotification;


/**

 * Created by StandAlone on 10/10/2559.

 */



public class ContentFragmentPagerAdapter extends FragmentPagerAdapter  implements PagerSlidingTabStrip.IconTabProvider {

    private final int PAGE_COUNT = 3;
    private String userUid;

    private String tabTitles[] = new String[] { "JOIN", "NOTIFICATION", "FRIENDS" };

    private int tabIcons[] = {R.drawable.ic_directions_walk_white_36dp, R.drawable.ic_announcement_white_36dp, R.drawable.ic_group_white_36dp};



    public ContentFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
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

        switch(position){

            case 0: return new FragmentJoin();

            case 1: return new FragmentNotification();

            case 2: return FragmentFriendList.newInstance(userUid);

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