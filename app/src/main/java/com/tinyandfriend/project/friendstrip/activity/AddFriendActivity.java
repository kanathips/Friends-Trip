package com.tinyandfriend.project.friendstrip.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tinyandfriend.project.friendstrip.AcceptFriendNotification;
import com.tinyandfriend.project.friendstrip.ConstantValue;
import com.tinyandfriend.project.friendstrip.R;
import com.tinyandfriend.project.friendstrip.info.FriendInfo;
import com.tinyandfriend.project.friendstrip.info.UserInfo;

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

                    DataSnapshot target;
                    Iterator<DataSnapshot> o = dataSnapshot.getChildren().iterator();

                    if (o.hasNext()) {
                        target = o.next();
                        if(!target.child("displayName").exists()) {
                            targetNameTextView.setText("ไม่พบผู้ใช้งาน");
                            targetNameTextView.setVisibility(View.VISIBLE);
                            addButton.setVisibility(View.INVISIBLE);
                            addButton.setOnClickListener(null);
                            progressDialog.dismiss();
                            return;
                        }
                        targetUid = target.getKey();

                        String targetName = target.child("displayName").getValue().toString();
                        targetNameTextView.setVisibility(View.VISIBLE);
                        targetNameTextView.setText(targetName);
                        friendPhoto.setVisibility(View.VISIBLE);
                        if(target.child("profilePhoto").exists()) {
                            String profilePhoto = target.child("profilePhoto").getValue().toString();
                                Glide.with(AddFriendActivity.this).load(profilePhoto).centerCrop().into(friendPhoto);
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
                                            addFriend(targetUid, userUid);
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

        Query query = reference.child(ConstantValue.DISPLAY_NAME_INDEX_CHILD).orderByChild("displayName").equalTo(searchText);
        query.addListenerForSingleValueEvent(listener);
        listeners.add(listener);

    }

    /***
     * Add UID and status of sender to receiver Child in friendTerm Node in firebase DB
     * then Add UID and status of receiver to sender child in friendTerm Node in firebase
     *
     * @param targetUID the UID of target
     * @param senderUID the UID of sender
     */
    private void addFriend(final String targetUID, final String senderUID) {
        final ProgressDialog tempProgressDialog = new ProgressDialog(this);
        final int[] checkStatus = {1};

        tempProgressDialog.show();
        tempProgressDialog.setMessage("กำลังเพิ่มเพื่อน");

        ValueEventListener senderListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String username = dataSnapshot.child("displayName").getValue(String.class);
                    String userPhotoUri = dataSnapshot.child("profilePicture").getValue(String.class);
                    FriendInfo friendInfo = new FriendInfo(userPhotoUri, username, Approving);

                    reference.child(ConstantValue.FRIEND_LIST_CHILD).child(targetUID).child(senderUID).setValue(friendInfo);
                    AcceptFriendNotification acceptFriendNotification = new AcceptFriendNotification(friendInfo);
                    reference.child(ConstantValue.NOTIFICATION_CHILD).child(targetUID).push().setValue(acceptFriendNotification);

                    if (checkStatus[0] >= 2) {
                        tempProgressDialog.dismiss();
                    } else {
                        checkStatus[0]++;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (checkStatus[0] >= 2) {
                    tempProgressDialog.dismiss();
                } else {
                    checkStatus[0]++;
                }
            }
        };

        ValueEventListener targetListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String username = dataSnapshot.child("displayName").getValue(String.class);
                    String userPhotoUri = dataSnapshot.child("profilePicture").getValue(String.class);
                    FriendInfo friendInfo = new FriendInfo(userPhotoUri, username, Pending);
                    reference.child(ConstantValue.FRIEND_LIST_CHILD).child(senderUID).child(targetUID).setValue(friendInfo);

                    if (checkStatus[0] >= 2) {
                        tempProgressDialog.dismiss();
                    } else {
                        checkStatus[0]++;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (checkStatus[0] >= 2) {
                    tempProgressDialog.dismiss();
                } else {
                    checkStatus[0]++;
                }
            }
        };

        listeners.add(targetListener);
        listeners.add(senderListener);

        reference.child(ConstantValue.USERS_CHILD).child(senderUID).addListenerForSingleValueEvent(senderListener);
        reference.child(ConstantValue.USERS_CHILD).child(targetUID).addListenerForSingleValueEvent(targetListener);

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
}
