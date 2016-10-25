package com.tinyandfriend.project.friendstrip.info;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by NewWy on 22/10/2559.
 */

public class LatLng implements Parcelable {

    double latitude;
    double longitude;

    public LatLng(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected LatLng(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<LatLng> CREATOR = new Creator<LatLng>() {
        @Override
        public LatLng createFromParcel(Parcel in) {
            return new LatLng(in);
        }

        @Override
        public LatLng[] newArray(int size) {
            return new LatLng[size];
        }
    };

    public com.google.android.gms.maps.model.LatLng toGmsLatLng(){
        return new com.google.android.gms.maps.model.LatLng(latitude, longitude);
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public  LatLng (com.google.android.gms.maps.model.LatLng latLng){
        this.latitude = latLng.latitude;
        this.longitude = latLng.longitude;
    }

    public LatLng(){

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }
}
