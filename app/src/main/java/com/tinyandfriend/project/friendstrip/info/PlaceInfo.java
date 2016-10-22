package com.tinyandfriend.project.friendstrip.info;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by NewWy on 10/10/2559.
 */
public class PlaceInfo implements Parcelable {
    String name;
    LatLng location;
    int day;
    String address;
    String id;

    public PlaceInfo(Parcel in) {
        name = in.readString();
        location = in.readParcelable(LatLng.class.getClassLoader());
        day = in.readInt();
        address = in.readString();
        id = in.readString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlaceInfo info = (PlaceInfo) o;

        if (day != info.day) return false;
        if (!name.equals(info.name)) return false;
        if (!location.equals(info.location)) return false;
        if (address != null ? !address.equals(info.address) : info.address != null) return false;
        return id.equals(info.id);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + location.hashCode();
        result = 31 * result + day;
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + id.hashCode();
        return result;
    }

    public static final Creator<PlaceInfo> CREATOR = new Creator<PlaceInfo>() {
        @Override
        public PlaceInfo createFromParcel(Parcel in) {
            return new PlaceInfo(in);
        }

        @Override
        public PlaceInfo[] newArray(int size) {
            return new PlaceInfo[size];
        }
    };

    public PlaceInfo() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeParcelable(location, flags);
        dest.writeInt(day);
        dest.writeString(address);
        dest.writeString(id);
    }
}
