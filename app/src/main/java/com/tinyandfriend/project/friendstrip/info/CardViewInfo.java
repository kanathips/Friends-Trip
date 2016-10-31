package com.tinyandfriend.project.friendstrip.info;

import android.net.Uri;

/**
 * Created by StandAlone on 21/10/2559.
 */

public class CardViewInfo {

    private String name_card;
    private String date_card;
    private int count_people;
    private int pic_id;
    private String thumbnail;

    public CardViewInfo(String name_card, String date_card, int count_people, int pic_id) {
        this.name_card = name_card;
        this.date_card = date_card;
        this.count_people = count_people;
        this.pic_id = pic_id;
    }

    public CardViewInfo(String name_card, String date_card, int count_people, String thumbnail) {
        this.name_card = name_card;
        this.date_card = date_card;
        this.count_people = count_people;
        this.thumbnail = thumbnail;
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

    public String getDate_card() {
        return date_card;
    }

    public void setDate_card(String date_card) {
        this.date_card = date_card;
    }

    public int getCount_people() {
        return count_people;
    }

    public void setCount_people(int count_people) {
        this.count_people = count_people;
    }
}
