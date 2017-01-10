package com.tinyandfriend.project.friendstrip.info;

/**
 * Created by NewWy on 13/11/2559.
 */

public enum NotificationType {

    AcceptFriend(1), Default(0) ;

    private final int type;

    NotificationType(int type){
        this.type = type;
    }

    public final int getType(){
        return type;
    }
}
