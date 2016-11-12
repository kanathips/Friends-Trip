package com.tinyandfriend.project.friendstrip.adapter;

import android.app.Dialog;
import android.content.Context;
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
import com.tinyandfriend.project.friendstrip.info.CardViewInfo;
import com.tinyandfriend.project.friendstrip.info.PlaceInfo;
import com.tinyandfriend.project.friendstrip.info.TripInfo;
import com.tinyandfriend.project.friendstrip.info.UserInfo;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TripRoomCardViewAdapter extends RecyclerView.Adapter<TripRoomCardViewAdapter.TripRoomHolder> {

    private Context mContext;
    private List<CardViewInfo> albumList;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private static final String USERS_CHILD = "users";
    private static final String TRIP_CHILD = "tripRoom";
    private static final String TRIP_ID = "tripId";
    private static final String OWNER_UID = "ownerUID";
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();
    String userUid = user.getUid();
    private int widthPixels;
    private int heightPixels;

    class TripRoomHolder extends RecyclerView.ViewHolder {
        //        TextView titles, tripStart,count;
//        ImageView thumbnail;
//        TextView tripEnd;
//        CardView cardView;
        TextView title, spoil_trip, date_trip, count_people;
        TextView title_content, count_content, name_content, email_content, fromDate, toDate;
        ImageView title_thumbnail, head_image;
        CircleImageView content_avatar;
        FoldingCell foldingCell;
        Button content_location_bt;
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

            content_location_bt = (Button) itemView.findViewById(R.id.content_location_btn);
            profile_dialog = (RelativeLayout) itemView.findViewById(R.id.profile_dialog);


//            titles = (TextView) itemView.findViewById(R.id.name_card);
//            tripStart = (TextView) itemView.findViewById(R.id.trip_start);
//            tripEnd = (TextView) itemView.findViewById(R.id.trip_end);
//            count = (TextView) itemView.findViewById(R.id.count_people);
//            thumbnail = (ImageView) itemView.findViewById(R.id.image_card);
//            cardView = (CardView) itemView.findViewById(R.id.card_view);
        }
    }

    public TripRoomCardViewAdapter(Context mContext, List<CardViewInfo> albumList, int pixelWidth, int pixelHeight) {
        this.mContext = mContext;
        this.albumList = albumList;
        this.widthPixels = pixelWidth;
        this.heightPixels = pixelHeight;
    }

    @Override
    public TripRoomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell, parent, false);

        return new TripRoomHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TripRoomHolder holder, int position) {

        final CardViewInfo album = albumList.get(position);

        holder.title.setText(album.getName_card());
        holder.spoil_trip.setText(album.getTripSpoil());
        holder.date_trip.setText(album.getTripStart() + " ถึง \n" + album.getTripEnd());
        holder.count_people.setText(album.getCount_people() + " คน");

        holder.title_content.setText(album.getName_card());
        holder.count_content.setText(album.getCount_people() + " คน");
        holder.fromDate.setText(album.getTripStart());
        holder.toDate.setText(album.getTripEnd());

//        reference.child(TRIP_CHILD).child(USERS_CHILD).child(TRIP_ID).addListenerForSingleValueEvent(new ValueEventListener() {
        reference.child(TRIP_CHILD).child(album.getTripId()).child(OWNER_UID).addListenerForSingleValueEvent(new ValueEventListener() {
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


        if (album.getThumbnail() != null) {
            Glide.with(mContext)
                    .load(album.getThumbnail()).centerCrop()
                    .into(holder.title_thumbnail);
            Glide.with(mContext)
                    .load(album.getThumbnail()).centerCrop()
                    .into(holder.head_image);

        }


        holder.foldingCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.foldingCell.toggle(false);
            }
        });


        holder.content_location_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_map);
                dialog.show();
                final GoogleMap googleMap;
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

                reference.child(ConstantValue.TRIPROOM_CHILD).child(album.getTripId()).addListenerForSingleValueEvent(new ValueEventListener() {
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




        holder.profile_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.profile_dialog);
                dialog.show();

                final CircleImageView circleImageView = (CircleImageView) dialog.findViewById(R.id.user_profile_photo);
                final TextView userName_dialog = (TextView) dialog.findViewById(R.id.user_profile_name_dialog);
                final TextView userEmail_dialog = (TextView)dialog.findViewById(R.id.user_profile_email_dialog);


                reference.child(TRIP_CHILD).child(album.getTripId()).child(OWNER_UID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String owner_uid = dataSnapshot.getValue(String.class);

                            if(owner_uid.equals(userUid)){
                                ImageView imageView = (ImageView) dialog.findViewById(R.id.add_friend);
                                imageView.setVisibility(View.INVISIBLE);
                            }

                            reference.child(USERS_CHILD).child(owner_uid).addListenerForSingleValueEvent(new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);

                                        userName_dialog.setText(userInfo.getDisplayName());
                                        userEmail_dialog.setText(userInfo.getEmail());

                                        if (userInfo.getProfilePhoto() != null && !userInfo.getProfilePhoto().isEmpty()) {
                                            Glide.with(mContext)
                                                    .load(userInfo.getProfilePhoto()).centerCrop()
                                                    .into(circleImageView);
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




            }
        });


//        holder.titles.setText(album.getName_card());
//        holder.tripStart.setText(album.getTripStart());
//        holder.tripEnd.setText(album.getTripEnd());
//        holder.count.setText(Integer.toString(album.getCount_people()));
//
//        if (album.getThumbnail() != null) {
//            Glide.with(mContext)
//                    .load(album.getThumbnail()).centerCrop()
//                    .into(holder.thumbnail);
//
//
//            holder.cardView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(mContext, JoinDetailActivity.class);
//                    intent.putExtra("key_room",album.getTripId());
//                    intent.putExtra("start_date",holder.tripStart.getText().toString());
//                    intent.putExtra("end_date",holder.tripEnd.getText().toString());
//                    intent.putExtra("count_people",holder.count.getText().toString());
//                    intent.putExtra("name_trip",holder.titles.getText().toString());
//                    intent.putExtra("pic_thumbnail",album.getThumbnail().toString());
//
//                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext, holder.thumbnail, "profile");
//
//                    mContext.startActivity(intent, options.toBundle());
//                }
//            });
//        }
    }


    @Override
    public int getItemCount() {
        return albumList.size();
    }


}
