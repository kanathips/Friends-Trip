package com.tinyandfriend.project.friendstrip.fragment;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.tinyandfriend.project.friendstrip.ConstantValue;
import com.tinyandfriend.project.friendstrip.R;
import com.tinyandfriend.project.friendstrip.activity.ChatActivity;
import com.tinyandfriend.project.friendstrip.view.SingleSheetFAB;

import de.hdodenhof.circleimageview.CircleImageView;


public class FragmentRoomHost extends Fragment implements PlaceSelectionListener, OnMapReadyCallback {

    MapView mapView;
    private DatabaseReference reference;
    private FirebaseUser user;
    private String userUid;
    private String tripID;
    private MaterialSheetFab<SingleSheetFAB> materialSheetFab;

    public FragmentRoomHost() {
        // Required empty public constructor
    }

    public static FragmentRoomHost newInstance(String userUid, String tripID) {
        FragmentRoomHost fragment = new FragmentRoomHost();
        Bundle args = new Bundle();
        args.putString(ConstantValue.USER_UID, userUid);
        args.putString(ConstantValue.TRIP_ID_CHILD, tripID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        reference = FirebaseDatabase.getInstance().getReference();

        if (getArguments() != null) {
            userUid = getArguments().getString(ConstantValue.USER_UID);
            tripID = getArguments().getString(ConstantValue.TRIP_ID_CHILD);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_host_trip, container, false);
        createMap(view, savedInstanceState);

        Button chat_bt = (Button) view.findViewById(R.id.chat_detail);
        Button joiner_list = (Button) view.findViewById(R.id.joiner_list);
        Button detail_trip = (Button) view.findViewById(R.id.room_detail);
        final ImageView imageView = (ImageView) view.findViewById(R.id.image_detail);
        final TextView room_name = (TextView) view.findViewById(R.id.room_name_text);
        final TextView room_spoil = (TextView) view.findViewById(R.id.room_spoil_text);

//        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.config_trip);


        reference = FirebaseDatabase.getInstance().getReference();
        reference.child(ConstantValue.TRIP_ROOM_CHILD).child(tripID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot profilePhotoChild_thumb = dataSnapshot.child(ConstantValue.THUMB_NAIL);
                DataSnapshot profilePhotoChild_text = dataSnapshot.child(ConstantValue.TRIP_NAME);
                DataSnapshot profilePhotoChild_spoil = dataSnapshot.child(ConstantValue.TRIP_SPOIL);
                if (profilePhotoChild_thumb.exists()) {
                    String profilePhotoURL = profilePhotoChild_thumb.getValue(String.class);
                    Glide.with(getActivity().getApplicationContext())
                            .load(profilePhotoURL).centerCrop()
                            .into(imageView);
                }
                if (profilePhotoChild_text.exists()) {
                    String profilePhotoURL_text = profilePhotoChild_text.getValue(String.class);
                    room_name.setText(profilePhotoURL_text);
                }
                if (profilePhotoChild_spoil.exists()) {
                    String profilePhotoURL_spoil = profilePhotoChild_spoil.getValue(String.class);
                    room_spoil.setText(profilePhotoURL_spoil);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


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

                ViewPager viewPager = (ViewPager) dialog.findViewById(R.id.viewpager_joiner);


                dialog.show();
            }
        });


        detail_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.detail_joiner);
                dialog.setCancelable(true);

                final CircleImageView profile_host = (CircleImageView) dialog.findViewById(R.id.content_avatar_image);
                final TextView name_host = (TextView) dialog.findViewById(R.id.content_name_view);
                final TextView content_from_date = (TextView) dialog.findViewById(R.id.content_from_date);
                final TextView content_to_date = (TextView) dialog.findViewById(R.id.content_to_date);
                final TextView text_count = (TextView) dialog.findViewById(R.id.head_text_count);

                reference.child(ConstantValue.TRIP_ROOM_CHILD).child(tripID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        DataSnapshot dataSnapshot1 = dataSnapshot.child(ConstantValue.OWNER_UID);
                        DataSnapshot from_date_snap = dataSnapshot.child(ConstantValue.TRIP_FROM_DATE);
                        DataSnapshot to_date_snap = dataSnapshot.child(ConstantValue.TRIP_TO_DATE);
                        DataSnapshot count_people = dataSnapshot.child(ConstantValue.TRIP_MAX_MEMBERS);
                        if (dataSnapshot1.exists()) {
                            reference.child(ConstantValue.USERS_CHILD).child(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    DataSnapshot profile_avatar = dataSnapshot.child(ConstantValue.PROFILE_PHOTO_CHILD);
                                    DataSnapshot profile_name_host = dataSnapshot.child(ConstantValue.DISPLAY_NAME);
                                    if (profile_avatar.exists() && profile_name_host.exists()) {
                                        String profile_URL = profile_avatar.getValue(String.class);
                                        String profile_URL_displayName = profile_name_host.getValue(String.class);

                                        Glide.with(getActivity().getApplicationContext())
                                                .load(profile_URL).centerCrop()
                                                .into(profile_host);
                                        name_host.setText(profile_URL_displayName);

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                        if(from_date_snap.exists() && to_date_snap.exists() && count_people.exists()){
                            String from_date_string = from_date_snap.getValue(String.class);
                            String to_date_string = to_date_snap.getValue(String.class);
                            int count_people_string = count_people.getValue(Integer.class);

                            content_from_date.setText(from_date_string);
                            content_to_date.setText(to_date_string);
                            text_count.setText(count_people_string+"");

                        }

                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                dialog.show();
            }
        });


//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        return view;
    }

    public void createMap(View view, Bundle bundle) {
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

    @Override
    public void onPlaceSelected(Place place) {

    }

    @Override
    public void onError(Status status) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
