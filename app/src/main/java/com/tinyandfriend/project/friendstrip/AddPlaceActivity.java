package com.tinyandfriend.project.friendstrip;

//import android.support.v4.app.FragmentManager;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.tinyandfriend.project.friendstrip.adapter.PlaceInfoAdapter;
import com.tinyandfriend.project.friendstrip.info.PlaceInfo;
import com.tinyandfriend.project.friendstrip.view.SingleSheetFAB;

import java.util.ArrayList;

public class AddPlaceActivity extends AppCompatActivity implements PlaceSelectionListener, OnMapReadyCallback {

    private GoogleMap googleMap;
    private int pixelWidth;
    private int pixelHeight;
    private boolean mapFlag = true;


    private static final String TAG = "Add_Place_Activity";
    private MaterialSheetFab<SingleSheetFAB> materialSheetFab;
    private int tripDuration;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);
        placeInfos = new ArrayList<>();
        pixelWidth = getResources().getDisplayMetrics().widthPixels;
        pixelHeight = getResources().getDisplayMetrics().heightPixels;

        if (getIntent().hasExtra("placeInfos")) {
            originalPlaceInfos = getIntent().getParcelableArrayListExtra("placeInfos");
        } else {
            originalPlaceInfos = new ArrayList<>();
        }

        tripDuration = (int) getIntent().getLongExtra("tripDuration", 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentManager fragmentManager = getFragmentManager();

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                fragmentManager.findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(this);

        MapFragment mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setupAddPlaceFab(tripDuration);
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
                originalPlaceInfos.addAll(placeInfos);
                Intent intent = new Intent(this, CreateTripActivity.class);
                intent.putParcelableArrayListExtra("placeInfos", originalPlaceInfos);
                setResult(RESULT_OK, intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupAddPlaceFab(int tripDuration) {
        if (tripDuration == 0 || tripDuration == 1) {
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_place_button);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addPlace(placeInfos, placeInfo, googleMap);
                }
            });
        } else {

            ArrayList<String> fabSheetTexts = new ArrayList<>();

            for (int i = 1; i <= tripDuration && i < 6; i++) {
                String text = "วันที่ " + i;
                fabSheetTexts.add(text);
            }

            if (tripDuration > 5) {
                fabSheetTexts.add("เพิ่มเติม");
            }


            SingleSheetFAB fab = (SingleSheetFAB) findViewById(R.id.add_place_button);
            ListView sheetView = (ListView) findViewById(R.id.fab_sheet);
            View overlay = findViewById(R.id.overlay);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, fabSheetTexts);
            int sheetColor = getResources().getColor(R.color.fabBackGround);
            int fabColor = getResources().getColor(R.color.colorPrimary);

            sheetView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (placeInfo == null)
                        return;
                    if (originalPlaceInfos.contains(placeInfo) || placeInfos.contains(placeInfo)) {
                        Toast.makeText(AddPlaceActivity.this, "คุณเลือกสถานที่นี้ไปแล้ว", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (position < 5) {
                        placeInfo.setDay(position + 1);
                        addPlace(placeInfos, placeInfo, googleMap);
                    } else {
                        setupDialog(placeInfos, placeInfo, googleMap);
                    }
                    materialSheetFab.hideSheet();
                }
            });

            sheetView.setAdapter(adapter);
            materialSheetFab = new MaterialSheetFab<>(fab, sheetView, overlay,
                    sheetColor, fabColor);
        }
    }

    @Override
    public void onBackPressed() {
        if (materialSheetFab == null) {
            super.onBackPressed();
        }else if(materialSheetFab.isSheetVisible()){
            materialSheetFab.hideSheet();
        }
        else {
            super.onBackPressed();
        }
    }

    private void addPlace(ArrayList<PlaceInfo> placeInfos, PlaceInfo placeInfo, GoogleMap googleMap) {
        if (placeInfo == null)
            return;
        placeInfos.add(placeInfo);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(placeInfo.getName());
        markerOptions.position(placeInfo.getLocation().toGmsLatLng());
        markerOptions.snippet(placeInfo.getAddress());
        googleMap.addMarker(markerOptions).setTag(placeInfo.getDay());
    }

    private ArrayList<PlaceInfo> originalPlaceInfos;
    private PlaceInfo placeInfo;
    private ArrayList<PlaceInfo> placeInfos;


    @Override
    public void onPlaceSelected(Place place) {
        placeInfo = new PlaceInfo();
        placeInfo.setLocationFromGms(place.getLatLng());
        placeInfo.setName(place.getName().toString());
        placeInfo.setId(place.getId());
        placeInfo.setAddress(place.getAddress().toString());

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

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        googleMap.getUiSettings().setMapToolbarEnabled(false);
        for (int i = 0; i < originalPlaceInfos.size(); i++) {
            PlaceInfo info = originalPlaceInfos.get(i);
            MarkerOptions options = new MarkerOptions();
            options.position(info.getLocation().toGmsLatLng());
            options.title(info.getName());
            builder.include(info.getLocation().toGmsLatLng());
            googleMap.addMarker(options);
        }

        if (originalPlaceInfos.size() > 1) {
            LatLngBounds latLngBounds = builder.build();
            int padding = (int) (pixelWidth * 0.15);
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(latLngBounds, pixelWidth, pixelHeight, padding);
            googleMap.animateCamera(cu);
        } else if (originalPlaceInfos.size() == 1) {
            PlaceInfo info = originalPlaceInfos.get(0);
            CameraPosition test = CameraPosition.fromLatLngZoom(info.getLocation().toGmsLatLng(), 17);
            CameraUpdate cu = CameraUpdateFactory.newCameraPosition(test);
            googleMap.animateCamera(cu);
        }

        if (mapFlag)
            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        else
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        googleMap.setInfoWindowAdapter(new PlaceInfoAdapter(this));
    }

    public void onClickChangeMapType(View view) {
        if (mapFlag)
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        else
            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mapFlag = !mapFlag;
    }

    private void setupDialog(final ArrayList<PlaceInfo> placeInfos, final PlaceInfo placeInfo, final GoogleMap googleMap) {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.trip_day_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.dialog_editText);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                int tripDay = Integer.parseInt(userInput.getText().toString());

                                if (tripDay > tripDuration || tripDay == 0) {
                                    Toast.makeText(AddPlaceActivity.this, "วันที่ไม่ถูกต้อง (เกินวันที่กำหนดไว้)", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                placeInfo.setDay(tripDay);
                                addPlace(placeInfos, placeInfo, googleMap);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
}
