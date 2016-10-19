package com.tinyandfriend.project.friendstrip;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.tinyandfriend.project.friendstrip.info.FriendStatus.Approving;
import static com.tinyandfriend.project.friendstrip.info.FriendStatus.Pending;

/**
 * Created by NewWy on 12/10/2559.
 */

public class AddFriendsActivity extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private Button addButton;
    private TextView targetNameTextView;

    @Override
    protected void onCreate(@org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        addButton = (Button)findViewById(R.id.add_friend_button);
        targetNameTextView = (TextView)findViewById(R.id.target_name);
        progressDialog = new ProgressDialog(this);
    }

    private ProgressDialog progressDialog;

    public void onclickSearch(View view) {
        String searchText = ((EditText) findViewById(R.id.search_text)).getText().toString();
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

                    if(o.hasNext()){
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
                    }else{
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
                Toast.makeText(AddFriendsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    /***
     *  Add UID and status of sender to receiver Child in friendTerm Node in firebase DB
     *  then Add UID and status of receiver to sender child in friendTerm Node in firebase
     *
     *  @param targetUID the UID of target
     *  @param senderUID the UID of sender
     *
     */
    private void addFriend(String targetUID, String senderUID){
        ProgressDialog tempProgressDialog = new ProgressDialog(this);

        tempProgressDialog.show();
        tempProgressDialog.setMessage("กำลังเพิ่มเพื่อน");
        Map<String, Object> sendMap = new HashMap<>();
        sendMap.put(targetUID, Pending);
        databaseReference.child("friendTerm").child(senderUID).updateChildren(sendMap);

        Map<String, Object> receiveMap = new HashMap<>();
        receiveMap.put(senderUID, Approving);
        databaseReference.child("friendTerm").child(targetUID).updateChildren(receiveMap);
        tempProgressDialog.dismiss();
    }
}
