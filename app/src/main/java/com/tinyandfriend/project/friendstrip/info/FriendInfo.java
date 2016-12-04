package com.tinyandfriend.project.friendstrip.info;

/**
 * Created by NewWy on 31/10/2559.
 */

public class FriendInfo {
    private String friendPhotoUrl;
    private String friendName;
    private FriendStatus status;
    private String friendUid;

    public String getFriendUid() {
        return friendUid;
    }

    public void setFriendUid(String friendUid) {
        this.friendUid = friendUid;
    }

    public FriendInfo(){

    }

    @Override
    public String toString() {
        return "FriendInfo{" +
                "friendPhotoUrl='" + friendPhotoUrl + '\'' +
                ", friendName='" + friendName + '\'' +
                ", status=" + status +
                ", friendUid='" + friendUid + '\'' +
                '}';
    }

    public FriendInfo(String friendName, String friendUid, String friendPhotoUrl, FriendStatus status) {
        this.friendPhotoUrl = friendPhotoUrl;
        this.friendName = friendName;
        this.friendUid = friendUid;
        this.status =status;
    }

    public FriendStatus getStatus() {
        return status;
    }

    public void setStatus(FriendStatus status) {
        this.status = status;
    }

    public FriendInfo(String friendPhotoUrl, String friendName, FriendStatus status) {
        this.friendPhotoUrl = friendPhotoUrl;
        this.friendName = friendName;
        this.status = status;
    }

    public String getFriendPhotoUrl() {
        return friendPhotoUrl;
    }

    public void setFriendPhotoUrl(String friendPhotoUrl) {
        this.friendPhotoUrl = friendPhotoUrl;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FriendInfo that = (FriendInfo) o;

        return friendUid != null ? friendUid.equals(that.friendUid) : that.friendUid == null;

    }

    @Override
    public int hashCode() {
        return friendName.hashCode();
    }
}
