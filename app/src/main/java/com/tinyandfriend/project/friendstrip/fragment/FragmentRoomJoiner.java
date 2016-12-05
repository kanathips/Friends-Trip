package com.tinyandfriend.project.friendstrip.fragment;


import android.app.Dialog;
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
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.maps.MapView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tinyandfriend.project.friendstrip.ConstantValue;
import com.tinyandfriend.project.friendstrip.R;
import com.tinyandfriend.project.friendstrip.activity.ChatActivity;
import com.tinyandfriend.project.friendstrip.adapter.FriendListAdapter;
import com.tinyandfriend.project.friendstrip.info.FriendInfo;

import java.util.ArrayList;


public class FragmentRoomJoiner extends Fragment {

    MapView mapView;
    private DatabaseReference reference;
    private FirebaseUser user;
    private String userUid;
    private int type;
    private String tripId;
    private FriendListAdapter friendListAdapter;
    private Context context;

    public FragmentRoomJoiner() {
        // Required empty public constructor
    }

    public static FragmentRoomJoiner newInstance(String userUid, String tripId) {
        FragmentRoomJoiner fragment = new FragmentRoomJoiner();
        Bundle args = new Bundle();
        args.putString(ConstantValue.USER_UID, userUid);
        args.putString("tripId", tripId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        reference = FirebaseDatabase.getInstance().getReference();

        if (getArguments() != null) {
            userUid = getArguments().getString(ConstantValue.USER_UID);
            tripId = getArguments().getString("tripId");
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
        final View view = inflater.inflate(R.layout.fragment_room, container, false);
        createMap(view,savedInstanceState);

        Button chat_bt = (Button) view.findViewById(R.id.chat_detail);
        Button joiner_list = (Button) view.findViewById(R.id.joiner_list);
        chat_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                startActivity(intent);
            }
        });


        joiner_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.viewpager_dialog_joiner);
                dialog.setCancelable(true);

                final RecyclerView joinerList = (RecyclerView) dialog.findViewById(R.id.joiner_list);
                final ArrayList<FriendInfo> members = new ArrayList<>();
                reference.child(ConstantValue.TRIP_ROOM_CHILD).child(tripId).child("members").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                                if(!members.contains(new FriendInfo(dataSnapshot.getKey()))){
                                    String photo = snapshot.child("photo").getValue(String.class);
                                    String name = snapshot.child("name").getValue(String.class);
                                    String uid = snapshot.child("uid").getValue(String.class);
                                    FriendInfo friendInfo = new FriendInfo(photo, name, uid);
                                    members.add(friendInfo);
                                }
                            }

                            friendListAdapter = new FriendListAdapter(context, members, reference);
                            joinerList.setAdapter(friendListAdapter);
                            joinerList.setLayoutManager(new LinearLayoutManager(context));
                            joinerList.setItemAnimator(new DefaultItemAnimator());
                            friendListAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



                dialog.show();
            }
        });

        FloatingActionButton exitFab = (FloatingActionButton) view.findViewById(R.id.out_trip);
        exitFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child(ConstantValue.USERS_CHILD).child(userUid).child(ConstantValue.TRIP_ID_CHILD).removeValue();
                reference.child(ConstantValue.TRIP_ROOM_CHILD).child(tripId).child("members").child(userUid).removeValue();
            }
        });

        return view;
    }

    public void createMap(View view,Bundle bundle){
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(bundle);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    public void onPause() {
        if (mapView != null) {
            mapView.onPause();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (mapView != null) {
            try {
                mapView.onDestroy();
            } catch (NullPointerException e) {
                Log.e("1", "Error while attempting MapView.onDestroy(), ignoring exception", e);
            }
        }
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null) {
            mapView.onLowMemory();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapView != null) {
            mapView.onSaveInstanceState(outState);
        }
    }
}
