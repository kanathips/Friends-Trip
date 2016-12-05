package com.tinyandfriend.project.friendstrip.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tinyandfriend.project.friendstrip.ConstantValue;
import com.tinyandfriend.project.friendstrip.Notification;
import com.tinyandfriend.project.friendstrip.R;
import com.tinyandfriend.project.friendstrip.adapter.NotificationAdapter;
import com.tinyandfriend.project.friendstrip.info.NotificationType;

import java.util.ArrayList;

public class FragmentNotification extends Fragment {

    private View rootView;
    private ChildEventListener listener;
    private DatabaseReference reference;
    private static final String USER_UID = "userUid";
    private String userUid;
    private Context context;
    private RecyclerView notificationArea;
    private NotificationAdapter notificationAdapter;
    private ArrayList<Notification> notificationList;

    public static FragmentNotification newInstance(String userUid) {
        FragmentNotification fragment = new FragmentNotification();
        Bundle args = new Bundle();
        args.putString(USER_UID, userUid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reference = FirebaseDatabase.getInstance().getReference();
        if (getArguments() != null) {
            userUid = getArguments().getString(USER_UID);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_notification, container, false);
        Toast.makeText(context, "B4 LOAD", Toast.LENGTH_SHORT).show();
        loadNotification(reference, userUid);
        Toast.makeText(context, "AFTER LOAD", Toast.LENGTH_SHORT).show();
        notificationArea = (RecyclerView)rootView.findViewById(R.id.notification_area);
        notificationList = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(context, notificationList);
        notificationArea.setAdapter(notificationAdapter);
        notificationArea.setLayoutManager(new LinearLayoutManager(context));
        notificationArea.setItemAnimator(new DefaultItemAnimator());

        return rootView;
    }

    private void loadNotification(DatabaseReference reference, String userUid) {
        listener = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                if (dataSnapshot.exists()) {
                    Notification notification = dataSnapshot.getValue(Notification.class);
                    NotificationType notificationType = notification.getNotificationType();
                    String notificationId = dataSnapshot.getKey();

                    if (notificationList.contains(notification)) {
                        return;
                    }
                    switch (notificationType) {
//                        case AcceptFriend:
//                            notification = dataSnapshot.getValue(AcceptFriendNotification.class);
//                            notification.setId(notificationId);
//                            notificationList.add(notification);
//                            notificationAdapter.notifyDataSetChanged();
//                            break;
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Toast.makeText(context, "Change", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Toast.makeText(context, "Remove", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        Toast.makeText(context, userUid, Toast.LENGTH_SHORT).show();
        reference.child(ConstantValue.NOTIFICATION_CHILD).child(userUid).addChildEventListener(listener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (reference != null && listener != null)
            reference.removeEventListener(listener);
    }
}
