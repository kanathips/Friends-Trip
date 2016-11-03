package com.tinyandfriend.project.friendstrip.info;

/**
 * Created by NewWy on 31/10/2559.
 */

public class FriendListInfo {
    String friendPhotoUrl;
    String friendName;
    FriendStatus status;

    public FriendListInfo (){

    }

    public FriendStatus getStatus() {
        return status;
    }

    public void setStatus(FriendStatus status) {
        this.status = status;
    }

    public FriendListInfo(String friendPhotoUrl, String friendName, FriendStatus status) {
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
}
