package com.tinyandfriend.project.friendstrip;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private String username;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser = firebaseAuth.getCurrentUser();
                String toastText;
                if (firebaseUser == null) {
                    startActivity(new Intent(MainActivity.this, SignInActivity.class));
                    finish();
                } else {
                    if(!firebaseAuth.getCurrentUser().isEmailVerified()) {
                        toastText = "Please verify your email";
                    }else {
                        if ((username = firebaseUser.getDisplayName()) == null)
                            username = firebaseUser.getEmail();
                        toastText = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid()).toString();
                    }
                    Toast.makeText(MainActivity.this, toastText, Toast.LENGTH_SHORT).show();
                }
            }
        };
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

    public void onDestroy() {
        super.onDestroy();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    public void onSignOutClick(View view) {
        firebaseAuth.signOut();
        //TODO Change the line below to notify user about sign out
        Toast.makeText(this, "Signed out", Toast.LENGTH_SHORT).show();
    }

}
