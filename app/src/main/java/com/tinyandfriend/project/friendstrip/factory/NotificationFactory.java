package com.tinyandfriend.project.friendstrip.factory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tinyandfriend.project.friendstrip.AcceptFriendNotification;
import com.tinyandfriend.project.friendstrip.ConstantValue;
import com.tinyandfriend.project.friendstrip.Notification;
import com.tinyandfriend.project.friendstrip.R;
import com.tinyandfriend.project.friendstrip.holder.NotiAcptFriendHolder;
import com.tinyandfriend.project.friendstrip.holder.NotificationHolder;
import com.tinyandfriend.project.friendstrip.info.FriendStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.tinyandfriend.project.friendstrip.info.FriendStatus.Friend;

public class NotificationFactory {

    private final DatabaseReference reference;
    private Context context;
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public NotificationFactory(Context context){

        this.context = context;
        reference = FirebaseDatabase.getInstance().getReference();
    }

    public NotificationHolder getHolder(int type, ViewGroup parent){
        View itemView;
        switch (type) {
            case 0:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.notification_default, parent, false);
                return new NotificationHolder(itemView);
            case 1:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.notification_accept_friend, parent, false);
                return new NotificationHolder(itemView);

            default:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.notification_default, parent, false);
                return new NotificationHolder(itemView);
        }
    }

    public void bindHolder(NotificationHolder holder, Notification notification){

        if (notification.getPhotoUrl() != null)
            Glide.with(context).load(notification.getPhotoUrl()).into(holder.getNotificationPhoto());
        holder.getNotificationTextView().setText(notification.getMessage());

        Toast.makeText(context, holder.getType().toString(), Toast.LENGTH_SHORT).show();
        switch (holder.getType()){
            case AcceptFriend:
                final AcceptFriendNotification acptFrndNotiInfo = (AcceptFriendNotification) notification;
                NotiAcptFriendHolder notiAcptFriendHolder = (NotiAcptFriendHolder) holder;

                notiAcptFriendHolder.getAcceptButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String senderUid = acptFrndNotiInfo.getSenderUid();
                        reference.child(ConstantValue.NOTIFICATION_CHILD).child(acptFrndNotiInfo.getId()).child(senderUid).removeValue();
                        Map<String, Object> friendMap  = new HashMap<>();
                        friendMap.put("status", Friend);

                        reference.child(ConstantValue.FRIEND_LIST_CHILD).child(senderUid).child(user.getUid()).updateChildren(friendMap);
                        reference.child(ConstantValue.FRIEND_LIST_CHILD).child(user.getUid()).child(senderUid).updateChildren(friendMap);
                    }
                });

                notiAcptFriendHolder.getAcceptButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String senderUid = acptFrndNotiInfo.getSenderUid();
                        reference.child(ConstantValue.NOTIFICATION_CHILD).child(acptFrndNotiInfo.getId()).child(senderUid).removeValue();
                        reference.child(ConstantValue.FRIEND_LIST_CHILD).child(senderUid).child(user.getUid()).removeValue();
                        reference.child(ConstantValue.FRIEND_LIST_CHILD).child(user.getUid()).child(senderUid).removeValue();
                    }
                });

                break;
        }

    }
}
