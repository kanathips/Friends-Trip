package com.tinyandfriend.project.friendstrip.activity;


import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tinyandfriend.project.friendstrip.ConstantValue;
import com.tinyandfriend.project.friendstrip.MapUtils;
import com.tinyandfriend.project.friendstrip.R;
import com.tinyandfriend.project.friendstrip.adapter.MemberListAdapter;
import com.tinyandfriend.project.friendstrip.adapter.TripCardViewAdapter;
import com.tinyandfriend.project.friendstrip.info.MemberInfo;
import com.tinyandfriend.project.friendstrip.info.PlaceInfo;
import com.tinyandfriend.project.friendstrip.info.TripInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class TripReviewActivity extends AppCompatActivity {

    private DatabaseReference reference;
    private String tripId;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_review);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        reference = FirebaseDatabase.getInstance().getReference();

        tripId = getIntent().getStringExtra(ConstantValue.TRIP_ID_CHILD);

        final ImageView imageView = (ImageView) findViewById(R.id.image_detail);
        final TextView tripNameTextView = (TextView) findViewById(R.id.trip_name);
        final TextView tripSpoilTextView = (TextView) findViewById(R.id.trip_spoil);
        final TextView tripStartTextView = (TextView) findViewById(R.id.trip_start_date);
        final TextView tripEndTextView = (TextView) findViewById(R.id.trip_end_date);
        final TextView tripExpenseTextView = (TextView) findViewById(R.id.trip_expense);
        final TextView tripMaxMember = (TextView) findViewById(R.id.trip_max_member);

        reference = FirebaseDatabase.getInstance().getReference();

        reference.child(ConstantValue.TRIP_ROOM_CHILD).child(tripId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    final TripInfo tripInfo = dataSnapshot.getValue(TripInfo.class);
                    tripInfo.setId(dataSnapshot.getKey());
                    String profilePhotoURL = tripInfo.getThumbnail();
                    String tripName = tripInfo.getTripName();
                    String tripSpoil = tripInfo.getTripSpoil();
                    String startDate = tripInfo.getStartDate();
                    String endDate = tripInfo.getEndDate();
                    String expense = tripInfo.getExpense();
                    int maxMember = tripInfo.getMaxMember();


                    if (profilePhotoURL != null) {
                        Glide.with(TripReviewActivity.this)
                                .load(profilePhotoURL).centerCrop()
                                .into(imageView);
                    }
                    if (tripName != null) {
                        tripNameTextView.setText(tripName);
                    }

                    if (tripSpoil != null) {
                        tripSpoilTextView.setText(tripSpoil);
                    }

                    if (startDate != null) {
                        tripStartTextView.setText(startDate);
                    }

                    if (endDate != null) {
                        tripEndTextView.setText(endDate);
                    }

                    if (expense != null) {
                        tripExpenseTextView.setText(expense);
                    }

                    tripMaxMember.setText("" + maxMember);

                    Map<String, MemberInfo> members = tripInfo.getMembers();
                    MemberListAdapter memberListAdapter = new MemberListAdapter(TripReviewActivity.this, new ArrayList<MemberInfo>(members.values()), reference);
                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.member_list);

                    LinearLayoutManager layoutManager = new LinearLayoutManager(TripReviewActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(memberListAdapter);

                    final ArrayList<String> file = tripInfo.getFiles();
                    if (file != null) {
                        Button fileButton = (Button) findViewById(R.id.file_button);
                        fileButton.setVisibility(View.VISIBLE);
                        fileButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(file.get(0)));
                                startActivity(i);
                            }
                        });
                    }

                    Button mapButton = (Button) findViewById(R.id.map_button);
                    mapButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Dialog dialog = new Dialog(TripReviewActivity.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.dialog_map);
                            dialog.show();
                            final MapUtils[] mapUtils = new MapUtils[1];

                            final ArrayList<PlaceInfo> placeInfos = new ArrayList<>();

                            final MapView mMapView = (MapView) dialog.findViewById(R.id.mapView);
                            MapsInitializer.initialize(TripReviewActivity.this);
                            mMapView.onCreate(dialog.onSaveInstanceState());
                            mMapView.onResume();// needed to get the map to display immediately
                            mMapView.getMapAsync(new OnMapReadyCallback() {
                                @Override
                                public void onMapReady(GoogleMap googleMap) {
                                    googleMap.getUiSettings().setMapToolbarEnabled(false);
                                    mapUtils[0] = new MapUtils(googleMap);
                                }
                            });

                            final int widthPixels = getResources().getDisplayMetrics().widthPixels;
                            final int heightPixels = getResources().getDisplayMetrics().heightPixels;

                            reference.child(ConstantValue.TRIP_ROOM_CHILD).child(tripInfo.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    TripInfo tripInfo = dataSnapshot.getValue(TripInfo.class);

                                    ArrayList<PlaceInfo> tempPlaceInfos = tripInfo.getPlaceInfos();
                                    if (tempPlaceInfos != null && tempPlaceInfos.size() > 0) {
                                        placeInfos.addAll(tripInfo.getPlaceInfos());
                                        mapUtils[0].markPlace(placeInfos, tripInfo.getAppointPlace(), widthPixels, heightPixels);
                                    }
                                }


                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }

                            });
                        }
                    });
                    Button joinButton = (Button) findViewById(R.id.join_button);
                    joinButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updateTripRoom(reference, tripInfo.getId(), user.getUid());
                        }
                    });
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });
    }

    private void updateTripRoom(final DatabaseReference reference, final String tripId, final String userUid) {
        final DatabaseReference tripReference = reference.child(ConstantValue.TRIP_ROOM_CHILD).child(tripId);
        tripReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Long maxMember = (Long) dataSnapshot.child("maxMember").getValue();
                    if (dataSnapshot.child("members").getChildrenCount() < maxMember) {
                        MemberInfo memberInfo = new MemberInfo(user.getDisplayName(), userUid, user.getPhotoUrl().toString());
                        tripReference.child("members").child(userUid).setValue(memberInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                updateUserProfile(reference, tripId, userUid);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(TripReviewActivity.this, "ไม่สามารถเข้าร่วมได้ กรุณาลองใหม่อีกครั้ง", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(TripReviewActivity.this, "ทริปเต็มแล้ว", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateUserProfile(DatabaseReference reference, String tripId, String userUid) {
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("tripId", tripId);
        reference.child(ConstantValue.USERS_CHILD).child(userUid).updateChildren(updateMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(TripReviewActivity.this, "คุณได้เข้าร่วมทริปแล้ว", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(TripReviewActivity.this, "ไม่สามารถเข้าร่วมได้ กรุณาลองใหม่อีกครั้ง", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
