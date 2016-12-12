package com.tinyandfriend.project.friendstrip.info;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by NewWy on 9/10/2559.
 */

public class TripInfo {

    private String id;
    private String tripName;
    private String startDate;
    private String endDate;
    private String tripSpoil;
    private int maxMember;
    private String expense;
    private Map<String, Boolean> tag;
    private ArrayList<PlaceInfo> placeInfos;
    private ArrayList<String> files;
    private String ownerUID;
    private String thumbnail;
    private String status = "open";
    private PlaceInfo appointPlace;
    private Map<String, MemberInfo> members;


    public Map<String, MemberInfo> getMembers() {
        return members;
    }

    public void setMembers(Map<String, MemberInfo> members) {
        this.members = members;
    }

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

    public Map<String, Boolean> getTag() {

        return tag;
    }

    public void setTag(Map<String, Boolean> tag) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TripInfo tripInfo = (TripInfo) o;

        return id != null ? id.equals(tripInfo.id) : tripInfo.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public void setAppointPlace(PlaceInfo appointPlace) {
        this.appointPlace = appointPlace;
    }

    public PlaceInfo getAppointPlace() {
        return appointPlace;
    }
}

