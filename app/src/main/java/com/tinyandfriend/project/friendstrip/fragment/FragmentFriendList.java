package com.tinyandfriend.project.friendstrip.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tinyandfriend.project.friendstrip.ConstantValue;
import com.tinyandfriend.project.friendstrip.R;
import com.tinyandfriend.project.friendstrip.activity.AddFriendActivity;
import com.tinyandfriend.project.friendstrip.adapter.FriendListAdapter;
import com.tinyandfriend.project.friendstrip.info.FriendInfo;
import com.tinyandfriend.project.friendstrip.info.FriendStatus;

import java.util.ArrayList;

public class FragmentFriendList extends Fragment {


    private String userUid;
    private ArrayList<FriendInfo> friendList;
    private FriendListAdapter friendListAdapter;

    private static final String USER_UID = "userUid";
    private ChildEventListener listener;
    private DatabaseReference reference;

    public static FragmentFriendList newInstance(String userUid) {
        FragmentFriendList fragment = new FragmentFriendList();
        Bundle args = new Bundle();
        args.putString(USER_UID, userUid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userUid = getArguments().getString(USER_UID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_friend, container, false);
        final Context context = getContext();
        reference = FirebaseDatabase.getInstance().getReference();
        RecyclerView recyclerView = (RecyclerView) rootview.findViewById(R.id.friend_list);

        friendList = new ArrayList<>();

        friendListAdapter = new FriendListAdapter(context, friendList, reference);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(friendListAdapter);
        friendListAdapter.notifyDataSetChanged();


        FloatingActionButton fab = (FloatingActionButton) rootview.findViewById(R.id.fab);
        fab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(context, AddFriendActivity.class));
                    }
                }
        );

        getFriendList(reference, userUid);

        return rootview;
    }

    private void getFriendList(DatabaseReference reference, String userUid) {

        listener = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    FriendInfo friendInfo = dataSnapshot.getValue(FriendInfo.class);
                    FriendStatus status = friendInfo.getStatus();

                    if ((status != FriendStatus.Ban) && (status != FriendStatus.Approving) && !friendList.contains(friendInfo)) {
                        friendList.add(friendInfo);
                    }
                    friendListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    FriendInfo friendInfo = dataSnapshot.getValue(FriendInfo.class);
                    FriendStatus status = friendInfo.getStatus();
                    switch (status){
                        case Ban:
                            friendList.remove(friendInfo);
                            break;
                        case Friend:
                            friendList.add(friendInfo);
                            break;
                    }
                    friendListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

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
