package com.tinyandfriend.project.friendstrip.listener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tinyandfriend.project.friendstrip.ReVerifyEmailActivity;
import com.tinyandfriend.project.friendstrip.SignInActivity;

/**
 * Created by NewWy on 12/10/2559.
 */

public class AuthStateChangeListener implements FirebaseAuth.AuthStateListener {

    private Context context;

    public AuthStateChangeListener(Context context){
        this.context = context;
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
//            Toast.makeText(context, "Not Login", Toast.LENGTH_SHORT).show();
            context.startActivity(new Intent(context, SignInActivity.class));
            ((Activity)context).finish();
        } else {
            if(!firebaseAuth.getCurrentUser().isEmailVerified()) {
                Toast.makeText(context, "Sign In Ok But Not Verify", Toast.LENGTH_SHORT).show();
                context.startActivity(new Intent(context, ReVerifyEmailActivity.class));
//                ((Activity)context).finish();
            }

        }
    }
}
