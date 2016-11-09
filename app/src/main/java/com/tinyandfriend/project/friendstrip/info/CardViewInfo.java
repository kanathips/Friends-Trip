package com.tinyandfriend.project.friendstrip.info;

/**
 * Created by StandAlone on 21/10/2559.
 */

public class CardViewInfo {

    private String tripEnd;
    private String name_card;
    private String tripStart;
    private int count_people;
    private int pic_id;
    private String thumbnail;
    private String tripId;

    public String getTripEnd() {
        return tripEnd;
    }

    public void setTripEnd(String tripEnd) {
        this.tripEnd = tripEnd;
    }

    public CardViewInfo(String tripId,String name_card, String tripStart, String tripEnd, int count_people, int pic_id) {
        this.name_card = name_card;
        this.tripStart = tripStart;
        this.tripEnd = tripEnd;
        this.count_people = count_people;
        this.pic_id = pic_id;
        this.tripId = tripId;
    }

    public CardViewInfo(String tripId,String name_card, String tripStart, String tripEnd, int count_people, String thumbnail) {
        this.name_card = name_card;
        this.tripStart = tripStart;
        this.tripEnd = tripEnd;
        this.count_people = count_people;
        this.thumbnail = thumbnail;
        this.tripId = tripId;
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
}
