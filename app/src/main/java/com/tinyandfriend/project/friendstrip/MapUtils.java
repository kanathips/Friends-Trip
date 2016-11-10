package com.tinyandfriend.project.friendstrip;

import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLngBounds;
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
            googleMap.addMarker(options);
        }
    }

    public void markPlace(ArrayList<PlaceInfo> placeInfos, int pixelWidth, int pixelHeight) {
        addMarkers(placeInfos);
        updateCamera(placeInfos, pixelWidth, pixelHeight);
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
