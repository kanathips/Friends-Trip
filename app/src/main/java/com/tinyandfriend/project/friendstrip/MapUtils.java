package com.tinyandfriend.project.friendstrip;

import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tinyandfriend.project.friendstrip.info.PlaceInfo;

import java.util.ArrayList;

/**
 * Created by NewWy on 10/11/2559.
 */

public class MapUtils {

    private final GoogleMap googleMap;

    public MapUtils(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    public void addMarkers(ArrayList<PlaceInfo> placeInfos) {
        googleMap.clear();
        for (int i = 0; i < placeInfos.size(); i++) {
            PlaceInfo info = placeInfos.get(i);
            MarkerOptions options = new MarkerOptions();
            options.position(info.getLocation().toGmsLatLng());
            options.title(info.getName());
            googleMap.addMarker(options).setTag(info.getDay());
        }
    }

    public void markPlace(ArrayList<PlaceInfo> placeInfos, int pixelWidth, int pixelHeight) {
        addMarkers(placeInfos);
        updateCamera(placeInfos, pixelWidth, pixelHeight);
    }

    public void markPlace(ArrayList<PlaceInfo> placeInfos, PlaceInfo appointPlace, int pixelWidth, int pixelHeight) {
        ArrayList<PlaceInfo> newPlaces = new ArrayList<>(placeInfos);
        newPlaces.add(appointPlace);
        addMarkers(newPlaces, appointPlace);
        updateCamera(newPlaces, pixelWidth, pixelHeight);
    }

    private void addMarkers(ArrayList<PlaceInfo> placeInfos, PlaceInfo appointPlace) {
        addMarkers(placeInfos);
        addAppoint(appointPlace);
    }

    public void addAppoint(PlaceInfo appointPlace){

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(appointPlace.getName());
        markerOptions.position(appointPlace.getLocation().toGmsLatLng());
        markerOptions.snippet(appointPlace.getAddress());
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        googleMap.addMarker(markerOptions).setTag("สถานที่นัดพบ");
    }


    public void updateCamera(ArrayList<PlaceInfo> placeInfos, int pixelWidth, int pixelHeight) {

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (int i = 0; i < placeInfos.size(); i++) {
            PlaceInfo info = placeInfos.get(i);
            builder.include(info.getLocation().toGmsLatLng());
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
}
