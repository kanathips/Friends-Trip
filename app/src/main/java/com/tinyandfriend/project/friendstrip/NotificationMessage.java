package com.tinyandfriend.project.friendstrip;

/**
 *
 * Created by NewWy on 11/11/2559
 */

public class NotificationMessage {

    private String message;
    private String photoUrl;
    private String notificationType;

    public NotificationMessage() {

    }

    public NotificationMessage(String message, String photoUrl) {
        this.message = message;
        this.photoUrl = photoUrl;
    }

    public NotificationMessage(String message, String photoUrl, String notificationType) {
        this.message = message;
        this.photoUrl = photoUrl;
        this.notificationType = notificationType;
    }

    public String getMessage() {
        return message;
    }

    public String getNotificationType() {
        return notificationType;
    }

    protected void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
