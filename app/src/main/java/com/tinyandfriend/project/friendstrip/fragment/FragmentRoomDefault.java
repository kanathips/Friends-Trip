package com.tinyandfriend.project.friendstrip.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.MapView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tinyandfriend.project.friendstrip.ConstantValue;
import com.tinyandfriend.project.friendstrip.R;


public class FragmentRoomDefault extends Fragment {

    MapView mapView;
    private DatabaseReference reference;
    private String userUid;
    private FragmentManager fragmentManager;
    String tripId;

    public FragmentRoomDefault() {
        // Required empty public constructor
    }

    public static FragmentRoomDefault newInstance(String userUid) {
        FragmentRoomDefault fragment = new FragmentRoomDefault();
        Bundle args = new Bundle();
        args.putString(ConstantValue.USER_UID, userUid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentManager = getChildFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame, new FragmentRoomBeforeJoin());
        transaction.commit();

        reference = FirebaseDatabase.getInstance().getReference();

        if (getArguments() != null) {
            userUid = getArguments().getString(ConstantValue.USER_UID);
        }

        reference.child(ConstantValue.USERS_CHILD).child(userUid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (!dataSnapshot.getKey().equals(ConstantValue.TRIP_ID_CHILD))
                    return;
                if (dataSnapshot.exists()) {
                    tripId = dataSnapshot.getValue(String.class);
                    reference.child(ConstantValue.TRIP_ROOM_CHILD).child(tripId).child(ConstantValue.OWNER_UID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String ownerUID = dataSnapshot.getValue(String.class);
                                FragmentTransaction transaction = fragmentManager.beginTransaction();
                                if (userUid.equals(ownerUID)) {
                                    transaction.replace(R.id.frame, FragmentRoomHost.newInstance(userUid, tripId));
                                } else {
                                    transaction.replace(R.id.frame, FragmentRoomJoiner.newInstance(userUid, tripId));
                                }
                                transaction.commit();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    tripId = null;
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.getKey().equals(ConstantValue.TRIP_ID_CHILD))
                    return;
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frame, new FragmentRoomBeforeJoin());
                transaction.commit();
                tripId = null;
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return inflater.inflate(R.layout.fragment_default_room, container, false);
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

    public String getTripId() {
        return tripId;
    }
}
