package com.tinyandfriend.project.friendstrip.info;

import com.tinyandfriend.project.friendstrip.info.PlaceInfo;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by NewWy on 9/10/2559.
 */

public class CreateTripInfo {

    String tripName;
    String startDate;
    String endDate;
    String tripSpoil;
    String numberMember;
    String expense;
    ArrayList<String> tag;
    ArrayList<PlaceInfo> placeInfos;
    TreeMap<String, String> files;

    public TreeMap<String, String> getFiles() {
        return files;
    }

    public void setFiles(TreeMap<String, String> files) {
        this.files = files;
    }

    public String getNumberMember() {
        return numberMember;
    }

    public void setNumberMember(String numberMember) {
        this.numberMember = numberMember;
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
