package com.tinyandfriend.project.friendstrip.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.MapView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tinyandfriend.project.friendstrip.ConstantValue;
import com.tinyandfriend.project.friendstrip.R;
import com.tinyandfriend.project.friendstrip.activity.ChatActivity;


public class FragmentRoomJoiner extends Fragment {

    MapView mapView;
    private DatabaseReference reference;
    private FirebaseUser user;
    private String userUid;
    private int type;

    public FragmentRoomJoiner() {
        // Required empty public constructor
    }

    public static FragmentRoomJoiner newInstance(String userUid, int type) {
        FragmentRoomJoiner fragment = new FragmentRoomJoiner();
        Bundle args = new Bundle();
        args.putString(ConstantValue.USER_UID, userUid);
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        reference = FirebaseDatabase.getInstance().getReference();

        if (getArguments() != null) {
            userUid = getArguments().getString(ConstantValue.USER_UID);
            type = getArguments().getInt("type");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_room, container, false);
        createMap(view,savedInstanceState);

        Button chat_bt = (Button) view.findViewById(R.id.chat_detail);
        chat_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                startActivity(intent);
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
