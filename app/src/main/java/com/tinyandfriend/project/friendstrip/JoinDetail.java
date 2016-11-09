package com.tinyandfriend.project.friendstrip;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tinyandfriend.project.friendstrip.info.PlaceInfo;
import com.tinyandfriend.project.friendstrip.info.TripInfo;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class JoinDetail extends AppCompatActivity implements OnMapReadyCallback {

    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private TextView date_text;
    private TextView des;
    private TextView count_people;
    private TextView expense;
    private GoogleMap googleMap;
    private int pixelWidth;
    private int pixelHeight;
    private static final int ADD_PLACE_REQUEST = 0;
    private ArrayList<PlaceInfo> placeInfos;


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
            reference.child("tripRoom").child(intent.getStringExtra("key_room")).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    TripInfo tripInfo = dataSnapshot.getValue(TripInfo.class);

                    Glide.with(JoinDetail.this)
                            .load(tripInfo.getThumbnail()).centerCrop()
                            .into(imageView);

                    collapsingToolbar.setTitle(tripInfo.getTripName());

                    date_text.setText(tripInfo.getStartDate()+" ถึง "+tripInfo.getEndDate());

                    expense.setText(tripInfo.getExpense()+" บาท");
                    count_people.setText(tripInfo.getMaxMember()+" คน");

                    des.setText("สปอยทริป : "+tripInfo.getTripSpoil());

                    placeInfos = tripInfo.getPlaceInfos();



                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
//
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        this.googleMap = googleMap;

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        googleMap.clear();
        for (int i = 0; i < placeInfos.size(); i++) {
            PlaceInfo info = placeInfos.get(i);
            MarkerOptions options = new MarkerOptions();
            options.position(info.getLocation().toGmsLatLng());
            options.title(info.getName());
            builder.include(info.getLocation().toGmsLatLng());
            googleMap.addMarker(options);
        }

        if (placeInfos.size() > 1) {
            LatLngBounds latLngBounds = builder.build();
            int padding = (int) (pixelWidth * 0.15);
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(latLngBounds, pixelWidth, pixelHeight, padding);
            googleMap.animateCamera(cu);
        } else if (placeInfos.size() == 1) {
            PlaceInfo info = placeInfos.get(0);
            CameraPosition test = CameraPosition.fromLatLngZoom(info.getLocation().toGmsLatLng(), 17);
            CameraUpdate cu = CameraUpdateFactory.newCameraPosition(test);
            googleMap.animateCamera(cu);
        }
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
