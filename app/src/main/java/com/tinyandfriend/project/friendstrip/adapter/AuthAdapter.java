package com.tinyandfriend.project.friendstrip.adapter;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.tinyandfriend.project.friendstrip.info.SignInInfo;
import com.tinyandfriend.project.friendstrip.info.SignUpInfo;

/**
 * Created by NewWy on 3/10/2559.
 */
public class AuthAdapter {
    public static final String TAG = "AuthAdapter";
    private final FirebaseAuth mAuth;
    private OnCompleteListener signInCompleteListener;
    private OnCompleteListener createAccountCompleteListener;
    private OnCompleteListener signUpCompleteListener;

    public AuthAdapter(FirebaseAuth firebaseAuth) {
        mAuth = firebaseAuth;
    }

    public Task<AuthResult> signUp(SignUpInfo userInfo) throws NullPointerException {
        String email = userInfo.getEmail();
        String password = userInfo.getPassword();
        return mAuth.createUserWithEmailAndPassword(email, password);
    }


    public Task<AuthResult> signIn(SignInInfo signInInfo) throws NullPointerException {
        String email = signInInfo.getEmail();
        String password = signInInfo.getPassword();
        return mAuth.signInWithEmailAndPassword(email, password);
    }
}