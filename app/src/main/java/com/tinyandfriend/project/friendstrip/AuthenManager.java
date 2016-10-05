package com.tinyandfriend.project.friendstrip;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by NewWy on 3/10/2559.
 */
public class AuthenManager {
    public static final String TAG = "AuthenManager";
    private final FirebaseAuth mAuth;
    private static final AuthenManager instance = new AuthenManager();
    private OnCompleteListener signInCompleteListener;
    private OnCompleteListener signUpCompleteListener;

    private AuthenManager(){
        mAuth = FirebaseAuth.getInstance();
    }

    public void signUp(SignUpInfo signUpInfo) throws NullPointerException {
        String email = signUpInfo.getEmail();
        String password = signUpInfo.getPassword();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(signUpCompleteListener);
    }

    public void signIn(SignInInfo signInInfo) throws NullPointerException{
        String email = signInInfo.getEmail();
        String password = signInInfo.getPassword();
        mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(signInCompleteListener);
    }

    public void setSignInCompleteListener(OnCompleteListener signInCompleteListener){
        this.signInCompleteListener = signInCompleteListener;
    }

    public void setSignUpCompleteListener(OnCompleteListener signUpCompleteListener){
        this.signUpCompleteListener = signUpCompleteListener;
    }

    public void signOut() {
        mAuth.signOut();
    }

    public static AuthenManager getInstance() {
        return instance;
    }
}
