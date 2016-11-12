package com.tinyandfriend.project.friendstrip;

import com.tinyandfriend.project.friendstrip.info.FriendInfo;

/**
 * Created by NewWy on 11/11/2559.
 */

public class AcceptFriendNotification extends NotificationMessage {

    private String senderUid;
    public static final String TYPE ="AcceptFriend";
    public AcceptFriendNotification() {
        setNotificationType(TYPE);
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
}
