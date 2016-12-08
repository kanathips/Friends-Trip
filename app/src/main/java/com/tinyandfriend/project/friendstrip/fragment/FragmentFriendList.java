package com.tinyandfriend.project.friendstrip.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    private ValueEventListener friendProfileListener;
    private Context context;

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

        reference = FirebaseDatabase.getInstance().getReference();
        friendList = new ArrayList<>();

        if (getArguments() != null) {
            userUid = getArguments().getString(USER_UID);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_friend_list, container, false);
        RecyclerView recyclerView = (RecyclerView) rootview.findViewById(R.id.friend_list);



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
                            Toast.makeText(context, "You got ban from " + friendInfo.getFriendName(), Toast.LENGTH_SHORT).show();
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
