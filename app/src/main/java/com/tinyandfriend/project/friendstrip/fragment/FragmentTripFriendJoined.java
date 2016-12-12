package com.tinyandfriend.project.friendstrip.fragment;

/**
 * Created by StandAlone on 12/10/2559.
 */

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tinyandfriend.project.friendstrip.ConstantValue;
import com.tinyandfriend.project.friendstrip.GridSpacingItemDecoration;
import com.tinyandfriend.project.friendstrip.R;
import com.tinyandfriend.project.friendstrip.adapter.TripCardViewAdapter;
import com.tinyandfriend.project.friendstrip.info.FriendInfo;
import com.tinyandfriend.project.friendstrip.info.TripCardViewInfo;
import com.tinyandfriend.project.friendstrip.info.TripInfo;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class FragmentTripFriendJoined extends Fragment {

    private List<TripCardViewInfo> tripList;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private ArrayList<String> rooms;
    private Context context;
    int pixelWidth;
    int pixelHeight;
    private static final String USER_UID = "userUid";
    private String userUid;
    private ScaleInAnimationAdapter alphaAdapter;

    public static FragmentTripFriendJoined newInstance(String userUid) {
        FragmentTripFriendJoined fragment = new FragmentTripFriendJoined();
        Bundle args = new Bundle();
        args.putString(USER_UID, userUid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        reference = FirebaseDatabase.getInstance().getReference();
        pixelWidth = getResources().getDisplayMetrics().widthPixels;
        pixelHeight = getResources().getDisplayMetrics().heightPixels;
        tripList = new ArrayList<>();
        rooms = new ArrayList<>();

        if (getArguments() != null) {
            userUid = getArguments().getString(USER_UID);
        }

        TripCardViewAdapter adapter = new TripCardViewAdapter(context, tripList, pixelWidth, pixelHeight);
        alphaAdapter = new ScaleInAnimationAdapter(adapter);
        alphaAdapter.setFirstOnly(false);
        alphaAdapter.setDuration(250);

        reference.child(ConstantValue.FRIEND_LIST_CHILD).child(userUid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                {
                    String friendId = dataSnapshot.getKey();
                    reference.child(ConstantValue.USERS_CHILD).child(friendId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            FriendInfo friendInfo = dataSnapshot.getValue(FriendInfo.class);
                            String tripId = friendInfo.getTripId();
                            if (tripId == null || rooms.contains(tripId)) {
                                return;
                            }
                            reference.child(ConstantValue.TRIP_ROOM_CHILD).child(tripId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    TripInfo tripInfo = dataSnapshot.getValue(TripInfo.class);
                                    String key = dataSnapshot.getKey();
                                    tripInfo.setId(key);
                                    if (rooms.contains(key) || tripInfo.getStatus().equals("close")) {
                                        if(rooms.contains(key) && tripInfo.getStatus().equals("close")) {
                                            rooms.remove(key);
                                            tripList.remove(tripList.indexOf(new TripCardViewInfo(key)));
                                            alphaAdapter.notifyDataSetChanged();
                                        }
                                        return;
                                    }
                                    rooms.add(dataSnapshot.getKey());
                                    TripCardViewInfo tripCardViewInfo = new TripCardViewInfo(key, tripInfo.getTripName(),
                                            tripInfo.getStartDate(), tripInfo.getEndDate(), tripInfo.getMaxMember(), tripInfo.getTripSpoil(), tripInfo.getThumbnail());
                                    tripList.add(0, tripCardViewInfo);
                                    alphaAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_trip_friend_joined, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(context, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setAdapter(alphaAdapter);

        return rootView;
    }

    /////////////////////////////////////////////////////// Start class CardView /////////////////////////////////////////////////////


    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}