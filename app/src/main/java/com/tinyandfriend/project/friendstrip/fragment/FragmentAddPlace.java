package com.tinyandfriend.project.friendstrip.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.tinyandfriend.project.friendstrip.FragmentPager;
import com.tinyandfriend.project.friendstrip.MapUtils;
import com.tinyandfriend.project.friendstrip.R;
import com.tinyandfriend.project.friendstrip.activity.AddPlaceActivity;
import com.tinyandfriend.project.friendstrip.info.PlaceInfo;
import com.tinyandfriend.project.friendstrip.info.TripInfo;
import com.tinyandfriend.project.friendstrip.view.DateSelect;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
public class FragmentAddPlace extends FragmentPager implements OnMapReadyCallback {

    private static final String TAG = "ADD_PLACE_FRAGMENT";
    private static final int ADD_PLACE_REQUEST = 0;
    private int pixelWidth;
    private int pixelHeight;
    private ArrayList<PlaceInfo> placeInfos;
    private Context context;
    private View rootView;
    private GoogleMap googleMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_add_place, container, false);

        context = getContext();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        pixelWidth = getResources().getDisplayMetrics().widthPixels;
        pixelHeight = getResources().getDisplayMetrics().heightPixels;

        placeInfos = new ArrayList<>();

        //Add Toolbar

        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);

        //Add datepicker to edittext

        final EditText startTripEditText = (EditText) rootView.findViewById(R.id.trip_start);
        final ImageButton startImageButton = (ImageButton) rootView.findViewById(R.id.alert_dialog_start_date);

        startImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateSelect dateSelect = new DateSelect(context, startTripEditText);
                dateSelect.show();
            }
        });

        final EditText endTripEditText = (EditText) rootView.findViewById(R.id.trip_end);
        final ImageButton endImageButton = (ImageButton) rootView.findViewById(R.id.alert_dialog_end_date);

        endImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateSelect dateSelect = new DateSelect(context, endTripEditText);
                dateSelect.show();
            }
        });

        MapFragment mapFragment = (MapFragment) activity.getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button editMapButton = (Button) rootView.findViewById(R.id.editMap);
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

        if(!isValidTripDate(startEditText, endEditText)){
            return;
        }
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
        long startTime = getTime(simpleDateFormat, startDate);
        long endTime = getTime(simpleDateFormat, endDate);
        diff = Math.round((endTime - startTime) / (double) 86400000);
        return diff + 1;
    }

    private long getTime(SimpleDateFormat dateFormat, String date) throws ParseException {
        return dateFormat.parse(date).getTime();
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
                MapUtils mapUtils = new MapUtils(googleMap);
                mapUtils.markPlace(placeInfos, pixelWidth, pixelHeight);
            }
        }
    }

    public boolean validateFrom() {
        boolean valid = true;
        String checkString;
        String errorText;
        EditText editText;

        if (placeInfos.size() == 0) {
            valid = false;
            Toast.makeText(context, "กรุณาเพิ่มสถานที่ท่องเที่ยว", Toast.LENGTH_SHORT).show();
        }


        MaterialEditText editText_name = (MaterialEditText) rootView.findViewById(R.id.trip_name);
        TextInputLayout textInputLayout;

        checkString = editText_name.getText().toString();
        if (checkString.isEmpty()) {
            valid = false;
            errorText = "กรุณาตั้งชื่อห้อง";
        }else if(checkString.length()>28) {
            valid = false;
            errorText = "กรุณากรอกชื่อห้องไม่เกิน 28 ตัวอักษร";
        }else {
            errorText = null;
        }
        editText_name.setError(errorText);

        editText = (EditText) rootView.findViewById(R.id.number_member);
        textInputLayout = (TextInputLayout) rootView.findViewById(R.id.number_member_layout);
        checkString = editText.getText().toString();
        if (checkString.isEmpty()) {
            valid = false;
            errorText = "กรุณาใส่จำนวนผู้เข้าร่วม";
        }else if(Integer.parseInt(checkString) > 20){
            valid = false;
            errorText = "กรุณาใส่จำนวนผู้เข้าร่วมไม่เกิน 20 คน";
        }else {
            errorText = null;
        }
//        editText.setError(errorText);
        textInputLayout.setError(errorText);


        MaterialEditText trip_spoil = (MaterialEditText) rootView.findViewById(R.id.trip_spoil);
        textInputLayout = (TextInputLayout) rootView.findViewById(R.id.trip_spoil_layout);

        checkString = trip_spoil.getText().toString();
        if (checkString.length() > 256) {
            valid = false;
            errorText = "กรุณาใส่ไม่เกิน 128 ตัวอักษร";
        } else {
            errorText = null;
        }
//        trip_spoil.setError(errorText);
        textInputLayout.setError(errorText);

        EditText startDate = (EditText) rootView.findViewById(R.id.trip_start);

        EditText endDate = (EditText) rootView.findViewById(R.id.trip_end);
        boolean validDate = isValidTripDate(startDate, endDate);
        return valid && validDate;

    }

    private boolean isValidTripDate(EditText startEditText, EditText endEditText) {

        String startDate = startEditText.getText().toString();
        String errorText;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        long startTime = 0, endTime = 0;
        boolean valid = true;

        try {
            if (startDate.isEmpty()) {
                errorText = "กรุณาใส่วันเริ่มทริป";
                valid = false;
            } else {
                startTime = getTime(simpleDateFormat, startDate);
                errorText = null;
            }
        } catch (ParseException e) {
            errorText = "รูปแบบวันไม่ถูกต้อง";
            valid = false;
        }
        startEditText.setError(errorText);

        String endDate = endEditText.getText().toString();

        try {
            if (endDate.isEmpty()) {
                errorText = "กรุณาใส่วันสิ้นสุดทริป";
                valid = false;
            } else {
                endTime = getTime(simpleDateFormat, endDate);
                if (endTime < startTime) {
                    errorText = "วันเริ่มต้นต้องมาก่อนวันจบ";
                    valid = false;
                } else {
                    errorText = null;
                }
            }
        } catch (ParseException e) {
            errorText = "รูปแบบวันไม่ถูกต้อง";
            valid = false;
        }
        endEditText.setError(errorText);

        endDate = endEditText.getText().toString();

        try {
            if (endDate.isEmpty()) {
                errorText = "กรุณาใส่วันสิ้นสุดทริป";
                valid = false;
            } else {
                endTime = getTime(simpleDateFormat, endDate);
                if (endTime < startTime) {
                    errorText = "วันเริ่มต้นต้องมาก่อนวันจบ";
                    valid = false;
                } else {
                    errorText = null;
                }
            }
        } catch (ParseException e) {
            errorText = "รูปแบบวันไม่ถูกต้อง";
            valid = false;
        }
        endEditText.setError(errorText);

        return valid;
    }

    @Override
    public void setInfo(Object createTripInfo) {
        TripInfo tripInfo = (TripInfo) createTripInfo;

        EditText editText;
        MaterialEditText editText_name;

        editText = (EditText) rootView.findViewById(R.id.trip_start);
        tripInfo.setStartDate(editText.getText().toString());

        editText = (EditText) rootView.findViewById(R.id.trip_end);
        tripInfo.setEndDate(editText.getText().toString());

        editText = (EditText)rootView.findViewById(R.id.trip_spoil);
        tripInfo.setTripSpoil(editText.getText().toString());

        tripInfo.setPlaceInfos(placeInfos);

        editText = (EditText) rootView.findViewById(R.id.expend);
        tripInfo.setExpense(editText.getText().toString());

        editText = (EditText) rootView.findViewById(R.id.number_member);
        tripInfo.setMaxMember(Integer.parseInt(editText.getText().toString()));

        editText_name = (MaterialEditText) rootView.findViewById(R.id.trip_name);
        tripInfo.setTripName(editText_name.getText().toString());

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
