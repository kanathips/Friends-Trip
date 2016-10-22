package com.tinyandfriend.project.friendstrip;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.tinyandfriend.project.friendstrip.adapter.FragmentPagerAdapter;
import com.tinyandfriend.project.friendstrip.info.CreateTripInfo;
import com.tinyandfriend.project.friendstrip.view.NoSwipeViewPager;

import java.util.ArrayList;


public class CreateTripActivity extends AppCompatActivity {

    private NoSwipeViewPager viewPager;
    private ArrayList<FragmentPager> fragmentPagers;
    private CreateTripInfo tripInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_trip_page);

        FragmentManager fragmentManager = getSupportFragmentManager();


        fragmentPagers = new ArrayList<>();
        AddPlaceFragment addPlaceFragment = new AddPlaceFragment();

        AddTagFragment addTagFragment = new AddTagFragment();

        fragmentPagers.add(addPlaceFragment);
        fragmentPagers.add(addTagFragment);
        FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(fragmentManager, fragmentPagers);

        viewPager = (NoSwipeViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(pagerAdapter);
        tripInfo = new CreateTripInfo();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.next_page_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentPager currentFragment = fragmentPagers.get(viewPager.getCurrentItem());
        switch (item.getItemId()) {
            case (R.id.action_next):
                if (currentFragment.validateFrom()) {
                        currentFragment.setInfo(tripInfo);
                    if(viewPager.getCurrentItem() == 0) {
                        viewPager.setCurrentItem(1);
                    }else if(viewPager.getCurrentItem() == 1){
                        Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();

                    }
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
