package com.tinyandfriend.project.friendstrip;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private String username;
    private AuthStateChangeListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        authStateListener = new AuthStateChangeListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    public void onSignOutClick(View view) {
        firebaseAuth.signOut();
        //TODO Change the line below to notify user about sign out
        Toast.makeText(this, "Signed out", Toast.LENGTH_SHORT).show();
    }

    public void onClickJoinTrip(View view){
        Intent intent = new Intent(this, AddFriendsActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClickCreateTrip(View view){
        Intent intent = new Intent(this, CreateTripActivity.class);
        startActivity(intent);
        finish();
    }

}
