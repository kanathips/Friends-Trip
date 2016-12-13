package com.tinyandfriend.project.friendstrip.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.ramotion.foldingcell.FoldingCell;
import com.tinyandfriend.project.friendstrip.ConstantValue;
import com.tinyandfriend.project.friendstrip.MapUtils;
import com.tinyandfriend.project.friendstrip.R;
import com.tinyandfriend.project.friendstrip.info.TripCardViewInfo;
import com.tinyandfriend.project.friendstrip.info.PlaceInfo;
import com.tinyandfriend.project.friendstrip.info.TripInfo;
import com.tinyandfriend.project.friendstrip.info.UserInfo;
import com.tinyandfriend.project.friendstrip.view.ProfileDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class TripCardViewAdapter extends RecyclerView.Adapter<TripCardViewAdapter.TripRoomHolder> {

    private Context mContext;
    private List<TripCardViewInfo> albumList;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private static final String USERS_CHILD = "users";
    private static final String TRIP_CHILD = "tripRoom";
    private static final String OWNER_UID = "ownerUID";
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();
    private String userUid = user.getUid();
    private int widthPixels;
    private int heightPixels;

    class TripRoomHolder extends RecyclerView.ViewHolder {

        private final Button fileButton;
        TextView title, spoil_trip, date_trip, count_people;
        TextView title_content, count_content, name_content, email_content, fromDate, toDate;
        ImageView title_thumbnail, head_image;
        CircleImageView content_avatar;
        FoldingCell foldingCell;
        Button content_location_bt, join_button;
        RelativeLayout profile_dialog;


        TripRoomHolder(View itemView) {
            super(itemView);
            foldingCell = (FoldingCell) itemView.findViewById(R.id.foldingcell_row);

            title = (TextView) itemView.findViewById(R.id.title_from_address);
            spoil_trip = (TextView) itemView.findViewById(R.id.title_to_address);
            date_trip = (TextView) itemView.findViewById(R.id.title_requests_date);
            count_people = (TextView) itemView.findViewById(R.id.count_people);
            title_thumbnail = (ImageView) itemView.findViewById(R.id.title_thumbnail);

            title_content = (TextView) itemView.findViewById(R.id.title_content);
            count_content = (TextView) itemView.findViewById(R.id.head_text_count);
            name_content = (TextView) itemView.findViewById(R.id.content_name_view);
            email_content = (TextView) itemView.findViewById(R.id.content_email_view);
            content_avatar = (CircleImageView) itemView.findViewById(R.id.content_avatar_image);
            head_image = (ImageView) itemView.findViewById(R.id.head_image);
            fromDate = (TextView) itemView.findViewById(R.id.content_from_date);
            toDate = (TextView) itemView.findViewById(R.id.content_to_date);
            join_button = (Button) itemView.findViewById(R.id.content_request_btn);
            fileButton = (Button) itemView.findViewById(R.id.content_file_btn);

            content_location_bt = (Button) itemView.findViewById(R.id.content_location_btn);
            profile_dialog = (RelativeLayout) itemView.findViewById(R.id.profile_dialog);

        }
    }

    public TripCardViewAdapter(Context mContext, List<TripCardViewInfo> albumList, int pixelWidth, int pixelHeight) {
        this.mContext = mContext;
        this.albumList = albumList;
        this.widthPixels = pixelWidth;
        this.heightPixels = pixelHeight;
    }

    @Override
    public TripRoomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trip_room_cell, parent, false);

        return new TripRoomHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TripRoomHolder holder, int position) {

        final TripCardViewInfo tripCardViewInfo = albumList.get(position);

        holder.title.setText(tripCardViewInfo.getName_card());
        holder.spoil_trip.setText(tripCardViewInfo.getTripSpoil());
        holder.date_trip.setText(tripCardViewInfo.getTripStart() + " ถึง \n" + tripCardViewInfo.getTripEnd());
        holder.count_people.setText(tripCardViewInfo.getCount_people() + " คน");

        holder.title_content.setText(tripCardViewInfo.getName_card());
        holder.count_content.setText(tripCardViewInfo.getCount_people() + " คน");
        holder.fromDate.setText(tripCardViewInfo.getTripStart());
        holder.toDate.setText(tripCardViewInfo.getTripEnd());

//        reference.child(TRIP_CHILD).child(USERS_CHILD).child(TRIP_ID).addListenerForSingleValueEvent(new ValueEventListener() {
        reference.child(TRIP_CHILD).child(tripCardViewInfo.getTripId()).child(OWNER_UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String owner_uid = dataSnapshot.getValue(String.class);

                    reference.child(USERS_CHILD).child(owner_uid).addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);

                                holder.name_content.setText(userInfo.getDisplayName());
                                holder.email_content.setText(userInfo.getEmail());

                                if (userInfo.getProfilePhoto() != null && !userInfo.getProfilePhoto().isEmpty()) {
                                    Glide.with(mContext)
                                            .load(userInfo.getProfilePhoto()).centerCrop()
                                            .into(holder.content_avatar);
                                }
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(mContext, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        if (tripCardViewInfo.getThumbnail() != null) {
            Glide.with(mContext)
                    .load(tripCardViewInfo.getThumbnail()).centerCrop()
                    .into(holder.title_thumbnail);
            Glide.with(mContext)
                    .load(tripCardViewInfo.getThumbnail()).centerCrop()
                    .into(holder.head_image);

        }


        holder.foldingCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.foldingCell.toggle(false);
            }
        });

        holder.join_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTripRoom(reference, tripCardViewInfo.getTripId(), userUid, holder);
            }
        });
        holder.content_location_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_map);
                dialog.show();
                final MapUtils[] mapUtils = new MapUtils[1];

                final ArrayList<PlaceInfo> placeInfos = new ArrayList<>();

                final MapView mMapView = (MapView) dialog.findViewById(R.id.mapView);
                MapsInitializer.initialize(mContext);
                mMapView.onCreate(dialog.onSaveInstanceState());
                mMapView.onResume();// needed to get the map to display immediately
                mMapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        googleMap.getUiSettings().setMapToolbarEnabled(false);
                        mapUtils[0] = new MapUtils(googleMap);
                    }
                });

                reference.child(ConstantValue.TRIP_ROOM_CHILD).child(tripCardViewInfo.getTripId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        TripInfo tripInfo = dataSnapshot.getValue(TripInfo.class);

                        ArrayList<PlaceInfo> tempPlaceInfos = tripInfo.getPlaceInfos();
                        if (tempPlaceInfos != null && tempPlaceInfos.size() > 0) {
                            placeInfos.addAll(tripInfo.getPlaceInfos());
                            mapUtils[0].markPlace(placeInfos, widthPixels, heightPixels);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });

            }
        });

        final String file = tripCardViewInfo.getFileUrl();
        if (file != null) {

            holder.fileButton.setVisibility(View.VISIBLE);
            holder.fileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(file));
                    mContext.startActivity(i);
                }
            });
        }

        holder.profile_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reference.child(TRIP_CHILD).child(tripCardViewInfo.getTripId()).child(OWNER_UID).addListenerForSingleValueEvent(
                        new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    String ownerUid = dataSnapshot.getValue(String.class);
                                    ProfileDialog profileDialog = new ProfileDialog(mContext, ownerUid, reference);
                                    profileDialog.show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }
        });
    }


    @Override
    public int getItemCount() {
        return albumList.size();
    }


    private void updateTripRoom(final DatabaseReference reference, final String tripId, final String userUid, final TripRoomHolder holder) {
        final DatabaseReference tripReference = reference.child(ConstantValue.TRIP_ROOM_CHILD).child(tripId);
        tripReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Long maxMember = (Long) dataSnapshot.child("maxMember").getValue();
                    if (dataSnapshot.child("members").getChildrenCount() < maxMember) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("name", user.getDisplayName());
                        map.put("uid", user.getUid());
                        map.put("photo", user.getPhotoUrl().toString());
                        tripReference.child("members").child(userUid).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                updateUserProfile(reference, tripId, userUid, holder);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(mContext, "ไม่สามารถเข้าร่วมได้ กรุณาลองใหม่อีกครั้ง", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(mContext, "ทริปเต็มแล้ว", Toast.LENGTH_SHORT).show();
                        holder.foldingCell.toggle(true);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateUserProfile(DatabaseReference reference, String tripId, String userUid, final TripRoomHolder holder) {
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("tripId", tripId);
        reference.child(ConstantValue.USERS_CHILD).child(userUid).updateChildren(updateMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(mContext, "คุณได้เข้าร่วมทริปแล้ว", Toast.LENGTH_SHORT).show();
                holder.foldingCell.toggle(false);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mContext, "ไม่สามารถเข้าร่วมได้ กรุณาลองใหม่อีกครั้ง", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
