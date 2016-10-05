package com.tinyandfriend.project.friendstrip;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity{

    private EditText emailEditText;
    private EditText passwordEditText;
    private FirebaseAuth firebaseAuth;

    public void onCreate(Bundle SavedInstanceBundle){
        super.onCreate(SavedInstanceBundle);
        setContentView(R.layout.activity_sign_in);

        emailEditText = (EditText)findViewById(R.id.email);
        passwordEditText = (EditText)findViewById(R.id.password);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void onClickLogin(View view){
        if(!validateForm())
            return;

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        final AuthenManager authenManager = AuthenManager.getInstance();

        try {
            SignInInfo signInInfo = new SignInInfo(email, password);
            authenManager.setSignInCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task task) {
                    Log.i(AuthenManager.TAG, "SignIn:OnComplete: " + task.isSuccessful());
                    String toastText;
                    if(!task.isSuccessful() && task.getException() != null){
                        //TODO Change The Toast to show user about sign in error
                        toastText = task.getException().getMessage();
                    }else if(!task.isSuccessful()){
                        //TODO Add This line to show about Sign In Error
                        toastText = "Sign In Fail";
                    }else {
                        //TODO Add This line to show about Sign In Success
                        if(!firebaseAuth.getCurrentUser().isEmailVerified()){
                            toastText = "Please verify your email";
                        }else {
                            toastText = "Sign In Success";
                            startActivity(new Intent(SignInActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                    Toast.makeText(SignInActivity.this, toastText, Toast.LENGTH_SHORT).show();
                }
            });
            authenManager.signIn(signInInfo);
        }catch (IllegalArgumentException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("SignInInfo", e.getMessage());
        }
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = emailEditText.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Required.");
            valid = false;
        } else {
            emailEditText.setError(null);
        }

        String password = passwordEditText.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Required.");
            valid = false;
        } else {
            passwordEditText.setError(null);
        }
        return valid;
    }

    public void onSignUpClick(View view){
        startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
        finish();
    }
}