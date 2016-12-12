package com.tinyandfriend.project.friendstrip.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tinyandfriend.project.friendstrip.ConstantValue;
import com.tinyandfriend.project.friendstrip.R;
import com.tinyandfriend.project.friendstrip.adapter.FriendListAdapter;
import com.tinyandfriend.project.friendstrip.info.FriendInfo;
import com.tinyandfriend.project.friendstrip.info.FriendStatus;

import java.util.ArrayList;

public class FriendListActivity extends AppCompatActivity {


    private ArrayList<FriendInfo> friendList;
    private FriendListAdapter friendListAdapter;

    private ChildEventListener listener;
    private DatabaseReference reference;
    private ValueEventListener friendProfileListener;
    private String userUid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userUid = user.getUid();
        reference = FirebaseDatabase.getInstance().getReference();
        friendList = new ArrayList<>();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.friend_list);



        friendListAdapter = new FriendListAdapter(this, friendList, reference);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(friendListAdapter);
        friendListAdapter.notifyDataSetChanged();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(FriendListActivity.this, AddFriendActivity.class));
                    }
                }
        );
        getFriendList(reference, userUid);

    }

    private void getFriendList(final DatabaseReference reference, String userUid) {

        listener = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    final FriendInfo friendInfo = dataSnapshot.getValue(FriendInfo.class);
                    FriendStatus status = friendInfo.getStatus();

                    if ((status != FriendStatus.Ban) && (status != FriendStatus.Approving) && !friendList.contains(friendInfo)) {
                        friendProfileListener = new ValueEventListener(){

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    String friendPhotoUrl = dataSnapshot.getValue(String.class);
                                    friendInfo.setFriendPhotoUrl(friendPhotoUrl);
                                }
                                if(!friendList.contains(friendInfo)) {
                                    friendList.add(friendInfo);
                                }
                                friendListAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        };
                        Log.i("FRIEND LIST", friendInfo.toString());
                        reference.child(ConstantValue.USERS_CHILD).child(friendInfo.getFriendUid()).child(ConstantValue.PROFILE_PHOTO_CHILD).addListenerForSingleValueEvent(friendProfileListener);
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    FriendInfo friendInfo = dataSnapshot.getValue(FriendInfo.class);
                    FriendStatus status = friendInfo.getStatus();
                    switch (status){
                        case Ban:
                            Toast.makeText(FriendListActivity.this, "You got ban from " + friendInfo.getFriendName(), Toast.LENGTH_SHORT).show();
                            friendList.remove(friendInfo);
                            break;
                        case Friend:
                            if(friendList.contains(friendInfo))
                                return;
                            friendList.add(friendInfo);
                            break;
                    }
                    friendListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    FriendInfo friendInfo = dataSnapshot.getValue(FriendInfo.class);
                    friendList.remove(friendInfo);
                    friendListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        reference.child(ConstantValue.FRIEND_LIST_CHILD).child(userUid).addChildEventListener(listener);


    }

    @Override
    public void onStop() {
        super.onStop();
        if (listener != null && reference != null)
            reference.removeEventListener(listener);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (listener != null && reference != null)
            reference.child(ConstantValue.FRIEND_LIST_CHILD).child(userUid).addChildEventListener(listener);
    }

}
