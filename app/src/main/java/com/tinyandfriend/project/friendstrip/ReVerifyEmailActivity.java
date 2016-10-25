package com.tinyandfriend.project.friendstrip;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ReVerifyEmailActivity extends AppCompatActivity {

    private  final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_verify_email);
    }

    public void onClickReVerify(View view){
        firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(ReVerifyEmailActivity.this, "Send " + task.isSuccessful(), Toast.LENGTH_SHORT).show();
            }
        });
        startActivity(new Intent(this, SignInActivity.class));
        finish();
    }

    public void onClickSignOut(View view){
        firebaseAuth.signOut();
        startActivity(new Intent(this, SignInActivity.class));
        finish();
    }
}
