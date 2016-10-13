package com.tinyandfriend.project.friendstrip;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by NewWy on 6/10/2559.
 */
public class ForgetPasswordActivity extends AppCompatActivity{


    public void onCreate(Bundle savedInstanceBundle){
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.activity_forget_password);
    }

    public void onClickForgetPassword(View view){
        String email = ((EditText)findViewById(R.id.email)).getText().toString();
        //TODO Change the line below to show the user about process
        Toast.makeText(this, "email : " + email, Toast.LENGTH_SHORT).show();
        if(Validator.validateEmail(email)) {
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //TODO Change the line below to show the user about reset password success
                                Toast.makeText(ForgetPasswordActivity.this, "OK", Toast.LENGTH_SHORT).show();
                                Log.i("ForgetPassword", "Forget : Ok");
                            }else{
                                //TODO Change the line below to show the user about error
                                Toast.makeText(ForgetPasswordActivity.this, "Error : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                Log.i("ForgetPassword", "Forget : Fail");
                            }
                        }
                    });
        }else{
            //TODO Change the line below to show the user about email format error
            Toast.makeText(ForgetPasswordActivity.this, "Error", Toast.LENGTH_SHORT).show();
        }
    }
}
