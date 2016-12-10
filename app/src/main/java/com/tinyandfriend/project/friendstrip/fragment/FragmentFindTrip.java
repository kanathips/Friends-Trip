package com.tinyandfriend.project.friendstrip.fragment;

/**
 * Created by StandAlone on 12/10/2559.
 */

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
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
import com.tinyandfriend.project.friendstrip.GridSpacingItemDecoration;
import com.tinyandfriend.project.friendstrip.R;
import com.tinyandfriend.project.friendstrip.activity.CreateTripActivity;
import com.tinyandfriend.project.friendstrip.adapter.TripCardViewAdapter;
import com.tinyandfriend.project.friendstrip.info.FriendInfo;
import com.tinyandfriend.project.friendstrip.info.TripCardViewInfo;
import com.tinyandfriend.project.friendstrip.info.TripInfo;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class FragmentFindTrip extends Fragment {

    private List<TripCardViewInfo> tripList;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private ArrayList<String> rooms;
    private Context context;
    int pixelWidth;
    int pixelHeight;
    private static final String USER_UID = "userUid";
    private String userUid;
    private boolean flag = false;
    private FragmentTripFriendJoined tripFriendJoined;
    private FragmentFindTripWithTag findTripWithTag;
    private FragmentManager fragmentManager;

    public static FragmentFindTrip newInstance(String userUid) {
        FragmentFindTrip fragment = new FragmentFindTrip();
        Bundle args = new Bundle();
        args.putString(USER_UID, userUid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        reference = FirebaseDatabase.getInstance().getReference();

        if (getArguments() != null) {
            userUid = getArguments().getString(USER_UID);
        }
        tripFriendJoined = FragmentTripFriendJoined.newInstance(userUid);
        findTripWithTag = new FragmentFindTripWithTag();
        fragmentManager = getChildFragmentManager();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_find_trip, container, false);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (!flag) {
            transaction.replace(R.id.find_trip_fragment, findTripWithTag);
        } else {
            transaction.replace(R.id.find_trip_fragment, tripFriendJoined);

        }
        transaction.commit();

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);

        fab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        if (flag) {
                            transaction.replace(R.id.find_trip_fragment, findTripWithTag);
                            flag = !flag;
                        } else {
                            transaction.replace(R.id.find_trip_fragment, tripFriendJoined);
                            flag = !flag;
                        }
                        transaction.commit();
                    }
                }
        );

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();

//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        if (!flag) {
//            transaction.remove(findTripWithTag);
//        } else {
//            transaction.remove(tripFriendJoined);
//        }
//        transaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        if (!flag) {
//            transaction.replace(R.id.find_trip_fragment, findTripWithTag);
//            transaction.remove(findTripWithTag);
//        } else {
//            transaction.replace(R.id.find_trip_fragment, tripFriendJoined);
//            transaction.remove(findTripWithTag);
//        }
//        transaction.commit();
//    }
    }
}