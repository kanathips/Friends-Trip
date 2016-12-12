package com.tinyandfriend.project.friendstrip.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tinyandfriend.project.friendstrip.R;
import com.tinyandfriend.project.friendstrip.activity.CreateTripActivity;
import com.tinyandfriend.project.friendstrip.view.MainViewPager;

import java.io.Serializable;


public class FragmentTripRoomBeforeJoin extends Fragment implements Serializable{


    private ViewPager viewPager;

    public FragmentTripRoomBeforeJoin() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_before_joined, container, false);


        Button joinButton = (Button) view.findViewById(R.id.join_button);
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            viewPager.setCurrentItem(1);
            }
        });

        Button createTripButton = (Button) view.findViewById(R.id.create_trip_button);
        createTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), CreateTripActivity.class));
            }
        });
        return view;
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }
}
