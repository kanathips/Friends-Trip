package com.tinyandfriend.project.friendstrip.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tinyandfriend.project.friendstrip.info.notification.AcceptFriendNotification;
import com.tinyandfriend.project.friendstrip.ConstantValue;
import com.tinyandfriend.project.friendstrip.R;
import com.tinyandfriend.project.friendstrip.info.FriendInfo;

import java.util.ArrayList;
import java.util.Iterator;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.tinyandfriend.project.friendstrip.info.FriendStatus.Approving;
import static com.tinyandfriend.project.friendstrip.info.FriendStatus.Pending;

public class AddFriendActivity extends AppCompatActivity {

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private Button addButton;
    private TextView targetNameTextView;
    private ProgressDialog progressDialog;
    private String userUid;
    private String userDisplayName;
    private ArrayList<ValueEventListener> listeners;
    private CircleImageView friendPhoto;
    private String userProfilePhotoUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        addButton = (Button) findViewById(R.id.add_friend_button);
        targetNameTextView = (TextView) findViewById(R.id.target_name);
        progressDialog = new ProgressDialog(this);
        Button searchButton = (Button) findViewById(R.id.search_button);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userUid = user.getUid();
        userDisplayName = user.getDisplayName();
        if (user.getPhotoUrl() == null) {
            userProfilePhotoUrl = null;
        } else {
            userProfilePhotoUrl = user.getPhotoUrl().toString();
        }
        listeners = new ArrayList<>();
        friendPhoto = (CircleImageView) findViewById(R.id.friend_photo);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = ((EditText) findViewById(R.id.search_text)).getText().toString();
                onclickSearch(searchText);
            }
        });
    }

    public void onclickSearch(String searchText) {

        progressDialog.setMessage("กำลังค้นหา");
        progressDialog.show();

        if (searchText.equals(userDisplayName) || searchText.isEmpty()) {
            targetNameTextView.setText(null);
            friendPhoto.setVisibility(View.INVISIBLE);
            friendPhoto.setImageBitmap(null);
            targetNameTextView.setVisibility(View.INVISIBLE);
            addButton.setVisibility(View.INVISIBLE);
            addButton.setOnClickListener(null);
            progressDialog.dismiss();
            return;
        }

        ValueEventListener listener = new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    final String targetUid;
                    final String targetProfilePhotoUrl;

                    DataSnapshot target;
                    Iterator<DataSnapshot> o = dataSnapshot.getChildren().iterator();

                    if (o.hasNext()) {
                        target = o.next();
                        if (!target.child("displayName").exists()) {
                            targetNameTextView.setText("ไม่พบผู้ใช้งาน");
                            targetNameTextView.setVisibility(View.VISIBLE);
                            addButton.setVisibility(View.INVISIBLE);
                            addButton.setOnClickListener(null);
                            progressDialog.dismiss();
                            return;
                        }
                        targetUid = target.getKey();
                        Log.i("FRIEND_LIST", "TARGET : " + targetUid);
                        Log.i("FRIEND_LIST", "ME : " + userUid);
                        final String targetName = target.child("displayName").getValue().toString();
                        targetNameTextView.setVisibility(View.VISIBLE);
                        targetNameTextView.setText(targetName);
                        friendPhoto.setVisibility(View.VISIBLE);
                        if (target.child("profilePhoto").exists()) {
                            targetProfilePhotoUrl = target.child("profilePhoto").getValue().toString();
                            Glide.with(AddFriendActivity.this).load(targetProfilePhotoUrl).centerCrop().into(friendPhoto);
                        } else {
                            targetProfilePhotoUrl = null;
                        }
                        ValueEventListener friendCheckListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                addButton.setVisibility(View.VISIBLE);
                                if (dataSnapshot.exists()) {

                                    addButton.setEnabled(false);
                                    addButton.setText("เป็นเพื่อนกันแล้ว");
                                } else {
                                    addButton.setEnabled(true);
                                    addButton.setText("เพิ่มเพื่อน");
                                    addButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            addButton.setEnabled(false);
                                            FriendInfo targetFriendInfo = new FriendInfo(targetName, targetUid, targetProfilePhotoUrl, Pending);
                                            FriendInfo senderFriendInfo = new FriendInfo(userDisplayName, userUid, userProfilePhotoUrl, Approving);
                                            addFriend(targetFriendInfo, senderFriendInfo);
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        };
                        reference.child(ConstantValue.FRIEND_LIST_CHILD).child(userUid).child(targetUid).addListenerForSingleValueEvent(friendCheckListener);
                        listeners.add(friendCheckListener);
                    } else {
                        targetNameTextView.setText(null);
                        targetNameTextView.setVisibility(View.INVISIBLE);
                        addButton.setVisibility(View.INVISIBLE);
                        addButton.setOnClickListener(null);
                        friendPhoto.setImageBitmap(null);
                        friendPhoto.setVisibility(View.INVISIBLE);
                    }

                } else {
                    targetNameTextView.setText("ไม่พบผู้ใช้งาน");
                    targetNameTextView.setVisibility(View.VISIBLE);
                    addButton.setVisibility(View.INVISIBLE);
                    addButton.setOnClickListener(null);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AddFriendActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        };

        Query query = reference.child(ConstantValue.DISPLAY_NAME_INDEX_CHILD).orderByChild("displayName").startAt(searchText);
        query.addListenerForSingleValueEvent(listener);
        listeners.add(listener);
    }

    private void addFriend(final FriendInfo targetFriendInfo, final FriendInfo senderFriendInfo) {
        final ProgressDialog tempProgressDialog = new ProgressDialog(this);
        final int[] checkStatus = {1};
        tempProgressDialog.setMessage("กำลังเพิ่มเพื่อน");
        tempProgressDialog.show();

        //sender
        reference.child(ConstantValue.FRIEND_LIST_CHILD).child(senderFriendInfo.getFriendUid()).child(targetFriendInfo.getFriendUid()).setValue(targetFriendInfo)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        checkAddComplete(checkStatus, tempProgressDialog);
                    }
                });

        //target
        reference.child(ConstantValue.FRIEND_LIST_CHILD).child(targetFriendInfo.getFriendUid()).child(senderFriendInfo.getFriendUid()).setValue(senderFriendInfo)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            AcceptFriendNotification acceptFriendNotification = new AcceptFriendNotification(senderFriendInfo);
                            reference.child(ConstantValue.NOTIFICATION_CHILD).child(targetFriendInfo.getFriendUid()).push().setValue(acceptFriendNotification)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            checkAddComplete(checkStatus, tempProgressDialog);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            checkAddComplete(checkStatus, tempProgressDialog);
                                        }
                                    });
                        } else {
                            checkAddComplete(checkStatus, tempProgressDialog);
                        }
                    }
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (reference != null) {
            for (ValueEventListener listener : listeners) {
                reference.removeEventListener(listener);
            }
            listeners.clear();
        }
    }

    private void checkAddComplete(int[] checkStatus, ProgressDialog tempProgressDialog) {
        if (checkStatus[0] >= 2) {
            tempProgressDialog.dismiss();
        } else {
            checkStatus[0]++;
        }
    }
}
