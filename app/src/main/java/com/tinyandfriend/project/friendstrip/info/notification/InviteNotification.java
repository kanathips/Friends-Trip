package com.tinyandfriend.project.friendstrip.info.notification;

/**
 * Created by NewWy on 11/12/2559.
 */

public class InviteNotification extends Notification {

    private String tripId;
    private static final NotificationType notificationType = NotificationType.TripInvite;

    public InviteNotification(String senderName, String senderPhotoUri, String tripId){
        setNotificationType(NotificationType.TripInvite);
        setMessage(senderName + " ต้องการชวนคุณร่วมทริป");
        setPhotoUrl(senderPhotoUri);
        this.tripId = tripId;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public InviteNotification() {
        setNotificationType(NotificationType.TripInvite);
    }
}
