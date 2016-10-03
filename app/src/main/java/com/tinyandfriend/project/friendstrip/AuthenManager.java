package com.tinyandfriend.project.friendstrip;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.Executor;

/**
 * Created by NewWy on 3/10/2559.
 */
public class AuthenManager {
    public static final String TAG = "AuthenManager";
    private FirebaseAuth mAuth;
    private static AuthenManager instance = new AuthenManager();
    private OnCompleteListener signInCompleteListener;
    private OnCompleteListener createAccountCompleteListener;

    private AuthenManager(){
        mAuth = FirebaseAuth.getInstance();
    }

    public void createAccount(CreateAccountInfo createAccountInfo) throws CreateAccountException{
        String email = createAccountInfo.getEmail();
        String password = createAccountInfo.getPassword();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(createAccountCompleteListener);
    }
    
    

    public void signIn(SignInInfo signInInfo){
        String email = signInInfo.getEmail();
        String password = signInInfo.getPassword();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(signInCompleteListener);
    }
    
    public void setSignInCompleteListener(OnCompleteListener signInCompleteListener){
        this.signInCompleteListener = signInCompleteListener;
    }

    public void setCreateAccountCompleteListener(OnCompleteListener createAccountCompleteListener){
        this.createAccountCompleteListener = createAccountCompleteListener;
    }

    private void signOut() {
        mAuth.signOut();
    }

    public static AuthenManager getInstance(){
        return instance;
    }


}
