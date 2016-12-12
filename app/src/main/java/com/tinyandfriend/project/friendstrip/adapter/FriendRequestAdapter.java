package com.tinyandfriend.project.friendstrip.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tinyandfriend.project.friendstrip.info.notification.Notification;
import com.tinyandfriend.project.friendstrip.R;
import com.tinyandfriend.project.friendstrip.factory.NotificationFactory;
import com.tinyandfriend.project.friendstrip.holder.NotificationHolder;

import java.util.ArrayList;

/**
 * Created by NewWy on 13/11/2559.
 */

public class FriendRequestAdapter extends RecyclerView.Adapter<NotificationHolder> {
    private final ArrayList<Notification> notificationList;
    private final NotificationFactory notificationFactory;

    public FriendRequestAdapter(Context context, ArrayList<Notification> notificationList) {
        this.notificationList = notificationList;
        notificationFactory = new NotificationFactory(context);
    }

    @Override
    public NotificationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_accept_friend, parent, false);
        return new NotificationHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NotificationHolder holder, int position) {
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
