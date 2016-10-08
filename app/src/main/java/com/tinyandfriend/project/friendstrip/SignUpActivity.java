package com.tinyandfriend.project.friendstrip;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

/**
 * Created by NewWy on 3/10/2559.
 */
public class SignUpActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText citizenIdEditText;
    private EditText rePasswordEditText;
    private EditText passwordEditText;
    private EditText displayNameEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setEmailEditText((EditText)findViewById(R.id.email));
        setRePasswordEditText((EditText)findViewById(R.id.repassword));
        setPasswordEditText((EditText)findViewById(R.id.password));
        setDisplayNameEditText((EditText)findViewById(R.id.displayname));
        setCitizenIdEditText((EditText)findViewById(R.id.citizen_id));

        getRePasswordEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b) {
                    String rePassword = getRePasswordEditText().getText().toString();
                    String password = getPasswordEditText().getText().toString();

                    String toastMessage;
                    if (rePassword.equals(password) && !rePassword.isEmpty()) {
                        toastMessage = "Password Match";
                    } else {
                        toastMessage = "Password Miss match";
                    }
                    //TODO Change this line to show the user about a confirm password
                    Toast.makeText(SignUpActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onSignUpClick(View view){
    //TODO Implement Validate From Function here
//        if(!validateFrom()){
//            return;
//        }

            final String email = getEmailEditText().getText().toString();
            final String password = getPasswordEditText().getText().toString();
            String rePassword = getRePasswordEditText().getText().toString();
            String displayName = getDisplayNameEditText().getText().toString();
            String citizenId = getCitizenIdEditText().getText().toString();


        final SignUpInfo signUpInfo = new SignUpInfo();
        try {

            signUpInfo.setEmail(email);
            signUpInfo.setPassword(password, rePassword);
            signUpInfo.setDisplayName(displayName);
            signUpInfo.setCitizenId(citizenId);
            final AuthenManager authenManager = AuthenManager.getInstance();
            authenManager.setSignUpCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task task) {
                    Log.i(AuthenManager.TAG, "SignUp:OnComplete: " + task.isSuccessful());
                    //TODO Change this line to show the user about a result of sign up

                    Exception exception = task.getException();

                    if(!task.isSuccessful()) {
                        //TODO Change this line to show user abount a Sign up Failure
                        Toast.makeText(SignUpActivity.this, "Sign Up Fail", Toast.LENGTH_SHORT).show();
                        Toast.makeText(SignUpActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }else{
                        authenManager.signIn(new SignInInfo(email, password));
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(signUpInfo.getDisplayName()).build();

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("SignUpActivity", "Email sent.");
                                        }
                                    }
                                });

                        user.updateProfile(profileUpdates);

                        //TODO Change this line to show user abount a Sign up Successful
                        Toast.makeText(SignUpActivity.this, "Sign Up Success", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                        finish();
                    }
                }
            });
            authenManager.signUp(signUpInfo);
        }catch (IllegalArgumentException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("SignUpInfo", e.getMessage());
        }
    }

    private EditText getEmailEditText() {
        return emailEditText;
    }

    private void setEmailEditText(EditText emailEditText) {
        this.emailEditText = emailEditText;
    }

    private EditText getCitizenIdEditText() {
        return citizenIdEditText;
    }

    private void setCitizenIdEditText(EditText citizenIdEditText) {
        this.citizenIdEditText = citizenIdEditText;
    }

    private EditText getRePasswordEditText() {
        return rePasswordEditText;
    }

    private void setRePasswordEditText(EditText rePasswordEditText) {
        this.rePasswordEditText = rePasswordEditText;
    }

    private EditText getPasswordEditText() {
        return passwordEditText;
    }

    private void setPasswordEditText(EditText passwordEditText) {
        this.passwordEditText = passwordEditText;
    }

    private EditText getDisplayNameEditText() {
        return displayNameEditText;
    }

    private void setDisplayNameEditText(EditText displayNameEditText) {
        this.displayNameEditText = displayNameEditText;
    }
}
