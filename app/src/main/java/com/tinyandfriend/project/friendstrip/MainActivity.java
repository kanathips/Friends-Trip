package com.tinyandfriend.project.friendstrip;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tinyandfriend.project.friendstrip.adapter.FragmentPagerAdapter_Content;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private String username;
    private FirebaseAuth.AuthStateListener authStateListener;
    private boolean stateFlag = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(stateFlag == false){
                    stateFlag = true;
                    return;
                }
                firebaseUser = firebaseAuth.getCurrentUser();
                String toastText = "";
                if (firebaseUser == null) {
                    toastText = "Please Sign In";
                    startActivity(new Intent(MainActivity.this, SignInActivity.class));
                    finish();
                } else if (!firebaseUser.isEmailVerified()) {
                    toastText = "Please verify your email";
                    startActivity(new Intent(MainActivity.this, ReVerifyEmailActivity.class));
                    finish();
                } else {
                    if ((username = firebaseUser.getDisplayName()) == null)
                        username = firebaseUser.getEmail();
                    toastText = "Welcome : " + username;
                }
                Toast.makeText(MainActivity.this, toastText, Toast.LENGTH_SHORT).show();
                stateFlag = false;
            }
        };

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
        );

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ////////////////////////////////////////////////////////// Fragement///////////////////////////////////////////////////////////
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new FragmentPagerAdapter_Content(getSupportFragmentManager()));

        // Give the PagerSlidingTabStrip the ViewPager
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(viewPager);

        tabsStrip.setOnPageChangeListener(
                new ViewPager.OnPageChangeListener(){

                    // This method will be invoked when a new page becomes selected.
                    @Override
                    public void onPageSelected(int position) {
                        switch (position){
                            case(0):
                                onSelectJoinPage();
                                break;
                            case(1):
                                onSelectFriendPage();
                                break;
                            case(2):
                                onSelectSomeThing();
                        }
                        Toast.makeText(MainActivity.this, "Selected page position: " + position, Toast.LENGTH_SHORT).show();
                    }

                    // This method will be invoked when the current page is scrolled
                    @Override
                    public void onPageScrolled(int position, float positionOffset,
                                               int positionOffsetPixels) {
                        // Code goes here
                    }

                    // Called when the scroll state changes:
                    // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
                    @Override
                    public void onPageScrollStateChanged(int state) {
                        // Code goes here
                    }
                }
        );
    }

    private void onSelectSomeThing() {
        FloatingActionButton actionButton = (FloatingActionButton) findViewById(R.id.fab);
        actionButton.setBackgroundResource(R.drawable.ic_location_city_white_36dp);
    }

    private void onSelectJoinPage() {
        FloatingActionButton actionButton = (FloatingActionButton) findViewById(R.id.fab);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CreateTripActivity.class));
            }
        });
        actionButton.setBackgroundResource(R.drawable.ic_directions_walk_white_36dp);
    }

    private void onSelectFriendPage(){
        FloatingActionButton actionButton = (FloatingActionButton) findViewById(R.id.fab);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddFriendsActivity.class));
            }
        });
        actionButton.setBackgroundResource(R.drawable.ic_group_white_36dp);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_log_out) {
            firebaseAuth.signOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

//    public void onDestroy() {
//        super.onDestroy();
//        if (authStateListener != null) {
//            firebaseAuth.removeAuthStateListener(authStateListener);
//        }
//    }
}
