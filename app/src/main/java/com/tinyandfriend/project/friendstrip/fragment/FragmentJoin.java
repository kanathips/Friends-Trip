package com.tinyandfriend.project.friendstrip.fragment;

/**
 * Created by StandAlone on 12/10/2559.
 */

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tinyandfriend.project.friendstrip.ConstantValue;
import com.tinyandfriend.project.friendstrip.GridSpacingItemDecoration;
import com.tinyandfriend.project.friendstrip.R;
import com.tinyandfriend.project.friendstrip.activity.CreateTripActivity;
import com.tinyandfriend.project.friendstrip.adapter.TripCardViewAdapter;
import com.tinyandfriend.project.friendstrip.info.TripCardViewInfo;
import com.tinyandfriend.project.friendstrip.info.TripInfo;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class FragmentJoin extends Fragment {

    private List<TripCardViewInfo> tripList;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private ArrayList<String> rooms;
    private Context context;
    int pixelWidth;
    int pixelHeight;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment__join, container, false);


        pixelWidth = getResources().getDisplayMetrics().widthPixels;
        pixelHeight = getResources().getDisplayMetrics().heightPixels;

        tripList = new ArrayList<>();
        TripCardViewAdapter adapter = new TripCardViewAdapter(getActivity(), tripList,pixelWidth,pixelHeight);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));

        final ScaleInAnimationAdapter alphaAdapter = new ScaleInAnimationAdapter(adapter);
        alphaAdapter.setFirstOnly(false);
        alphaAdapter.setDuration(250);
        recyclerView.setAdapter(alphaAdapter);

        rooms = new ArrayList<>();

        context = getContext();

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(context, CreateTripActivity.class));
                    }
                }
        );

        reference.child(ConstantValue.TRIP_ROOM_CHILD).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                {
                    String key = dataSnapshot.getKey();
                    if (key != null && !rooms.contains(key)) {
                        rooms.add(dataSnapshot.getKey());
                        TripInfo tripInfo = dataSnapshot.getValue(TripInfo.class);
                        TripCardViewInfo tripCardViewInfo = new TripCardViewInfo(key,tripInfo.getTripName(),
                                tripInfo.getStartDate(), tripInfo.getEndDate(), tripInfo.getMaxMember(),tripInfo.getTripSpoil(), tripInfo.getThumbnail());
                        tripList.add(0, tripCardViewInfo);
                        alphaAdapter.notifyDataSetChanged();
                    }
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

        return rootView;
    }

    /////////////////////////////////////////////////////// Start class CardView /////////////////////////////////////////////////////



    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}