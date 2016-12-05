package com.tinyandfriend.project.friendstrip;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
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
import com.tinyandfriend.project.friendstrip.info.PlaceInfo;
import com.tinyandfriend.project.friendstrip.info.TripInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JoinDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private TextView date_text;
    private TextView des;
    private TextView count_people;
    private TextView expense;
    private int pixelWidth;
    private int pixelHeight;
    private MapUtils mapUtils;
    private static final int ADD_PLACE_REQUEST = 0;

    private ArrayList<PlaceInfo> placeInfos;
    private String tripId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_detail);

        final Intent intent = getIntent();

        final ImageView imageView = (ImageView) findViewById(R.id.image_thumbnail);
        setDate_text((TextView) findViewById(R.id.date_text));
        setDes((TextView) findViewById(R.id.message_host));
        setCount_people((TextView) findViewById(R.id.count_people) );
        setExpense((TextView) findViewById(R.id.expense_trip) );

        placeInfos = new ArrayList<>();
        pixelWidth = getResources().getDisplayMetrics().widthPixels;
        pixelHeight = getResources().getDisplayMetrics().heightPixels;


        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        if(intent.hasExtra("key_room") ){
            tripId = intent.getStringExtra("key_room");
            reference.child(ConstantValue.TRIP_ROOM_CHILD).child(tripId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    TripInfo tripInfo = dataSnapshot.getValue(TripInfo.class);

                    Glide.with(JoinDetailActivity.this)
                            .load(tripInfo.getThumbnail()).centerCrop()
                            .into(imageView);

                    collapsingToolbar.setTitle(tripInfo.getTripName());

                    date_text.setText(tripInfo.getStartDate()+" ถึง "+tripInfo.getEndDate());

                    expense.setText(tripInfo.getExpense()+" บาท");
                    count_people.setText(tripInfo.getMaxMember()+" คน");

                    des.setText("สปอยทริป : "+tripInfo.getTripSpoil());
                    ArrayList<PlaceInfo> tempPlaceInfos = tripInfo.getPlaceInfos();
                    if(tempPlaceInfos != null && tempPlaceInfos.size() > 0) {
                        placeInfos.addAll(tripInfo.getPlaceInfos());
                        mapUtils.markPlace(placeInfos, pixelWidth, pixelHeight);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    public void onClickJoinTrip(View view){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userUid = user.getUid();
        updateTripRoom(reference, tripId, userUid);
    }


    private void updateTripRoom(final DatabaseReference reference, final String tripId, final String userUid){
        final DatabaseReference tripReference = reference.child("tripRoom").child(tripId);
        tripReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Long maxMember = (Long) dataSnapshot.child("maxMember").getValue();
                    if(dataSnapshot.child("members").getChildrenCount() < maxMember){
                        Map<String, Object> map = new HashMap<>();
                        map.put(userUid, true);
                        tripReference.child("members").updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                updateUserProfile(reference, tripId, userUid);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(JoinDetailActivity.this, "ไม่สามารถเข้าร่วมได้ กรุณาลองใหม่อีกครั้ง (1)", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        Toast.makeText(JoinDetailActivity.this, "ทริปเต็มแล้ว", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateUserProfile(DatabaseReference reference, String tripId, String userUid){
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("tripId", tripId);
        reference.child("users").child(userUid).updateChildren(updateMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(JoinDetailActivity.this, "Join OK", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(JoinDetailActivity.this, "ไม่สามารถเข้าร่วมได้ กรุณาลองใหม่อีกครั้ง (2)", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        mapUtils = new MapUtils(googleMap);
    }




    public TextView getDate_text() {
        return date_text;
    }

    public void setDate_text(TextView date_text) {
        this.date_text = date_text;
    }

    public TextView getDes() {
        return des;
    }

    public void setDes(TextView des) {
        this.des = des;
    }

    public TextView getCount_people() {
        return count_people;
    }

    public void setCount_people(TextView count_people) {
        this.count_people = count_people;
    }

    public TextView getExpense() {
        return expense;
    }

    public void setExpense(TextView expense) {
        this.expense = expense;
    }


}