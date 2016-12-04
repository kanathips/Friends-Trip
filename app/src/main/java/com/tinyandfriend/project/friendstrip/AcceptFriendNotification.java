package com.tinyandfriend.project.friendstrip;

import com.google.firebase.database.Exclude;
import com.tinyandfriend.project.friendstrip.info.FriendInfo;
import com.tinyandfriend.project.friendstrip.info.NotificationType;

/**
 * Created by NewWy on 11/11/2559.
 */

public class AcceptFriendNotification extends Notification {

    private String senderUid;
    private String id;
    private static final NotificationType TYPE = NotificationType.AcceptFriend;
    public AcceptFriendNotification() {
        setNotificationType(TYPE);
    }

    public AcceptFriendNotification(FriendInfo friendInfo, String id) {
        this.id = id;
        setNotificationType(TYPE);
        String message = friendInfo.getFriendName() +" ต้องการเป็นเพื่อนกับคุณ";
        String photoUrl = friendInfo.getFriendPhotoUrl();
        setSenderUid(friendInfo.getFriendUid());
        setMessage(message);
        setPhotoUrl(photoUrl);
    }

    public AcceptFriendNotification(FriendInfo friendInfo) {
        setNotificationType(TYPE);
        String message = friendInfo.getFriendName() +" ต้องการเป็นเพื่อนกับคุณ";
        String photoUrl = friendInfo.getFriendPhotoUrl();
        setSenderUid(friendInfo.getFriendUid());
        setMessage(message);
        setPhotoUrl(photoUrl);
    }

    public String getSenderUid() {
        return senderUid;
    }

    private void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
