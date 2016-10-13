package com.tinyandfriend.project.friendstrip;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by NewWy on 12/10/2559.
 */

public class AddFriendsActivity extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
    }

    public void onclickSearch(View view) {
        String searchText = ((EditText) findViewById(R.id.search_text)).getText().toString();
        Query query = databaseReference.child("users").orderByChild("email").equalTo(searchText);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot info = dataSnapshot;
                if(info.getValue() != null) {
                    Toast.makeText(AddFriendsActivity.this, info.getValue().toString(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Toast.makeText(AddFriendsActivity.this, "OK", Toast.LENGTH_SHORT).show();
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
//                    Toast.makeText(AddFriendsActivity.this, postSnapshot.getChildren().toString(), Toast.LENGTH_SHORT).show();
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }
}
