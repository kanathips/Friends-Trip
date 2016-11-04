package com.tinyandfriend.project.friendstrip.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tinyandfriend.project.friendstrip.R;
import com.tinyandfriend.project.friendstrip.info.FriendListInfo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.tinyandfriend.project.friendstrip.info.FriendStatus.Approving;
import static com.tinyandfriend.project.friendstrip.info.FriendStatus.Pending;

public class FragmentAddFriend extends Fragment {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private Button addButton;
    private TextView targetNameTextView;
    private ProgressDialog progressDialog;
    private View rootView;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_add_friend, container, false);
        addButton = (Button) rootView.findViewById(R.id.add_friend_button);
        context = getContext();
        targetNameTextView = (TextView) rootView.findViewById(R.id.target_name);
        progressDialog = new ProgressDialog(context);
        Button searchButton = (Button) rootView.findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclickSearch(v);
            }
        });

        return rootView;
    }

    public void onclickSearch(View view) {
        String searchText = ((EditText) rootView.findViewById(R.id.search_text)).getText().toString();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        progressDialog.setMessage("กำลังค้นหา");
        progressDialog.show();

        if (searchText.equals(user.getDisplayName())) {
            return;
        }

        Query query = databaseReference.child("displayNameIndex").orderByChild("displayName").equalTo(searchText);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    final String targetUID;
                    String targetName;
                    DataSnapshot target;
                    Iterator<DataSnapshot> o = dataSnapshot.getChildren().iterator();

                    if (o.hasNext()) {
                        target = o.next();
                        targetUID = target.getKey();
                        targetName = target.child("displayName").getValue().toString();
                        targetNameTextView.setVisibility(View.VISIBLE);
                        targetNameTextView.setText(targetName);
                        addButton.setVisibility(View.VISIBLE);
                        addButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                addFriend(targetUID, user.getUid());
                            }
                        });
                    } else {
                        targetNameTextView.setText(null);
                        targetNameTextView.setVisibility(View.INVISIBLE);
                        addButton.setVisibility(View.INVISIBLE);
                        addButton.setOnClickListener(null);
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
                Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    /***
     * Add UID and status of sender to receiver Child in friendTerm Node in firebase DB
     * then Add UID and status of receiver to sender child in friendTerm Node in firebase
     *
     * @param targetUID the UID of target
     * @param senderUID the UID of sender
     */
    private void addFriend(final String targetUID, final String senderUID) {
        final ProgressDialog tempProgressDialog = new ProgressDialog(context);
        final int[] checkStatus = {1};

        tempProgressDialog.show();
        tempProgressDialog.setMessage("กำลังเพิ่มเพื่อน");

        databaseReference.child("users").child(senderUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String username = dataSnapshot.child("displayName").getValue(String.class);
                    String userPhotoUri = dataSnapshot.child("profilePicture").getValue(String.class);
                    FriendListInfo friendListInfo = new FriendListInfo(userPhotoUri, username, Pending);
                    databaseReference.child("friendTerm").child(targetUID).setValue(friendListInfo);
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
                }else{
                    checkStatus[0]++;
                }
            }
        });

        databaseReference.child("users").child(targetUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String username = dataSnapshot.child("displayName").getValue(String.class);
                    String userPhotoUri = dataSnapshot.child("profilePicture").getValue(String.class);
                    FriendListInfo friendListInfo = new FriendListInfo(userPhotoUri, username, Approving);
                    databaseReference.child("friendTerm").child(senderUID).setValue(friendListInfo);

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
                }else{
                    checkStatus[0]++;
                }
            }
        });

    }
}
