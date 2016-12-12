//package com.tinyandfriend.project.friendstrip.fragment;
//
//
//import android.app.Dialog;
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.v4.app.Fragment;
//import android.support.v7.widget.DefaultItemAnimator;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.google.android.gms.common.api.Status;
//import com.google.android.gms.location.places.Place;
//import com.google.android.gms.location.places.ui.PlaceSelectionListener;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.MapView;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.gordonwong.materialsheetfab.MaterialSheetFab;
//import com.tinyandfriend.project.friendstrip.ConstantValue;
//import com.tinyandfriend.project.friendstrip.MapUtils;
//import com.tinyandfriend.project.friendstrip.R;
//import com.tinyandfriend.project.friendstrip.activity.ChatActivity;
//import com.tinyandfriend.project.friendstrip.adapter.FriendListAdapter;
//import com.tinyandfriend.project.friendstrip.info.FriendInfo;
//import com.tinyandfriend.project.friendstrip.info.PlaceInfo;
//import com.tinyandfriend.project.friendstrip.info.TripInfo;
//import com.tinyandfriend.project.friendstrip.view.SingleSheetFAB;
//
//import java.util.ArrayList;
//
//import de.hdodenhof.circleimageview.CircleImageView;
//
//
//public class FragmentTripRoomHost extends Fragment implements PlaceSelectionListener, OnMapReadyCallback {
//
//    MapView mapView;
//    private DatabaseReference reference;
//    private FirebaseUser user;
//    private String userUid;
//    private String tripId;
//    private MaterialSheetFab<SingleSheetFAB> materialSheetFab;
//    private FriendListAdapter friendListAdapter;
//    private Context context;
//    private GoogleMap googleMap;
//    private boolean mapFlag = true;
//
//    public FragmentTripRoomHost() {
//        // Required empty public constructor
//    }
//
//    public static FragmentTripRoomHost newInstance(String userUid, String tripID) {
//        FragmentTripRoomHost fragment = new FragmentTripRoomHost();
//        Bundle args = new Bundle();
//        args.putString(ConstantValue.USER_UID, userUid);
//        args.putString(ConstantValue.TRIP_ID_CHILD, tripID);
//
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        reference = FirebaseDatabase.getInstance().getReference();
//
//        if (getArguments() != null) {
//            userUid = getArguments().getString(ConstantValue.USER_UID);
//            tripId = getArguments().getString(ConstantValue.TRIP_ID_CHILD);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        this.context = context;
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        final View view = inflater.inflate(R.layout.fragment_host_trip, container, false);
//        createMap(view, savedInstanceState);
//
//        final ImageView imageView = (ImageView) view.findViewById(R.id.image_detail);
//        final TextView room_name = (TextView) view.findViewById(R.id.room_name_text);
//        final TextView room_spoil = (TextView) view.findViewById(R.id.room_spoil_text);
//
//        reference = FirebaseDatabase.getInstance().getReference();
//
//        reference.child(ConstantValue.TRIP_ROOM_CHILD).child(tripId).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                DataSnapshot profilePhotoChild_thumb = dataSnapshot.child(ConstantValue.THUMB_NAIL);
//                DataSnapshot profilePhotoChild_text = dataSnapshot.child(ConstantValue.TRIP_NAME);
//                DataSnapshot profilePhotoChild_spoil = dataSnapshot.child(ConstantValue.TRIP_SPOIL);
//                if (profilePhotoChild_thumb.exists()) {
//                    String profilePhotoURL = profilePhotoChild_thumb.getValue(String.class);
//                    Glide.with(context)
//                            .load(profilePhotoURL).centerCrop()
//                            .into(imageView);
//                }
//                if (profilePhotoChild_text.exists()) {
//                    String profilePhotoURL_text = profilePhotoChild_text.getValue(String.class);
//                    room_name.setText(profilePhotoURL_text);
//                }
//                if (profilePhotoChild_spoil.exists()) {
//                    String profilePhotoURL_spoil = profilePhotoChild_spoil.getValue(String.class);
//                    room_spoil.setText(profilePhotoURL_spoil);
//                }
//            }
//
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//
//        final Button chat_bt = (Button) view.findViewById(R.id.chat_detail);
//        Button joiner_list = (Button) view.findViewById(R.id.joiner_list);
//        Button detail_trip = (Button) view.findViewById(R.id.room_detail);
//        chat_bt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), ChatActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        final FloatingActionButton convertMap = (FloatingActionButton) view.findViewById(R.id.convert_map);
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
//
//
//        joiner_list.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Dialog dialog = new Dialog(getContext());
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setContentView(R.layout.viewpager_dialog_joiner);
//                dialog.setCancelable(true);
//
//                final RecyclerView joinerList = (RecyclerView) dialog.findViewById(R.id.joiner_list);
//                final ArrayList<FriendInfo> members = new ArrayList<>();
//                reference.child(ConstantValue.TRIP_ROOM_CHILD).child(tripId).child("members").addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        if (dataSnapshot.exists()) {
//                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                if (!members.contains(new FriendInfo(dataSnapshot.getKey()))) {
//                                    String photo = snapshot.child("photo").getValue(String.class);
//                                    String name = snapshot.child("name").getValue(String.class);
//                                    String uid = snapshot.child("uid").getValue(String.class);
//                                    FriendInfo friendInfo = new FriendInfo(photo, name, uid);
//                                    members.add(friendInfo);
//                                }
//                            }
//
//                            friendListAdapter = new FriendListAdapter(context, members, reference);
//                            joinerList.setAdapter(friendListAdapter);
//                            joinerList.setLayoutManager(new LinearLayoutManager(context));
//                            joinerList.setItemAnimator(new DefaultItemAnimator());
//                            friendListAdapter.notifyDataSetChanged();
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//
//                dialog.show();
//            }
//        });
//
//
//        detail_trip.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Dialog dialog = new Dialog(getContext());
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setContentView(R.layout.detail_joiner);
//                dialog.setCancelable(true);
//
//                final CircleImageView profile_host = (CircleImageView) dialog.findViewById(R.id.content_avatar_image);
//                final TextView name_host = (TextView) dialog.findViewById(R.id.content_name_view);
//                final TextView content_from_date = (TextView) dialog.findViewById(R.id.content_from_date);
//                final TextView content_to_date = (TextView) dialog.findViewById(R.id.content_to_date);
//                final TextView text_count = (TextView) dialog.findViewById(R.id.head_text_count);
//                final Button fileButton = (Button) dialog.findViewById(R.id.content_deadline_time);
//
//                reference.child(ConstantValue.TRIP_ROOM_CHILD).child(tripId).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        DataSnapshot dataSnapshot1 = dataSnapshot.child(ConstantValue.OWNER_UID);
//                        DataSnapshot from_date_snap = dataSnapshot.child(ConstantValue.TRIP_FROM_DATE);
//                        DataSnapshot to_date_snap = dataSnapshot.child(ConstantValue.TRIP_TO_DATE);
//                        DataSnapshot count_people = dataSnapshot.child(ConstantValue.TRIP_MAX_MEMBERS);
//                        TripInfo tripInfo = dataSnapshot.getValue(TripInfo.class);
//                        final ArrayList<String> file = tripInfo.getFiles();
//                        if (file == null) {
//                            fileButton.setVisibility(View.INVISIBLE);
//                        } else {
//                            fileButton.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent i = new Intent(Intent.ACTION_VIEW);
//                                    i.setData(Uri.parse(file.get(0)));
//                                    startActivity(i);
//                                }
//                            });
//                        }
//                        if (dataSnapshot1.exists()) {
//                            reference.child(ConstantValue.USERS_CHILD).child(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(DataSnapshot dataSnapshot) {
//                                    DataSnapshot profile_avatar = dataSnapshot.child(ConstantValue.PROFILE_PHOTO_CHILD);
//                                    DataSnapshot profile_name_host = dataSnapshot.child(ConstantValue.DISPLAY_NAME);
//                                    if (profile_avatar.exists() && profile_name_host.exists()) {
//                                        String profile_URL = profile_avatar.getValue(String.class);
//                                        String profile_URL_displayName = profile_name_host.getValue(String.class);
//
//                                        Glide.with(getActivity().getApplicationContext())
//                                                .load(profile_URL).centerCrop()
//                                                .into(profile_host);
//                                        name_host.setText(profile_URL_displayName);
//
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelled(DatabaseError databaseError) {
//
//                                }
//                            });
//                        }
//                        if (from_date_snap.exists() && to_date_snap.exists() && count_people.exists()) {
//                            String from_date_string = from_date_snap.getValue(String.class);
//                            String to_date_string = to_date_snap.getValue(String.class);
//                            int count_people_string = count_people.getValue(Integer.class);
//
//                            content_from_date.setText(from_date_string);
//                            content_to_date.setText(to_date_string);
//                            text_count.setText(count_people_string + "");
//
//                        }
//
//                    }
//
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//
//
//                dialog.show();
//            }
//        });
//
//        FloatingActionButton exitFab = (FloatingActionButton) view.findViewById(R.id.out_trip);
//        exitFab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                reference.child(ConstantValue.USERS_CHILD).child(userUid).child(ConstantValue.TRIP_ID_CHILD).removeValue();
//                reference.child(ConstantValue.TRIP_ROOM_CHILD).child(tripId).removeValue();
//            }
//        });
//
//        return view;
//    }
//
//    public void createMap(View view, Bundle bundle) {
//        mapView = (MapView) view.findViewById(R.id.map);
//        mapView.onCreate(bundle);
//        mapView.getMapAsync(this);
//
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        if (mapView != null) {
//            mapView.onResume();
//        }
//    }
//
//    @Override
//    public void onPause() {
//        if (mapView != null) {
//            mapView.onPause();
//        }
//        super.onPause();
//    }
//
//    @Override
//    public void onDestroy() {
//        if (mapView != null) {
//            try {
//                mapView.onDestroy();
//            } catch (NullPointerException e) {
//                Log.e("1", "Error while attempting MapView.onDestroy(), ignoring exception", e);
//            }
//        }
//        super.onDestroy();
//    }
//
//    @Override
//    public void onLowMemory() {
//        super.onLowMemory();
//        if (mapView != null) {
//            mapView.onLowMemory();
//        }
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        if (mapView != null) {
//            mapView.onSaveInstanceState(outState);
//        }
//    }
//
//    @Override
//    public void onPlaceSelected(Place place) {
//
//    }
//
//    @Override
//    public void onError(Status status) {
//
//    }
//
//    @Override
//    public void onMapReady(final GoogleMap googleMap) {
//        this.googleMap = googleMap;
//
//        reference.child(ConstantValue.TRIP_ROOM_CHILD).child(tripId).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    TripInfo tripInfo = dataSnapshot.getValue(TripInfo.class);
//
//                    int pixelWidth = getResources().getDisplayMetrics().widthPixels;
//                    int heightPixels = getResources().getDisplayMetrics().heightPixels;
//                    ArrayList<PlaceInfo> places = tripInfo.getPlaceInfos();
//                    MapUtils mapUtils = new MapUtils(googleMap);
//                    mapUtils.addMarkers(places);
//                    mapUtils.addAppoint(tripInfo.getAppointPlace());
//                    mapUtils.updateCamera(places, pixelWidth, heightPixels);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
//}

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


public class FragmentTripRoomHost extends Fragment {

    private DatabaseReference reference;
    private String tripId;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private Context context;
    private String userUid;
    private TripInfo tripInfo;

    public static FragmentTripRoomHost newInstance(String userUid, String tripId) {
        FragmentTripRoomHost fragment = new FragmentTripRoomHost();
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
                    tripInfo = dataSnapshot.getValue(TripInfo.class);
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
                    leaveButton.setText("ลบทริป");
                    leaveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            reference.child(ConstantValue.USERS_CHILD).child(userUid).child(ConstantValue.TRIP_ID_CHILD).removeValue();
                            reference.child(ConstantValue.TRIP_ROOM_CHILD).child(tripId).removeValue();
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

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putParcelable("tripInfo", tripInfo);
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//    }

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
