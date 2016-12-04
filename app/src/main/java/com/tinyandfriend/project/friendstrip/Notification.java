package com.tinyandfriend.project.friendstrip;

import com.tinyandfriend.project.friendstrip.info.NotificationType;

/**
 *
 * Created by NewWy on 11/11/2559
 */

public class Notification {

    private String message;
    private String photoUrl;
    private NotificationType notificationType;
    private String id;
    public Notification() {

    }

    public Notification(String message, String photoUrl) {
        this.message = message;
        this.photoUrl = photoUrl;
    }

    public Notification(String message, String photoUrl, NotificationType notificationType, String id) {
        this.message = message;
        this.photoUrl = photoUrl;
        this.notificationType = notificationType;
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    protected void setNotificationType(NotificationType notificationType) {
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

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Notification that = (Notification) o;

        return id != null ? id.equals(that.id) : that.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public void setId(String id) {
        this.id = id;
    }
}
