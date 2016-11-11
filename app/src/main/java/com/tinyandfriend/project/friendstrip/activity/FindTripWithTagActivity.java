package com.tinyandfriend.project.friendstrip.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.MapView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tinyandfriend.project.friendstrip.ConstantValue;
import com.tinyandfriend.project.friendstrip.R;
import com.tinyandfriend.project.friendstrip.adapter.TagListViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FindTripWithTagActivity extends AppCompatActivity {

    private TagListViewAdapter regionTagAdapter;
    private TagListViewAdapter tripTagAdapter;
    private TagListViewAdapter placeTagAdapter;
    private DatabaseReference reference;
    private HashSet<String> searchedTrips;
    private HashMap<String, ChildEventListener> listenerMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_trip_with_tag);
        reference = FirebaseDatabase.getInstance().getReference();
        setUpTagList();

        searchedTrips = new HashSet<>();
    }

    public void onClickSearch(View view) {
        final ArrayList<String> searchList = new ArrayList<>();
        searchList.addAll(regionTagAdapter.getSelectedTag());
        searchList.addAll(tripTagAdapter.getSelectedTag());
        searchList.addAll(placeTagAdapter.getSelectedTag());
        for (final String tag : searchList) {
            reference.child(ConstantValue.TAGINDEX_CHILD).child(tag).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for(DataSnapshot child:dataSnapshot.getChildren()) {
                            if(child.getValue(Boolean.class)) {
                                searchedTrips.add(child.getKey());
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    private void setUpTagList() {
        ListView regionListView = (ListView) findViewById(R.id.tag_region);
        regionTagAdapter = new TagListViewAdapter(this, R.array.tag_region, R.layout.tag_listview_row);
        regionListView.setAdapter(regionTagAdapter);

        ListView tripListView = (ListView) findViewById(R.id.tag_trip);
        tripTagAdapter = new TagListViewAdapter(this, R.array.tag_trip, R.layout.tag_listview_row);
        tripListView.setAdapter(tripTagAdapter);

        ListView placeListView = (ListView) findViewById(R.id.tag_place);
        placeTagAdapter = new TagListViewAdapter(this, R.array.tag_place, R.layout.tag_listview_row);
        placeListView.setAdapter(placeTagAdapter);
    }
}
