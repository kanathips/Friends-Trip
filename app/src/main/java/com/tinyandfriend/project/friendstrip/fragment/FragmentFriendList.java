package com.tinyandfriend.project.friendstrip.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tinyandfriend.project.friendstrip.R;
import com.tinyandfriend.project.friendstrip.activity.AddFriendActivity;
import com.tinyandfriend.project.friendstrip.adapter.FriendListAdapter;
import com.tinyandfriend.project.friendstrip.info.FriendListInfo;

import java.util.ArrayList;

public class FragmentFriendList extends Fragment {

    private static final String USER_UID = "userUid";
    private String userUid;
    private ArrayList<FriendListInfo> friendList;
    private FriendListAdapter friendListAdapter;
    private static final String STATUS_CHILD = "status";
    private static final String FRIEND_STATUS = "Friend";
    private static final String FRIEND_LIST_CHILD = "friendTerm";

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
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        RecyclerView recyclerView = (RecyclerView) rootview.findViewById(R.id.friend_list);

        friendList = new ArrayList<>();

        friendListAdapter = new FriendListAdapter(context, friendList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(friendListAdapter);
        friendListAdapter.notifyDataSetChanged();


        FloatingActionButton fab = (FloatingActionButton) rootview.findViewById(R.id.fab);
        fab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        FragmentManager manager = getFragmentManager();
//                        FragmentTransaction transaction = manager.beginTransaction();
//                        AddFriendActivity fragmentAddFriend = new AddFriendActivity();
//                        transaction.replace(R.id.fragment_container, fragmentAddFriend);
//                        transaction.addToBackStack("AddFriend");
//                        transaction.commit();
//                        FragmentTransaction ft = getFragmentManager().beginTransaction();
//                        ft.add(new AddFriendActivity(), null);
//                        ft.commit();
                        startActivity(new Intent(context, AddFriendActivity.class));
                    }
                }
        );

        getFriendList(reference, userUid);

        return rootview;
    }

    private void getFriendList(DatabaseReference reference, String userUid){
        reference.child(FRIEND_LIST_CHILD).child(userUid).orderByChild(STATUS_CHILD).equalTo(FRIEND_STATUS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    FriendListInfo friendListInfo = dataSnapshot.getValue(FriendListInfo.class);
                    friendList.add(friendListInfo);
                    friendListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
