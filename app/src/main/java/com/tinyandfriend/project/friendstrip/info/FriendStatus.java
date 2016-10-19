package com.tinyandfriend.project.friendstrip.info;

/**
 * Created by NewWy on 16/10/2559.
 */

public enum FriendStatus {

    Pending(0), Approving(2), Ban(-1), Friend(1);

    FriendStatus(int status){
        setStatus(status);
    }

    private void setStatus(int status){
        this.status = status;
    }

    public int getStatus(){
        return status;
    }

    int status;
}
