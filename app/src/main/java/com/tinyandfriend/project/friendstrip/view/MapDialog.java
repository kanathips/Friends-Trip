package com.tinyandfriend.project.friendstrip.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.Window;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.tinyandfriend.project.friendstrip.ConstantValue;
import com.tinyandfriend.project.friendstrip.MapUtils;
import com.tinyandfriend.project.friendstrip.R;
import com.tinyandfriend.project.friendstrip.info.PlaceInfo;
import com.tinyandfriend.project.friendstrip.info.TripInfo;

import java.util.ArrayList;

/**
 * Created by NewWy on 12/12/2559.
 */

public class MapDialog extends Dialog {

    private final DatabaseReference reference;
    private final Context context;
    private final String tripId;
    private boolean mapFlag = false;
    public GoogleMap googleMap;

    public MapDialog(Context context, DatabaseReference reference, String tripId) {
        super(context);
        this.reference = reference;
        this.context = context;
        this.tripId = tripId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_map);


        final MapUtils[] mapUtils = new MapUtils[1];

        final ArrayList<PlaceInfo> placeInfos = new ArrayList<>();

        final MapView mMapView = (MapView) findViewById(R.id.mapView);
        MapsInitializer.initialize(context);
        mMapView.onCreate(onSaveInstanceState());
        mMapView.onResume();// needed to get the map to display immediately
        mMapView.getMapAsync(new OnMapReadyCallback() {


            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.getUiSettings().setMapToolbarEnabled(false);
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                MapDialog.this.googleMap = googleMap;
                mapUtils[0] = new MapUtils(googleMap);
            }
        });

        final int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        final int heightPixels = context.getResources().getDisplayMetrics().heightPixels;

        reference.child(ConstantValue.TRIP_ROOM_CHILD).child(tripId).addListenerForSingleValueEvent(new ValueEventListener() {
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


        final FloatingActionButton convertMap = (FloatingActionButton) findViewById(R.id.convert_map);

        convertMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mapFlag)
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                else
                    googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                mapFlag = !mapFlag;
            }
        });
    }
}
