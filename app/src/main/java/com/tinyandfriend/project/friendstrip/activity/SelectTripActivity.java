package com.tinyandfriend.project.friendstrip.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tinyandfriend.project.friendstrip.R;

import java.util.HashMap;
import java.util.Map;

public class SelectTripActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_trip);
    }

    public void onClickSelectTrip(View view){
        TextView textView = (TextView)findViewById(R.id.trip_id);

        final String tripId = textView.getText().toString();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference reference = database.getReference();
        reference.child("tripRoom").child(tripId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String userUid = user.getUid();

                    updateTripRoom(reference, tripId, userUid);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateTripRoom(final DatabaseReference reference, final String tripId, final String userUid){
        final DatabaseReference tripReference = reference.child("tripRoom").child(tripId);
        tripReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                     if(dataSnapshot.exists()){
                         Long maxMember = (Long) dataSnapshot.child("maxMember").getValue();
                         if(dataSnapshot.child("members").getChildrenCount() < maxMember){
                             Map<String, Object> map = new HashMap<>();
                             map.put(userUid, true);
                             tripReference.child("members").updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                 @Override
                                 public void onSuccess(Void aVoid) {
                                     updateUserProfile(reference, tripId, userUid);
                                 }
                             }).addOnFailureListener(new OnFailureListener() {
                                 @Override
                                 public void onFailure(@NonNull Exception e) {
                                     Toast.makeText(SelectTripActivity.this, "ไม่สามารถเข้าร่วมได้ กรุณาลองใหม่อีกครั้ง (1)", Toast.LENGTH_SHORT).show();
                                 }
                             });
                         }else{
                             Toast.makeText(SelectTripActivity.this, "ทริปเต็มแล้ว", Toast.LENGTH_SHORT).show();
                         }

                     }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateUserProfile(DatabaseReference reference, String tripId, String userUid){
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("tripId", tripId);
        reference.child("users").child(userUid).updateChildren(updateMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(SelectTripActivity.this, "Join OK", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SelectTripActivity.this, "ไม่สามารถเข้าร่วมได้ กรุณาลองใหม่อีกครั้ง (2)", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
