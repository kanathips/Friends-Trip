package com.tinyandfriend.project.friendstrip.view;//package com.tinyandfriend.project.friendstrip.view;
//
//import android.app.FragmentManager;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.app.DialogFragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.common.api.Status;
//import com.google.android.gms.location.places.Place;
//import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
//import com.google.android.gms.location.places.ui.PlaceSelectionListener;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.MapFragment;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.tinyandfriend.project.friendstrip.R;
//import com.tinyandfriend.project.friendstrip.info.PlaceInfo;
//
//import java.util.ArrayList;
//
///**
// * Created by NewWy on 10/10/2559.
// */
//
//public class MapDialog extends DialogFragment implements PlaceSelectionListener, OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {
//
//    private ArrayList<PlaceInfo> placeInfos;
//    private PlaceInfo placeInfo;
//
//    @NonNull
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.trip_day_dialog, container, false);
//
//        FragmentManager fragmentManager = getActivity().getFragmentManager();
//
//        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
//                fragmentManager.findFragmentById(R.id.place_autocomplete_fragment);
//        autocompleteFragment.setOnPlaceSelectedListener(this);
//
//        MapFragment mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//
////        Button button = (Button)view.findViewById(R.id.button);
////        button.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                if(placeInfo != null){
////                    placeInfos.add(placeInfo);
////                }
////            }
////        });
//                return super.onCreateView(inflater, container, savedInstanceState);
////        return view;
//    }
//
//    @Override
//    public void onPlaceSelected(Place place) {
//        placeInfo = new PlaceInfo();
//        placeInfo.setLocation(place.getLatLng());
//        placeInfo.setName(place.getName().toString());
//        placeInfo.setId(place.getId());
//    }
//
//    @Override
//    public void onError(Status status) {
//
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        googleMap.getUiSettings().setMapToolbarEnabled(false);
//        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }
//}

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.tinyandfriend.project.friendstrip.R;

public class MapDialog extends Dialog implements
        OnMapReadyCallback, PlaceSelectionListener {

    private Context context;

    public MapDialog(Context context) {
        super(context);
        this.context = context;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_day_dialog);

//        FragmentManager fragmentManager = activity.getFragmentManager();
//
//        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
//                fragmentManager.findFragmentById(R.id.place_autocomplete_fragment);
//        autocompleteFragment.setOnPlaceSelectedListener(this);
//
//        MapFragment mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

    }

    @Nullable

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    @Override
    public void onPlaceSelected(Place place) {

    }

    @Override
    public void onError(Status status) {

    }



    @Override
    public void dismiss() {
        super.dismiss();

        FragmentManager fragmentManager = ((Activity)context).getFragmentManager();
        FragmentTransaction ft2 = fragmentManager
                .beginTransaction();

        ft2.remove(fragmentManager
                .findFragmentByTag("map"));
        ft2.remove(fragmentManager.findFragmentByTag("place_autocomplete_fragment"));
        ft2.commit();
    }

}

