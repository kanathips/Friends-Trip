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
import com.google.firebase.auth.FirebaseUser;
import com.tinyandfriend.project.friendstrip.adapter.AuthAdapter;
import com.tinyandfriend.project.friendstrip.info.SignInInfo;

public class SignInActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private FirebaseAuth firebaseAuth;

    public void onCreate(Bundle SavedInstanceBundle) {
        super.onCreate(SavedInstanceBundle);
        setContentView(R.layout.activity_sign_in);

        setEmailEditText((EditText) findViewById(R.id.email));
        setPasswordEditText((EditText) findViewById(R.id.password));

        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void onClickLogin(View view) {
        if (!validateForm())
            return;

        try {
            SignInInfo signInInfo = new SignInInfo(getEmailText(), getPasswordText());
            AuthAdapter authAdapter = new AuthAdapter(firebaseAuth);
            authAdapter.signIn(signInInfo).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.i("SignInActivity", "SignIn:OnComplete: " + task.isSuccessful());
                    String toastText = "";
                    if (!task.isSuccessful() && task.getException() != null) {
                        //TODO Change The Toast to show user about sign in error
                        toastText = task.getException().getMessage();
                    } else if (!task.isSuccessful()) {
                        //TODO Add This line to show about Sign In Error
                        toastText = "Sign In Fail";
                    } else {
                        FirebaseUser user = task.getResult().getUser();
                        //TODO Add This line to show about Sign In Success
//                        if (!user.isEmailVerified()) {
//                            toastText = "Please verify your email";
//                        } else {
                        if (user.isEmailVerified()) {
                            toastText = "Sign In Success";
                            startActivity(new Intent(SignInActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                    Toast.makeText(SignInActivity.this, toastText, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("SignInInfo", e.getMessage());
        }
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = emailEditText.getText().toString();
        if (email.isEmpty() || !Validator.validateEmail(email)) {
            emailEditText.setError("จำเป็นต้องใส่.");
            valid = false;
        } else {
            emailEditText.setError(null);
        }

        String password = passwordEditText.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("จำเป็นต้องใส่");
            valid = false;
        } else {
            passwordEditText.setError(null);
        }
        return valid;
    }

    public void onClickSignUp(View view) {
        startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
        finish();
    }

    public void onClickForgetPassword(View view){
        startActivity(new Intent(SignInActivity.this, ForgetPasswordActivity.class));
        finish();
    }

    private String getPasswordText(){
        return getPasswordEditText().getText().toString().trim();
    }

    private String getEmailText(){
        return getEmailEditText().getText().toString().trim();
    }

    public EditText getPasswordEditText() {
        return passwordEditText;
    }

    public void setPasswordEditText(EditText passwordEditText) {
        this.passwordEditText = passwordEditText;
    }

    public EditText getEmailEditText() {

        return emailEditText;
    }

    public void setEmailEditText(EditText emailEditText) {
        this.emailEditText = emailEditText;
    }
}