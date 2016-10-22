package com.tinyandfriend.project.friendstrip;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tinyandfriend.project.friendstrip.info.CreateTripInfo;
import com.tinyandfriend.project.friendstrip.info.PlaceInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by NewWy on 22/10/2559.
 */

public class AddPlaceFragment extends FragmentPager implements OnMapReadyCallback {

    private static final String TAG = "ADD_PLACE_FRAGMENT";
    private static final int ADD_PLACE_REQUEST = 0;
    private int pixelWidth;
    private int pixelHeight;
    private ArrayList<PlaceInfo> placeInfos;
    private Context context;
    private AppCompatActivity activity;
    private View rootView;
    private GoogleMap googleMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.add_place_fragment, container, false);

        context = getContext();
        activity = (AppCompatActivity)getActivity();
        pixelWidth = getResources().getDisplayMetrics().widthPixels;
        pixelHeight = getResources().getDisplayMetrics().heightPixels;

        placeInfos = new ArrayList<>();;

        //Add Toolbar

        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);

        //Add datepicker to edittext

        final EditText startTripEditText = (EditText) rootView.findViewById(R.id.trip_start);
        startTripEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateSelect dateSelect = new DateSelect(context, startTripEditText);
                dateSelect.show();
            }
        });

        final EditText endTripEditText = (EditText) rootView.findViewById(R.id.trip_end);

        endTripEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateSelect dateSelect = new DateSelect(context, endTripEditText);
                dateSelect.show();
            }
        });

        MapFragment mapFragment = (MapFragment) activity.getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button editMapButton = (Button)rootView.findViewById(R.id.editMap);
        editMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickEditMap();
            }
        });
        return rootView;
    }


    public void onClickEditMap() {
        EditText startEditText = (EditText) rootView.findViewById(R.id.trip_start);
        EditText endEditText = (EditText) rootView.findViewById(R.id.trip_end);
        long tripDuration;
        try {
            String startDate = startEditText.getText().toString();
            String endDate = endEditText.getText().toString();

            tripDuration = calculateTripDay(startDate, endDate);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            tripDuration = 0;
        }
        Intent intent = new Intent(context, AddPlaceActivity.class);
        intent.putExtra("placeInfos", placeInfos);

        intent.putExtra("tripDuration", tripDuration);
        startActivityForResult(intent, ADD_PLACE_REQUEST);
    }

    public long calculateTripDay(String startDate, String endDate) throws ParseException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        long diff;
        Date dateStart = simpleDateFormat.parse(startDate);
        Date dateEnd = simpleDateFormat.parse(endDate);
        diff = Math.round((dateEnd.getTime() - dateStart.getTime()) / (double) 86400000);
        return diff + 1;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        this.googleMap = googleMap;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_PLACE_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                placeInfos = data.getParcelableArrayListExtra("placeInfos");
                LatLngBounds.Builder builder = new LatLngBounds.Builder();

                for (int i = 0; i < placeInfos.size(); i++) {
                    PlaceInfo info = placeInfos.get(i);
                    MarkerOptions options = new MarkerOptions();
                    options.position(info.getLocation());
                    options.title(info.getName());
                    builder.include(info.getLocation());
                    googleMap.addMarker(options);
                }

                if (placeInfos.size() > 1) {
                    LatLngBounds latLngBounds = builder.build();
                    int padding = (int) (pixelWidth * 0.15);
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(latLngBounds, pixelWidth, pixelHeight, padding);
                    googleMap.animateCamera(cu);
                } else if (placeInfos.size() == 1) {
                    PlaceInfo info = placeInfos.get(0);
                    CameraPosition test = CameraPosition.fromLatLngZoom(info.getLocation(), 17);
                    CameraUpdate cu = CameraUpdateFactory.newCameraPosition(test);
                    googleMap.animateCamera(cu);
                }
            }
        }
    }

    public boolean validateFrom() {
        boolean valid = true;
        String checkString;
        String errorText;
        if(placeInfos.size() == 0){
            valid = false;
            Toast.makeText(context, "กรุณาเพิ่มสถานที่ท่องเที่ยว", Toast.LENGTH_SHORT).show();
        }

        EditText editText = (EditText)rootView.findViewById(R.id.trip_name);
        checkString = editText.getText().toString();
        if(checkString.isEmpty()){
            valid = false;
            errorText = "กรุณาตั้งชื่อห้อง";
        }else{
            errorText = null;
        }
        editText.setError(errorText);

        editText = (EditText)rootView.findViewById(R.id.trip_start);
        checkString = editText.getText().toString();
        if(checkString.isEmpty()){
            errorText = "กรุณาใส่วันเริ่มทริป";
        }else {
            errorText = null;
        }
        editText.setError(errorText);

        editText = (EditText)rootView.findViewById(R.id.trip_end);
        checkString = editText.getText().toString();
        if(checkString.isEmpty()){
            errorText = "กรุณาใส่วันสิ้นสุดทริป";
        }else {
            errorText = null;
        }
        editText.setError(errorText);

        editText = (EditText)rootView.findViewById(R.id.number_member);
        checkString = editText.getText().toString();
        if(checkString.isEmpty()){
            errorText = "กรุณาใส่จำนวนผู้เข้าร่วม";
        }else {
            errorText = null;
        }
        editText.setError(errorText);

        editText = (EditText)rootView.findViewById(R.id.expend);
        checkString = editText.getText().toString();
        if(checkString.isEmpty()){
            errorText = "กรุณาใส่ค่าใช้จ่ายโดยประมาณ";
        }else {
            errorText = null;
        }
        editText.setError(errorText);


        return valid;
    }

    @Override
    public void setInfo(Object createTripInfo) {
        CreateTripInfo tripInfo = (CreateTripInfo)createTripInfo;

        EditText editText;

        editText = (EditText)rootView.findViewById(R.id.trip_start);
        tripInfo.setStartDate(editText.getText().toString());

        editText = (EditText)rootView.findViewById(R.id.trip_end);
        tripInfo.setEndDate(editText.getText().toString());

        tripInfo.setPlaceInfos(placeInfos);

        editText = (EditText)rootView.findViewById(R.id.expend);
        tripInfo.setExpense(editText.getText().toString());

        editText = (EditText)rootView.findViewById(R.id.number_member);
        tripInfo.setNumberMember(editText.getText().toString());

        editText = (EditText)rootView.findViewById(R.id.trip_name);
        tripInfo.setTripName(editText.getText().toString());
    }



    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
