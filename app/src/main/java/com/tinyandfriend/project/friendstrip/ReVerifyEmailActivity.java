package com.tinyandfriend.project.friendstrip;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class ReVerifyEmailActivity extends AppCompatActivity {

    private  final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_verify_email);
    }

    public void onClickReVerify(View view){
        firebaseAuth.getCurrentUser().sendEmailVerification();
    }

    public void onClickSignOut(View view){
        firebaseAuth.signOut();
    }
}
