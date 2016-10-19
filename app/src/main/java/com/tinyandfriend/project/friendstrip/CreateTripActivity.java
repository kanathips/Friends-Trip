package com.tinyandfriend.project.friendstrip;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tinyandfriend.project.friendstrip.adapter.TagListViewAdapter;
import com.tinyandfriend.project.friendstrip.info.PlaceInfo;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by NewWy on 9/10/2559.
 */

public class CreateTripActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ArrayList<PlaceInfo> placeInfos;
    private static final int ADD_PLACE_REQUEST = 1;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.action_next):
                if(validateFrom()) {
                    Toast.makeText(this, "Commit", Toast.LENGTH_SHORT).show();
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean validateFrom(){
        boolean valid = true;



        return valid;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.next_page_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);

        placeInfos = new ArrayList<>();

        ListView regionListView = (ListView)findViewById(R.id.tag_region);
        TagListViewAdapter regionTagAdapter = new TagListViewAdapter(this, R.array.tag_region, R.layout.tag_listview_row);
        regionListView.setAdapter(regionTagAdapter);

        ListView tripListView = (ListView)findViewById(R.id.tag_trip);
        TagListViewAdapter tripTagAdapter = new TagListViewAdapter(this, R.array.tag_trip, R.layout.tag_listview_row);
        tripListView.setAdapter(tripTagAdapter);

        ListView placeListView = (ListView)findViewById(R.id.tag_place);
        TagListViewAdapter placeTagAdapter = new TagListViewAdapter(this, R.array.tag_place, R.layout.tag_listview_row);
        placeListView.setAdapter(placeTagAdapter);

        //Add Toolbar

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Add datepicker to edittext

        final EditText startTripEditText = (EditText) findViewById(R.id.trip_start);
        startTripEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePicker = new DatePickerDialog(CreateTripActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String date = String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear)
                                + "-" + String.valueOf(year);
                        startTripEditText.setText(date);
                    }
                }, yy, mm, dd);
                datePicker.show();
            }

        });

        //Add datepicker to edittext

        final EditText endTripEditText = (EditText) findViewById(R.id.trip_end);

        endTripEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePicker = new DatePickerDialog(CreateTripActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String date = String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear)
                                + "-" + String.valueOf(year);
                        endTripEditText.setText(date);
                    }
                }, yy, mm, dd);
                datePicker.show();
            }

        });

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button editMapButton = (Button)findViewById(R.id.editMap);
        editMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateTripActivity.this, AddPlaceActivity.class);
                intent.putExtra("placeInfos", placeInfos);
                startActivityForResult(intent, ADD_PLACE_REQUEST);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        MarkerOptions markerOptions = new MarkerOptions();
        LatLng latLng = new LatLng(0, 0);
        markerOptions.position(latLng);
        markerOptions.title("I'm Here");
        googleMap.addMarker(markerOptions);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_PLACE_REQUEST) {
            if (resultCode == RESULT_OK) {
                placeInfos = data.getParcelableArrayListExtra("placeInfos");
            }
        }
    }


}
