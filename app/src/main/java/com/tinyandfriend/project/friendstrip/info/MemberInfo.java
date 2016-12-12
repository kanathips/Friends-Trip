package com.tinyandfriend.project.friendstrip.info;

/**
 * Created by NewWy on 12/12/2559.
 */

public class MemberInfo
{

    private String name;
    private String uid;
    private String photo;

    public MemberInfo(){}

    public MemberInfo(String name, String uid, String photo) {
        this.name = name;
        this.uid = uid;
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
