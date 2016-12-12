package com.tinyandfriend.project.friendstrip.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tinyandfriend.project.friendstrip.ConstantValue;
import com.tinyandfriend.project.friendstrip.R;
import com.tinyandfriend.project.friendstrip.adapter.ContentFragmentPagerAdapter;
import com.tinyandfriend.project.friendstrip.adapter.InviteFriendListAdapter;
import com.tinyandfriend.project.friendstrip.info.UserInfo;
import com.tinyandfriend.project.friendstrip.view.MainViewPager;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private String username;
    private String userEmail;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private boolean stateFlag = true;
    private String userUid;
    private ContentFragmentPagerAdapter contentFragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final View header = navigationView.getHeaderView(0);

        contentFragmentPagerAdapter = new ContentFragmentPagerAdapter(getSupportFragmentManager());

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (!stateFlag) {
                    stateFlag = true;
                    return;
                }
                user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(MainActivity.this, SignInActivity.class));
                    finish();
                } else if (!user.isEmailVerified()) {
                    startActivity(new Intent(MainActivity.this, SignInActivity.class));
                    finish();
                } else {
                    final CircleImageView circleImageView = (CircleImageView) header.findViewById(R.id.user_profile_photo);
                    TextView username_nav = (TextView) header.findViewById(R.id.username_nav_header);
                    TextView email_nav = (TextView) header.findViewById(R.id.email_nav_header);
                    username = user.getDisplayName();
                    userEmail = user.getEmail();
                    userUid = user.getUid();
                    username_nav.setText(username);
                    email_nav.setText(userEmail);

                    reference.child(ConstantValue.USERS_CHILD).child(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);

                            if (userInfo.getProfilePhoto() != null && !userInfo.getProfilePhoto().isEmpty()) {
                                Glide.with(MainActivity.this)
                                        .load(userInfo.getProfilePhoto()).centerCrop()
                                        .into(circleImageView);
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                    contentFragmentPagerAdapter.setUserUid(user.getUid());

                    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

                    setSupportActionBar(toolbar);

                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                            MainActivity.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                    drawer.setDrawerListener(toggle);
                    toggle.syncState();

                    ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
                    viewPager.setAdapter(contentFragmentPagerAdapter);
                    contentFragmentPagerAdapter.setViewPager(viewPager);

                    PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
                    tabsStrip.setViewPager(viewPager);
                }
                stateFlag = false;
            }
        };
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.nav_profile:
                startActivity(new Intent(this, EditUserInfoActivity.class));
                break;
            case R.id.nav_friend_list:
                intent = new Intent(this, FriendListActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_log_out:
                firebaseAuth.signOut();
                startActivity(new Intent(this, SignInActivity.class));
                finish();
                break;
            case R.id.nav_invite:
                intent = new Intent(this, InviteFriendActivity.class);
                String tripId = contentFragmentPagerAdapter.getTripId();
                intent.putExtra("tripId", tripId);
                startActivity(intent);
                break;
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
}


