package com.tinyandfriend.project.friendstrip;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity
{
    public void onCreate(Bundle SavedInstanceBundle){
        super.onCreate(SavedInstanceBundle);
        setContentView(R.layout.activity_sign_in);
    }

    public void onClickLogin(View view){
        String email = ((EditText)findViewById(R.id.email)).getText().toString();
        String password = ((EditText)findViewById(R.id.password)).getText().toString();
        AuthenManager authenManager = AuthenManager.getInstance();
        try {
            SignInInfo signInInfo = new SignInInfo(email, password);
            authenManager.setSignInCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task task) {
                    Log.i(AuthenManager.TAG, "SignIn:OnComplete: " + task.isSuccessful());
                    Toast.makeText(SignInActivity.this, "Sign In " + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                }
            });
            authenManager.signIn(signInInfo);
        }catch (IllegalArgumentException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("SignInInfo", e.getMessage());
        }
    }

}