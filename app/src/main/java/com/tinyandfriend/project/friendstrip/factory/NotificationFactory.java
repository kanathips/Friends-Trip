package com.tinyandfriend.project.friendstrip.factory;

import android.content.Intent;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tinyandfriend.project.friendstrip.ConstantValue;
import com.tinyandfriend.project.friendstrip.R;
import com.tinyandfriend.project.friendstrip.activity.TripReviewActivity;
import com.tinyandfriend.project.friendstrip.holder.NotiAcptFriendHolder;
import com.tinyandfriend.project.friendstrip.holder.NotificationHolder;
import com.tinyandfriend.project.friendstrip.info.notification.AcceptFriendNotification;
import com.tinyandfriend.project.friendstrip.info.notification.InviteNotification;
import com.tinyandfriend.project.friendstrip.info.notification.Notification;

import java.util.HashMap;
import java.util.Map;

import static com.tinyandfriend.project.friendstrip.info.FriendStatus.Friend;
import static com.tinyandfriend.project.friendstrip.info.notification.NotificationType.TripInvite;

public class NotificationFactory {

    private final DatabaseReference reference;
    private final String userUid;
    private Context context;
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public NotificationFactory(Context context){

        this.context = context;
        reference = FirebaseDatabase.getInstance().getReference();
        userUid = user.getUid();
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
                return new NotiAcptFriendHolder(itemView);
            case 2:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.notification_default, parent, false);
                NotificationHolder holder = new NotificationHolder(itemView);
                holder.setType(TripInvite);
                return holder;
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
        Log.i("Bind Notificaiton", "Bind Type "+ holder.getType());
        switch (holder.getType()){
            case AcceptFriend:
                final AcceptFriendNotification acptFrndNotiInfo = (AcceptFriendNotification) notification;
                NotiAcptFriendHolder notiAcptFriendHolder = (NotiAcptFriendHolder) holder;
                final String senderUid = acptFrndNotiInfo.getSenderUid();
                notiAcptFriendHolder.getAcceptButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reference.child(ConstantValue.NOTIFICATION_CHILD).child(userUid).child(acptFrndNotiInfo.getId()).removeValue();
                        Map<String, Object> friendMap  = new HashMap<>();
                        friendMap.put("status", Friend);
                        reference.child(ConstantValue.FRIEND_LIST_CHILD).child(senderUid).child(userUid).updateChildren(friendMap);
                        reference.child(ConstantValue.FRIEND_LIST_CHILD).child(userUid).child(senderUid).updateChildren(friendMap);
                    }
                });

                notiAcptFriendHolder.getDenidedButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reference.child(ConstantValue.NOTIFICATION_CHILD).child(userUid).child(acptFrndNotiInfo.getId()).removeValue();
                        reference.child(ConstantValue.FRIEND_LIST_CHILD).child(senderUid).child(userUid).removeValue();
                        reference.child(ConstantValue.FRIEND_LIST_CHILD).child(userUid).child(senderUid).removeValue();
                    }
                });
                holder.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "Test", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case TripInvite:
                final InviteNotification inviteNotification =  (InviteNotification)notification;
                holder.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, TripReviewActivity.class);
                        intent.putExtra(ConstantValue.TRIP_ID_CHILD, inviteNotification.getTripId());
                        context.startActivity(intent);
                    }
                });
                break;
        }

    }
}
