package com.tinyandfriend.project.friendstrip.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tinyandfriend.project.friendstrip.AcceptFriendNotification;
import com.tinyandfriend.project.friendstrip.ConstantValue;
import com.tinyandfriend.project.friendstrip.NotificationMessage;
import com.tinyandfriend.project.friendstrip.R;

public class FragmentNotification extends Fragment {

    private View rootView;
    private ChildEventListener listener;
    private DatabaseReference reference;
    private static final String USER_UID = "userUid";
    private String userUid;
    private Context context;

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

        reference = FirebaseDatabase.getInstance().getReference();

        loadNotification(reference, userUid);
        return rootView;
    }

    private void loadNotification(DatabaseReference reference, String userUid){
        listener = new ChildEventListener(){

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if(dataSnapshot.exists()){
                    String notificationType = dataSnapshot.getValue(NotificationMessage.class).getNotificationType();
                    switch (notificationType){
                        case AcceptFriendNotification.TYPE:
                            AcceptFriendNotification acceptFriendNotification = dataSnapshot.getValue(AcceptFriendNotification.class);
                            break;
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
            }
        };
        reference.child(ConstantValue.NOTIFICATION_CHILD).child(userUid).addChildEventListener(listener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(reference != null && listener != null)
            reference.removeEventListener(listener);
    }
}
