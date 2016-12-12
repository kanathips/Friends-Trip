package com.tinyandfriend.project.friendstrip.fragment;//
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tinyandfriend.project.friendstrip.ConstantValue;
import com.tinyandfriend.project.friendstrip.R;
import com.tinyandfriend.project.friendstrip.activity.ChatActivity;
import com.tinyandfriend.project.friendstrip.adapter.MemberListAdapter;
import com.tinyandfriend.project.friendstrip.info.MemberInfo;
import com.tinyandfriend.project.friendstrip.info.TripInfo;
import com.tinyandfriend.project.friendstrip.view.MapDialog;

import java.util.ArrayList;
import java.util.Map;


public class FragmentTripRoomJoiner extends Fragment {

    private DatabaseReference reference;
    private String tripId;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private Context context;
    private String userUid;

    public static FragmentTripRoomJoiner newInstance(String userUid, String tripId) {
        FragmentTripRoomJoiner fragment = new FragmentTripRoomJoiner();
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_trip_joinner, container, false);


        reference = FirebaseDatabase.getInstance().getReference();

        final ImageView imageView = (ImageView) rootView.findViewById(R.id.image_detail);
        final TextView tripNameTextView = (TextView) rootView.findViewById(R.id.trip_name);
        final TextView tripSpoilTextView = (TextView) rootView.findViewById(R.id.trip_spoil);
        final TextView tripStartTextView = (TextView) rootView.findViewById(R.id.trip_start_date);
        final TextView tripEndTextView = (TextView) rootView.findViewById(R.id.trip_end_date);
        final TextView tripExpenseTextView = (TextView) rootView.findViewById(R.id.trip_expense);
        final TextView tripMaxMember = (TextView) rootView.findViewById(R.id.trip_max_member);

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
                        Glide.with(context)
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
                    MemberListAdapter memberListAdapter = new MemberListAdapter(context, new ArrayList<MemberInfo>(members.values()), reference);
                    RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.member_list);

                    LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(memberListAdapter);

                    final ArrayList<String> file = tripInfo.getFiles();
                    if (file != null) {
                        Button fileButton = (Button) rootView.findViewById(R.id.file_button);
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

                    Button mapButton = (Button) rootView.findViewById(R.id.map_button);
                    mapButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MapDialog mapDialog = new MapDialog(context, reference, tripId);
                            mapDialog.show();
                        }
                    });
                    Button chatButton = (Button) rootView.findViewById(R.id.chat_button);
                    chatButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, ChatActivity.class);
                            startActivity(intent);
                        }
                    });

                    Button leaveButton = (Button) rootView.findViewById(R.id.leave_button);
                    leaveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            reference.child(ConstantValue.USERS_CHILD).child(userUid).child(ConstantValue.TRIP_ID_CHILD).removeValue();
                            reference.child(ConstantValue.TRIP_ROOM_CHILD).child(tripId).child("members").child(userUid).removeValue();
                        }
                    });

                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });
        return rootView;
    }


//        final FloatingActionButton convertMap = (FloatingActionButton) rootView.findViewById(R.id.convert_map);
//
//        convertMap.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (mapFlag)
//                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//                else
//                    googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//                mapFlag = !mapFlag;
//            }
//        });

}
