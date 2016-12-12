package com.tinyandfriend.project.friendstrip.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.tinyandfriend.project.friendstrip.info.notification.Notification;
import com.tinyandfriend.project.friendstrip.factory.NotificationFactory;
import com.tinyandfriend.project.friendstrip.holder.NotificationHolder;

import java.util.ArrayList;

/**
 * Created by NewWy on 13/11/2559.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationHolder> {
    private final ArrayList<Notification> notificationList;
    private final NotificationFactory notificationFactory;

    public NotificationAdapter(Context context, ArrayList<Notification> notificationList) {
        this.notificationList = notificationList;
        notificationFactory = new NotificationFactory(context);
    }

    @Override
    public NotificationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return notificationFactory.getHolder(viewType, parent);
    }

    @Override
    public void onBindViewHolder(NotificationHolder holder, int position) {
        Notification notification = notificationList.get(position);
        notificationFactory.bindHolder(holder, notification);
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }



    @Override
    public int getItemViewType(int position) {
        return notificationList.get(position).getNotificationType().getType();
    }
}
