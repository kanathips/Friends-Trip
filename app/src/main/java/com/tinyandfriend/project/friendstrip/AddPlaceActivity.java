package com.tinyandfriend.project.friendstrip;

//import android.support.v4.app.FragmentManager;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tinyandfriend.project.friendstrip.info.PlaceInfo;

import java.util.ArrayList;

import static android.R.attr.padding;

public class AddPlaceActivity extends AppCompatActivity implements PlaceSelectionListener, OnMapReadyCallback {

    private GoogleMap googleMap;
    private int pixelWidth;
    private int pixelHeight;
    private boolean mapFlag = true;


    private static final String TAG = "Add_Place_Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);

        pixelWidth = getResources().getDisplayMetrics().widthPixels;
        pixelHeight = getResources().getDisplayMetrics().heightPixels;

//        if (getIntent().hasExtra("placeInfos")) {
//            placeInfos = getIntent().getParcelableArrayListExtra("placeInfos");
//        } else {
//            placeInfos = new ArrayList<>();
//        }

        if(placeInfos.size() > 2){
            createLatLngBounds(placeInfos);
        }else if (placeInfos.size() == 1){
            PlaceInfo info = placeInfos.get(0);
            CameraPosition test = CameraPosition.fromLatLngZoom(info.getLocation(), 17);
            CameraUpdate cu = CameraUpdateFactory.newCameraPosition(test);
            googleMap.animateCamera(cu);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentManager fragmentManager = getFragmentManager();

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                fragmentManager.findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(this);

        MapFragment mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private LatLngBounds createLatLngBounds(ArrayList<PlaceInfo> placeInfos){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (PlaceInfo info: placeInfos){
            builder.include(info.getLocation());
        }
        return builder.build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.commit_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.commit):
                Intent intent = new Intent(this, CreateTripActivity.class);
                intent.putParcelableArrayListExtra("placeInfos", placeInfos);
                setResult(RESULT_OK, intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void onclickAddPlace(View view) {
        if(placeInfo == null)
            return;
        placeInfos.add(placeInfo);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(placeInfo.getName());
        markerOptions.position(placeInfo.getLocation());
        googleMap.addMarker(markerOptions);
    }

    private ArrayList<PlaceInfo> placeInfos;
    private PlaceInfo placeInfo;


    @Override
    public void onPlaceSelected(Place place) {
        placeInfo = new PlaceInfo();
        placeInfo.setLocation(place.getLatLng());
        placeInfo.setName(place.getName().toString());
        placeInfo.setId(place.getId());

        CameraPosition test = CameraPosition.fromLatLngZoom(place.getLatLng(), 17);
        CameraUpdate cu = CameraUpdateFactory.newCameraPosition(test);
        googleMap.animateCamera(cu);
    }

    @Override
    public void onError(Status status) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        for (int i = 0; i < placeInfos.size(); i++) {
            PlaceInfo info = placeInfos.get(i);
            MarkerOptions options = new MarkerOptions();
            options.position(info.getLocation());
            options.title(info.getName());

            googleMap.addMarker(options);
        }

        if(mapFlag)
            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        else
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    public void onClickChangeMapType(View view){
        if(mapFlag)
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        else
            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mapFlag = !mapFlag;
    }
}
