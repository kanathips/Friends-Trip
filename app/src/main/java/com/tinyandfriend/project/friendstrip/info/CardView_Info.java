package com.tinyandfriend.project.friendstrip.info;

/**
 * Created by StandAlone on 21/10/2559.
 */

public class CardView_Info {

    private String name_card;
    private String date_card;
    private String count_people;
    private int pic_id;

    public CardView_Info(String name_card, String date_card, String count_people, int pic_id) {
        this.name_card = name_card;
        this.date_card = date_card;
        this.count_people = count_people;
        this.pic_id = pic_id;
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

    public String getCount_people() {
        return count_people;
    }

    public void setCount_people(String count_people) {
        this.count_people = count_people;
    }
}
