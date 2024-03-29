package com.tinyandfriend.project.friendstrip.info;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by NewWy on 22/10/2559.
 */

public class FireBaseLatLng implements Parcelable {

    double latitude;
    double longitude;

    public FireBaseLatLng(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected FireBaseLatLng(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<FireBaseLatLng> CREATOR = new Creator<FireBaseLatLng>() {
        @Override
        public FireBaseLatLng createFromParcel(Parcel in) {
            return new FireBaseLatLng(in);
        }

        @Override
        public FireBaseLatLng[] newArray(int size) {
            return new FireBaseLatLng[size];
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

    public FireBaseLatLng(com.google.android.gms.maps.model.LatLng latLng){
        this.latitude = latLng.latitude;
        this.longitude = latLng.longitude;
    }

    public FireBaseLatLng(){

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
