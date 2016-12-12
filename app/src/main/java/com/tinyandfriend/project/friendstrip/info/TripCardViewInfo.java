package com.tinyandfriend.project.friendstrip.info;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by StandAlone on 21/10/2559.
 */

public class TripCardViewInfo implements Parcelable {

    private String tripEnd;
    private String name_card;
    private String tripStart;
    private int count_people;
    private int pic_id;
    private String thumbnail;
    private String tripId;
    private String tripSpoil;

    protected TripCardViewInfo(Parcel in) {
        tripEnd = in.readString();
        name_card = in.readString();
        tripStart = in.readString();
        count_people = in.readInt();
        pic_id = in.readInt();
        thumbnail = in.readString();
        tripId = in.readString();
        tripSpoil = in.readString();
    }

    public static final Creator<TripCardViewInfo> CREATOR = new Creator<TripCardViewInfo>() {
        @Override
        public TripCardViewInfo createFromParcel(Parcel in) {
            return new TripCardViewInfo(in);
        }

        @Override
        public TripCardViewInfo[] newArray(int size) {
            return new TripCardViewInfo[size];
        }
    };

    public String getTripEnd() {
        return tripEnd;
    }

    public void setTripEnd(String tripEnd) {
        this.tripEnd = tripEnd;
    }

    public TripCardViewInfo(String tripId) {
        this.tripId = tripId;
    }

    public TripCardViewInfo(String tripId, String name_card, String tripStart, String tripEnd, int count_people, String tripSpoil, int pic_id) {
        this.name_card = name_card;
        this.tripStart = tripStart;
        this.tripEnd = tripEnd;
        this.count_people = count_people;
        this.pic_id = pic_id;
        this.tripId = tripId;
        this.tripSpoil = tripSpoil;
    }

    public TripCardViewInfo(String tripId, String name_card, String tripStart, String tripEnd, int count_people, String tripSpoil, String thumbnail) {
        this.name_card = name_card;
        this.tripStart = tripStart;
        this.tripEnd = tripEnd;
        this.count_people = count_people;
        this.thumbnail = thumbnail;
        this.tripId = tripId;
        this.tripSpoil = tripSpoil;
    }

    public String getTripSpoil() {
        return tripSpoil;
    }

    public void setTripSpoil(String tripSpoil) {
        this.tripSpoil = tripSpoil;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getPic_id() {
        return pic_id;
    }

    public void setPic_id(int pic_id) {
        this.pic_id = pic_id;
    }

    public String getName_card() {
        return name_card;
    }

    public void setName_card(String name_card) {
        this.name_card = name_card;
    }

    public String getTripStart() {
        return tripStart;
    }

    public void setTripStart(String tripStart) {
        this.tripStart = tripStart;
    }

    public int getCount_people() {
        return count_people;
    }

    public void setCount_people(int count_people) {
        this.count_people = count_people;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TripCardViewInfo that = (TripCardViewInfo) o;

        return tripId != null ? tripId.equals(that.tripId) : that.tripId == null;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tripEnd);
        dest.writeString(name_card);
        dest.writeString(tripStart);
        dest.writeInt(count_people);
        dest.writeInt(pic_id);
        dest.writeString(thumbnail);
        dest.writeString(tripId);
        dest.writeString(tripSpoil);
    }
}
