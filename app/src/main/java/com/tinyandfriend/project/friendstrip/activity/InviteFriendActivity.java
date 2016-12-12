package com.tinyandfriend.project.friendstrip.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.tinyandfriend.project.friendstrip.ConstantValue;
import com.tinyandfriend.project.friendstrip.R;
import com.tinyandfriend.project.friendstrip.adapter.InviteFriendListAdapter;
import com.tinyandfriend.project.friendstrip.info.FriendInfo;
import com.tinyandfriend.project.friendstrip.info.FriendStatus;
import com.tinyandfriend.project.friendstrip.info.notification.InviteNotification;

import java.util.ArrayList;

public class InviteFriendActivity extends AppCompatActivity {

    private String tripId;
    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String userUid;
    private ChildEventListener listener;
    private ArrayList<FriendInfo> friendList;
    private InviteFriendListAdapter inviteFriendListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friend);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        tripId = intent.getStringExtra("tripId");
        userUid = user.getUid();
        friendList = new ArrayList<>();

        inviteFriendListAdapter = new InviteFriendListAdapter(this, friendList);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.friend_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(inviteFriendListAdapter);

        listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                FriendInfo friendInfo = dataSnapshot.getValue(FriendInfo.class);

                if(friendInfo.getStatus() == FriendStatus.Friend){
                    friendList.add(friendInfo);
                    inviteFriendListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                FriendInfo friendInfo = dataSnapshot.getValue(FriendInfo.class);
                switch (friendInfo.getStatus()){
                    case Ban:
                        if(friendList.contains(friendInfo)){
                            friendList.remove(friendInfo);
                            inviteFriendListAdapter.notifyDataSetChanged();
                        }
                        break;
                    case Friend:
                        friendList.add(friendInfo);
                        inviteFriendListAdapter.notifyDataSetChanged();
                        break;
                }


            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                FriendInfo friendInfo = dataSnapshot.getValue(FriendInfo.class);

                if(friendList.contains(friendInfo)){
                    friendList.remove(friendInfo);
                    inviteFriendListAdapter.notifyDataSetChanged();
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

        FloatingActionButton inviteButton = (FloatingActionButton) findViewById(R.id.invite_button);
        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<FriendInfo> inviteList = inviteFriendListAdapter.getInviteList();
                for(FriendInfo friendInfo: inviteList){
                    InviteNotification inviteNotification = new InviteNotification(user.getDisplayName(), user.getPhotoUrl().toString(), tripId);
                    reference.child(ConstantValue.NOTIFICATION_CHILD).child(friendInfo.getFriendUid()).push().setValue(inviteNotification);
                    Toast.makeText(InviteFriendActivity.this, "ชวนเพื่อนเรียบร้อยแล้ว", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        reference.child(ConstantValue.FRIEND_LIST_CHILD).child(userUid).removeEventListener(listener);
    }
}
