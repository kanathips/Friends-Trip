package com.tinyandfriend.project.friendstrip.info;

import java.util.ArrayList;

/**
 * Created by NewWy on 9/10/2559.
 */

public class TripInfo {

    private String tripName;
    private String startDate;
    private String endDate;
    private String tripSpoil;
    private int maxMember;
    private String expense;
    private ArrayList<String> tag;
    private ArrayList<PlaceInfo> placeInfos;
    private ArrayList<String> files;
    private String ownerUID;
    private String thumbnail;


    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getOwnerUID() {
        return ownerUID;
    }

    public void setOwnerUID(String ownerUID) {
        this.ownerUID = ownerUID;
    }

    public ArrayList<String> getFiles() {
        return files;
    }

    public TripInfo() {
    }

    public void setFiles(ArrayList<String> files) {
        this.files = files;
    }

    public int getMaxMember() {
        return maxMember;
    }

    public void setMaxMember(int maxMember) {
        this.maxMember = maxMember;
    }

    public ArrayList<PlaceInfo> getPlaceInfos() {

        return placeInfos;
    }

    public void setPlaceInfos(ArrayList<PlaceInfo> placeInfos) {
        this.placeInfos = placeInfos;
    }

    public ArrayList<String> getTag() {

        return tag;
    }

    public void setTag(ArrayList<String> tag) {
        this.tag = tag;
    }

    public String getExpense() {

        return expense;
    }

    public void setExpense(String expense) {
        this.expense = expense;
    }

    public String getTripSpoil() {

        return tripSpoil;
    }

    public void setTripSpoil(String tripSpoil) {
        this.tripSpoil = tripSpoil;
    }

    public String getEndDate() {

        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartDate() {

        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getTripName() {

        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }
}
