package com.tinyandfriend.project.friendstrip;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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
import com.tinyandfriend.project.friendstrip.adapter.AuthAdapter;
import com.tinyandfriend.project.friendstrip.info.SignInInfo;

public class SignInActivity extends AppCompatActivity {

    private static final String TAG = "SignInActivity";
    private EditText emailEditText;
    private EditText passwordEditText;
    private FirebaseAuth firebaseAuth;

    public void onCreate(Bundle SavedInstanceBundle) {
        super.onCreate(SavedInstanceBundle);
        setContentView(R.layout.activity_sign_in);

        emailEditText = (EditText) findViewById(R.id.email);
        passwordEditText = (EditText) findViewById(R.id.password);

        firebaseAuth = FirebaseAuth.getInstance();


    }

    public void onClickLogin(View view) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        if (!validateForm())
            return;

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();


        try {
            SignInInfo signInInfo = new SignInInfo(email, password);
            AuthAdapter authAdapter = new AuthAdapter(firebaseAuth);
            authAdapter.signIn(signInInfo).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.i(TAG, "SignIn:OnComplete: " + task.isSuccessful());
                    if (!task.isSuccessful() && task.getException() != null) {
                        //TODO Change The Toast to show user about sign in error
                        Toast.makeText(SignInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    } else if (!task.isSuccessful()) {
                        //TODO Change The Toast to show user about sign in error
                        Toast.makeText(SignInActivity.this, "Sign In Fail", Toast.LENGTH_SHORT).show();
                    }else if(!task.getResult().getUser().isEmailVerified()){
                        Log.v(TAG, "SignIn:OnComplete: Account was not verify");
                        new AlertDialog.Builder(SignInActivity.this)
                                .setTitle("เข้าสู่ระบบไม่สำเร็จ")
                                .setMessage("ท่านยังไม่ได้ทำการยืนยันตัวตนผ่านทางอีเมล...")
                                .setPositiveButton("ส่งอีกครัง", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // continue with delete
                                        firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(SignInActivity.this, "Send " + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                })
                                .setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    } else if(task.isSuccessful()) {
                        Log.v(TAG, "SignIn:OnComplete: Sign In Pass");
                        progressDialog.show(SignInActivity.this, "เข้าสู่ระบบ", "กำลังทำการเข้าสู่ระบบ...");
                        startActivity(new Intent(SignInActivity.this, MainActivity.class));
                        finish();
                    }
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

    public void onSignUpClick(View view) {
        startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
//        finish();
    }

    public void onClickResetPassword(View view) {
        Intent intent = new Intent(this, ForgetPasswordActivity.class);
        startActivity(intent);
    }

}