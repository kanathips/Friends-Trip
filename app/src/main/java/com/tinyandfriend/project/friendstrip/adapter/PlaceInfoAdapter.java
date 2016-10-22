package com.tinyandfriend.project.friendstrip.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.tinyandfriend.project.friendstrip.R;

import org.w3c.dom.Text;

/**
 * Created by NewWy on 21/10/2559.
 */

public class PlaceInfoAdapter implements GoogleMap.InfoWindowAdapter {

    private final Context context;

    public PlaceInfoAdapter(Context conext){
        this.context = conext;
    }

    @Override
    public View getInfoWindow(Marker marker) {

        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.place_info, null);

        TextView title = (TextView)view.findViewById(R.id.title);
        title.setText(marker.getTitle());

        TextView snippet = (TextView)view.findViewById(R.id.snippet);
        snippet.setText(marker.getSnippet());

        TextView tripDay = (TextView)view.findViewById(R.id.trip_day);
        tripDay.setText(marker.getTag().toString());

        return view;
    }
}
