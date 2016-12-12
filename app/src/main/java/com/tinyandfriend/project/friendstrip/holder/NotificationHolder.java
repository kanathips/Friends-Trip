package com.tinyandfriend.project.friendstrip.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tinyandfriend.project.friendstrip.R;
import com.tinyandfriend.project.friendstrip.info.notification.NotificationType;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationHolder extends RecyclerView.ViewHolder {

    private CircleImageView notificationPhoto;
    private TextView notificationTextView;
    private NotificationType type;
    private View view;

    public NotificationHolder(View itemView) {
        super(itemView);
        view = itemView;
        setType(NotificationType.Default);
        notificationPhoto = (CircleImageView) itemView.findViewById(R.id.noti_photo);
        notificationTextView = (TextView) itemView.findViewById(R.id.noti_message);
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public TextView getNotificationTextView() {
        return notificationTextView;
    }

    public CircleImageView getNotificationPhoto() {
        return notificationPhoto;
    }

    public View getView() {
        return view;
    }
}