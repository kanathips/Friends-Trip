package com.tinyandfriend.project.friendstrip.info;

import android.net.Uri;

/**
 * Created by NewWy on 30/10/2559.
 */
public class ChatMessage {

    private String text;
    private String name;
    private Uri photoUrl;

    public ChatMessage() {
    }

    public ChatMessage(String text, String name, Uri photoUrl) {
        this.text = text;
        this.name = name;
        this.photoUrl = photoUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(Uri photoUrl) {
        this.photoUrl = photoUrl;
    }

}
