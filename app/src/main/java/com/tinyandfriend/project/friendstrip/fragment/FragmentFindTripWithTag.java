package com.tinyandfriend.project.friendstrip.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tinyandfriend.project.friendstrip.ConstantValue;
import com.tinyandfriend.project.friendstrip.GridSpacingItemDecoration;
import com.tinyandfriend.project.friendstrip.R;
import com.tinyandfriend.project.friendstrip.adapter.TagListViewAdapter;
import com.tinyandfriend.project.friendstrip.adapter.TripCardViewAdapter;
import com.tinyandfriend.project.friendstrip.info.TripCardViewInfo;
import com.tinyandfriend.project.friendstrip.info.TripInfo;

import java.util.ArrayList;
import java.util.HashSet;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
public class FragmentFindTripWithTag extends Fragment {

    private TagListViewAdapter regionTagAdapter;
    private TagListViewAdapter tripTagAdapter;
    private TagListViewAdapter placeTagAdapter;
    private DatabaseReference reference;
    private HashSet<String> searchedTrips;
    private HashSet<ValueEventListener> listenerList;
    private ArrayList<TripCardViewInfo> tripList;
    private int tagAmount;
    private int tagCheck;
    private ScaleInAnimationAdapter alphaAdapter;
    private static final String TAG = "SearchTripWithTag";
    private View rootView;
    private Context context;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_find_trip_with_tag, container, false);


        reference = FirebaseDatabase.getInstance().getReference();
        listenerList = new HashSet<>();
        searchedTrips = new HashSet<>();

        setUpTagList();
        setupRecycleView();
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void setupRecycleView() {
        tripList = new ArrayList<>();
        int pixelWidth = getResources().getDisplayMetrics().widthPixels;
        int pixelHeight = getResources().getDisplayMetrics().heightPixels;
        TripCardViewAdapter adapter = new TripCardViewAdapter(context, tripList, pixelWidth, pixelHeight);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.searched_trip);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(context, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        Button searchButton = (Button) rootView.findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSearch(v);
            }
        });
        alphaAdapter = new ScaleInAnimationAdapter(adapter);
        alphaAdapter.setFirstOnly(false);
        alphaAdapter.setDuration(250);
        recyclerView.setAdapter(alphaAdapter);
    }

    public void addTrip(final HashSet<String> searchedTrips, DatabaseReference reference) {
        for(final String tripId: searchedTrips) {
            ValueEventListener listener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        TripInfo tripInfo = dataSnapshot.getValue(TripInfo.class);
                        TripCardViewInfo tripCardViewInfo = new TripCardViewInfo(tripId, tripInfo.getTripName(),
                                tripInfo.getStartDate(), tripInfo.getEndDate(), tripInfo.getMaxMember(), tripInfo.getTripSpoil(), tripInfo.getThumbnail());
                        tripList.add(0, tripCardViewInfo);
                        alphaAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, "AddTrip: " + databaseError.getMessage());
                }
            };
            listenerList.add(listener);
            reference.child(ConstantValue.TRIP_ROOM_CHILD).child(tripId).addListenerForSingleValueEvent(listener);
        }
    }

    public void onClickSearch(View view) {
        tripList.clear();
        alphaAdapter.notifyDataSetChanged();
        final ArrayList<String> searchList = new ArrayList<>();
        searchList.addAll(regionTagAdapter.getSelectedTag());
        searchList.addAll(tripTagAdapter.getSelectedTag());
        searchList.addAll(placeTagAdapter.getSelectedTag());
        for (final String tag : searchList) {
            tagAmount = searchList.size();
            tagCheck = 0;
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            if (child.getValue(Boolean.class)) {
                                searchedTrips.add(child.getKey());
                            }
                        }
                        tagCheck++;
                        if(tagCheck == tagAmount){
                            addTrip(searchedTrips, reference);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, "SearchTrip: " + databaseError.getMessage());
                }
            };
            listenerList.add(valueEventListener);
            reference.child(ConstantValue.TAG_INDEX_CHILD).child(tag).addListenerForSingleValueEvent(valueEventListener);
        }

    }

    private void setUpTagList() {
        ListView regionListView = (ListView) rootView.findViewById(R.id.tag_region);
        regionTagAdapter = new TagListViewAdapter(context, R.array.tag_region, R.layout.tag_listview_row);
        regionListView.setAdapter(regionTagAdapter);

        ListView tripListView = (ListView) rootView.findViewById(R.id.tag_trip);
        tripTagAdapter = new TagListViewAdapter(context, R.array.tag_trip, R.layout.tag_listview_row);
        tripListView.setAdapter(tripTagAdapter);

        ListView placeListView = (ListView) rootView.findViewById(R.id.tag_place);
        placeTagAdapter = new TagListViewAdapter(context, R.array.tag_place, R.layout.tag_listview_row);
        placeListView.setAdapter(placeTagAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (ValueEventListener listener : listenerList) {
            reference.removeEventListener(listener);
        }
        listenerList.clear();
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
