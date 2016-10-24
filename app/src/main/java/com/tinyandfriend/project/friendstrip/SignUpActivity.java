package com.tinyandfriend.project.friendstrip;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.annotation.NonNull;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tinyandfriend.project.friendstrip.adapter.AuthAdapter;
import com.tinyandfriend.project.friendstrip.info.SignUpInfo;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by NewWy on 3/10/2559.
 */
public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";
    private FirebaseAuth firebaseAuth;
    private EditText emailEditText;
    private EditText citizenIdEditText;
    private EditText rePasswordEditText;
    private EditText passwordEditText;
    private EditText displayNameEditText;
    private EditText phoneNumberEditText;
    private EditText bDateEditText;
    private EditText fNameEditText;
    private EditText lNameEditText;

    private TextInputLayout emailTextLayout;
    private TextInputLayout citizenIdTextLayout;
    private TextInputLayout rePasswordTextLayout;
    private TextInputLayout passwordTextLayout;
    private TextInputLayout displayNameTextLayout;
    private TextInputLayout phoneNumberTextLayout;
    private TextInputLayout bDateTextLayout;
    private TextInputLayout fNameTextLayout;
    private TextInputLayout lNameTextLayout;

    private DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference();

    private EditText dateView;

    boolean valid = true;
    private int mYear, mMonth, mDay;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Use Setter
        setEmailEditText((EditText) findViewById(R.id.email));
        setRePasswordEditText((EditText) findViewById(R.id.repassword));
        setPasswordEditText((EditText) findViewById(R.id.password));
        setDisplayNameEditText((EditText) findViewById(R.id.displayname));
        setCitizenIdEditText((EditText) findViewById(R.id.citizen_id));
        setbDateEditText((EditText) findViewById(R.id.birth_date));
        setfNameEditText((EditText) findViewById(R.id.first_name));
        setlNameEditText((EditText) findViewById(R.id.last_name));

        setEmailTextLayout((TextInputLayout) findViewById(R.id.emailTextLayout));
        setfNameTextLayout((TextInputLayout) findViewById(R.id.fNameTextLayout));
        setlNameTextLayout((TextInputLayout) findViewById(R.id.lNameTextLayout));
        setDisplayNameTextLayout((TextInputLayout) findViewById(R.id.displayNameTextLayout));
        setCitizenIdTextLayout((TextInputLayout) findViewById(R.id.citizenIdTextLayout));
        setPasswordTextLayout((TextInputLayout) findViewById(R.id.passwordTextLayout));
        setRePasswordTextLayout((TextInputLayout) findViewById(R.id.rePasswordTextLayout));
        setPhoneNumberTextLayout((TextInputLayout) findViewById(R.id.phoneNumberTextLayout));

        setPhoneNumberEditText((EditText) findViewById(R.id.phonenumber));

        firebaseAuth = FirebaseAuth.getInstance();

        dateView = (EditText) findViewById(R.id.birth_date);


    }


    public void onClickDate(View view) {
//
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.MyDatePickerDialogTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        dateView.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        datePickerDialog.show();

    }


    public void onClickSignUp(View view) {
        //TODO Implement Validate From Function here

        if (!validateForm())
            return;

        SignUpInfo signUpInfo = new SignUpInfo();
        try {

            signUpInfo.setEmail(getEmailText());
            signUpInfo.setPassword(getPasswordText(), getRePasswordText());
            signUpInfo.setDisplayName(getDisplayNameText());
            signUpInfo.setCitizenId(getCitizenIdText());
            signUpInfo.setDateOfBirth(getBDateText());
            signUpInfo.setfName(getFNameText());
            signUpInfo.setlName(getLNameText());
            signUpInfo.setPhoneNumber(getPhoneNumberText());

            signUp(signUpInfo);


        } catch (IllegalArgumentException e) {
            Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("SignUpInfo", e.getMessage());
        }

    }

    private boolean validateForm() {
        if (!validateCheck(getfNameTextLayout(),getfNameEditText(),false,false,false)) {  return false;   }
        if (!validateCheck(getlNameTextLayout(),getlNameEditText(),false,false,false)) {  return false;   }
        if (!validateCheck(getEmailTextLayout(),getEmailEditText(),false,true,false)) {  return false;   }
        if (!validateCheck(getRePasswordTextLayout(),getRePasswordEditText(),true,false,false)) {  return false;   }
        if (!validateCheck(getPasswordTextLayout(),getPasswordEditText(),true,false,false)) {  return false;   }
        if (!validateCheck(getDisplayNameTextLayout(),getDisplayNameEditText(),false,false,false)) {  return false;   }
        if (!validateCheck(getPhoneNumberTextLayout(),getPhoneNumberEditText(),false,false,false)) {  return false;   }
        if (!validateCheck(getCitizenIdTextLayout(),getCitizenIdEditText(),false,false,true)) {  return false;   }

        return valid;

    }

    private boolean validateCheck(TextInputLayout textInputLayout,EditText editText,boolean isPassword,boolean isEmail,boolean isCitizenId) {
        if (editText.getText().toString().trim().isEmpty()) {
            textInputLayout.setError("จำเป็นต้องใส่");
            requestFocus(editText);
            valid = false;
            return false;
        } else if(!getPasswordText().equals(getRePasswordText())&&isPassword){
            textInputLayout.setError("รหัสผ่านไม่ตรงกัน");
            requestFocus(editText);
            valid = false;
            return false;
        }else if(!Validator.validateEmail(getEmailText())&&isEmail){
            textInputLayout.setError("รูปแบบอีเมลไม่ถูกต้อง");
            requestFocus(editText);
            valid = false;
            return false;
        }else if(!Validator.validateCitizenId(getCitizenIdText())&&isCitizenId){
            textInputLayout.setError("รหัสบัตรประจำตัวประชาชนไม่ถูกต้อง");
            requestFocus(editText);
            valid = false;
            return false;
        }else {
            textInputLayout.setErrorEnabled(false);
        }
        valid = true;
        return true;
    }

    public void onClickSignUp_Cancel(View view) {
//        startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
        finish();
    }

    public void signUp(final SignUpInfo signUpInfo) {
        final ProgressDialog progressDialog = ProgressDialog.show(SignUpActivity.this, "สมัครสมาชิก", "กำลังทำรายการโปรดรอ...");
        final boolean[] valid = {true};
        dbReference.child("citizenIdIndex").orderByChild("citizenId").equalTo(signUpInfo.getCitizenId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Toast.makeText(SignUpActivity.this, "เลขบัตรประจำตัวประชาชนนี้ ถูกใช้ไปแล้ว", Toast.LENGTH_SHORT).show();
                        } else {
//                            AuthAdapter authAdapter = new AuthAdapter(firebaseAuth);
//                            authAdapter.signUp(signUpInfo).addOnCompleteListener(
//                                    new OnCompleteListener<AuthResult>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<AuthResult> task) {
//                                            Log.i("SignUpActiviy", "SignUp:OnComplete: " + task.isSuccessful());
//                                            Exception exception = task.getException();
//
//                                            if (!task.isSuccessful() && exception != null) {
//                                                String errorText;
//                                                if (exception instanceof FirebaseAuthWeakPasswordException == true) {
//                                                    errorText = "รหัสผ่านต้องมีอย่างน้อย 6 ตัวอักษร";
//                                                }
//                                                Toast.makeText(SignUpActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
//                                            } else {
////                                                FirebaseUser user = task.getResult().getUser();
////                                                user.sendEmailVerification();
////                                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(signUpInfo.getDisplayName()).build();
////
////                                                user.updateProfile(profileUpdates);
////                                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
////                                                databaseReference.child("users").child(user.getUid()).setValue(signUpInfo);
////
////                                                Map<String, Object> citizenIdMap = new HashMap<>();
////                                                citizenIdMap.put("citizenId", signUpInfo.getCitizenId());
////                                                databaseReference.child("citizenIdIndex").child(user.getUid()).setValue(citizenIdMap);
////
////                                                Map<String, Object> displayNameMap = new HashMap<>();
////                                                databaseReference.child("displayNameIndex").child(user.getUid()).setValue(displayNameMap);
////
////                                                //TODO Change this line to show user abount a Sign up Successful
////                                                Toast.makeText(SignUpActivity.this, "สมัครสมาชิกถูกต้อง", Toast.LENGTH_SHORT).show();
////                                                finish();
//                                            }
//                                        }
//                                    });
//                        }
                            dbReference.child("displayNameIndex").orderByChild("displayName").equalTo(signUpInfo.getDisplayName())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists())
                                                Toast.makeText(SignUpActivity.this, "ชื่อที่ใช้ในระบบนี้ ถูกใช้ไปแล้ว", Toast.LENGTH_SHORT).show();
                                            else {
                                                AuthAdapter authAdapter = new AuthAdapter(firebaseAuth);
                                                authAdapter.signUp(signUpInfo).addOnCompleteListener(
                                                        new OnCompleteListener<AuthResult>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                                Log.i("SignUpActiviy", "SignUp:OnComplete: " + task.isSuccessful());
                                                                Exception exception = task.getException();

                                                                if (!task.isSuccessful() && exception != null) {
                                                                    String errorText;
                                                                    if (exception instanceof FirebaseAuthWeakPasswordException == true) {
                                                                        errorText = "รหัสผ่านต้องมีอย่างน้อย 6 ตัวอักษร";
                                                                    } else {
                                                                        errorText = exception.getMessage();
                                                                    }
                                                                    Toast.makeText(SignUpActivity.this, errorText, Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    FirebaseUser user = task.getResult().getUser();
                                                                    user.sendEmailVerification();
                                                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(signUpInfo.getDisplayName()).build();

                                                                    user.updateProfile(profileUpdates);
                                                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                                                    databaseReference.child("users").child(user.getUid()).setValue(signUpInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()) {
                                                                                Log.i(TAG, "Firebase : Insert Sign up Info Complete");
                                                                            } else {
                                                                                Log.e(TAG, "Firebase : Insert Sign up Info Not Complete");
                                                                            }
                                                                        }
                                                                    });

                                                                    Map<String, Object> citizenIdMap = new HashMap<>();
                                                                    citizenIdMap.put("citizenId", signUpInfo.getCitizenId());
                                                                    databaseReference.child("citizenIdIndex").child(user.getUid()).setValue(citizenIdMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()) {
                                                                                Log.i(TAG, "Firebase : Insert CitizenId Index Complete");
                                                                            } else {
                                                                                Log.e(TAG, "Firebase : Insert CitizenId Index Not Complete");
                                                                            }
                                                                        }
                                                                    });

                                                                    Map<String, Object> displayNameMap = new HashMap<>();
                                                                    displayNameMap.put("displayName", signUpInfo.getDisplayName());
                                                                    databaseReference.child("displayNameIndex").child(user.getUid()).setValue(displayNameMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()) {
                                                                                Log.i(TAG, "Firebase : Insert Display name Index Complete");
                                                                            } else {
                                                                                Log.e(TAG, "Firebase : Insert Display name Index Not Complete");
                                                                            }
                                                                        }
                                                                    });

                                                                    //TODO Change this line to show user abount a Sign up Successful
                                                                    Toast.makeText(SignUpActivity.this, "สมัครสมาชิกถูกต้อง", Toast.LENGTH_SHORT).show();
                                                                    finish();
                                                                }
                                                            }
                                                        });
                                            }
                                            progressDialog.dismiss();
                                        }

                                        //
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            Toast.makeText(SignUpActivity.this, "ชื่อที่ใช้ในระบบ " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(SignUpActivity.this, "บัตรปชช " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }



//    private boolean validateFrom() {
//
//        boolean valid = true;
//        String errorMessage;
//
//        String fName = getFNameText();
//
//        if (fName.isEmpty()) {
//            valid = false;
//            errorMessage = "จำเป็นต้องใส่";
//        } else {
//            errorMessage = null;
//        }
//
//        getfNameEditText().setError(errorMessage);
//
//        String phoneNumber = getPhoneNumberText();
//        if (phoneNumber.isEmpty()) {
//            valid = false;
//            errorMessage = "จำเป็นต้องใส่เบอร์โทรศัพท์";
//        } else {
//            errorMessage = null;
//        }
//        getPhoneNumberEditText().setError(errorMessage);
//
//        String email = getEmailText();
//
//        if (email.isEmpty()) {
//            valid = false;
//            errorMessage = "จำเป็นต้องใส่อีเมลล์";
//        } else if (!Validator.validateEmail(email)) {
//            valid = false;
//            errorMessage = "รูปแบบอีเมลล์ไม่ถูกต้อง";
//        } else {
//            errorMessage = null;
//        }
//        getEmailEditText().setError(errorMessage);
//
//        String password = getPasswordText();
//        if (password.isEmpty()) {
//            valid = false;
//            errorMessage = "จำเป็นต้องใส่รหัสผ่าน";
//        } else {
//            errorMessage = null;
//        }
//        getPasswordEditText().setError(errorMessage);
//
//        String rePassword = getRePasswordText();
//        if (rePassword.isEmpty()) {
//            valid = false;
//            errorMessage = "จำเป็นต้องยืนยันรหัสผ่าน";
//        } else if (!rePassword.equals(password)) {
//            valid = false;
//            errorMessage = "รหัสผ่านไม่ตรงกัน";
//        } else {
//            errorMessage = null;
//        }
//        getRePasswordEditText().setError(errorMessage);
//
//        String displayName = getDisplayNameText();
//        if (displayName.isEmpty()) {
//            valid = false;
//            errorMessage = "จำเป็นต่องใส่ผู้ใช้งาน";
//        } else {
//            errorMessage = null;
//        }
//        getDisplayNameEditText().setError(errorMessage);
//
//        String citizenId = getCitizenIdText();
//        if (citizenId.isEmpty()) {
//            valid = false;
//            errorMessage = "จำเป็นต่องใสรหัสประจำตัวประชาชน";
//        } else if (!Validator.validateCitizenId(citizenId)) {
//            valid = false;
//            errorMessage = "รหัสประจำตัวประชาชนไม่ถูกต้อง";
//        } else {
//            errorMessage = null;
//        }
//        getCitizenIdEditText().setError(errorMessage);
//        return valid;
//    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    // Setter And Getter

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


    private String getEmailText() {
        return getEmailEditText().getText().toString().trim();
    }


    private String getPasswordText() {
        return getPasswordEditText().getText().toString().trim();
    }


    private String getRePasswordText() {
        return getRePasswordEditText().getText().toString().trim();
    }


    private String getDisplayNameText() {
        return getDisplayNameEditText().getText().toString().trim();
    }


    private String getFNameText() {
        return getfNameEditText().getText().toString();
    }


    private String getBDateText() {
        return getbDateEditText().getText().toString();
    }


    private String getPhoneNumberText() {
        return getPhoneNumberEditText().getText().toString();
    }


    private String getLNameText() {
        return getlNameEditText().getText().toString();
    }

    public EditText getbDateEditText() {
        return bDateEditText;
    }

    public void setbDateEditText(EditText bDateEditText) {
        this.bDateEditText = bDateEditText;
    }

    public EditText getPhoneNumberEditText() {
        return phoneNumberEditText;
    }

    public void setPhoneNumberEditText(EditText phoneNumberEditText) {
        this.phoneNumberEditText = phoneNumberEditText;
    }

    public EditText getlNameEditText() {
        return lNameEditText;
    }

    public void setlNameEditText(EditText lNameEditText) {
        this.lNameEditText = lNameEditText;
    }

    public EditText getfNameEditText() {
        return fNameEditText;
    }

    public void setfNameEditText(EditText fNameEditText) {
        this.fNameEditText = fNameEditText;
    }

    private String getCitizenIdText() {
        return getCitizenIdEditText().getText().toString().trim();
    }

    public TextInputLayout getEmailTextLayout() {
        return emailTextLayout;
    }

    public void setEmailTextLayout(TextInputLayout emailTextLayout) {
        this.emailTextLayout = emailTextLayout;
    }

    public TextInputLayout getCitizenIdTextLayout() {
        return citizenIdTextLayout;
    }

    public void setCitizenIdTextLayout(TextInputLayout citizenIdTextLayout) {
        this.citizenIdTextLayout = citizenIdTextLayout;
    }

    public TextInputLayout getRePasswordTextLayout() {
        return rePasswordTextLayout;
    }

    public void setRePasswordTextLayout(TextInputLayout rePasswordTextLayout) {
        this.rePasswordTextLayout = rePasswordTextLayout;
    }

    public TextInputLayout getPasswordTextLayout() {
        return passwordTextLayout;
    }

    public void setPasswordTextLayout(TextInputLayout passwordTextLayout) {
        this.passwordTextLayout = passwordTextLayout;
    }

    public TextInputLayout getDisplayNameTextLayout() {
        return displayNameTextLayout;
    }

    public void setDisplayNameTextLayout(TextInputLayout displayNameTextLayout) {
        this.displayNameTextLayout = displayNameTextLayout;
    }

    public TextInputLayout getPhoneNumberTextLayout() {
        return phoneNumberTextLayout;
    }

    public void setPhoneNumberTextLayout(TextInputLayout phoneNumberTextLayout) {
        this.phoneNumberTextLayout = phoneNumberTextLayout;
    }

    public TextInputLayout getbDateTextLayout() {
        return bDateTextLayout;
    }

    public void setbDateTextLayout(TextInputLayout bDateTextLayout) {
        this.bDateTextLayout = bDateTextLayout;
    }

    public TextInputLayout getfNameTextLayout() {
        return fNameTextLayout;
    }

    public void setfNameTextLayout(TextInputLayout fNameTextLayout) {
        this.fNameTextLayout = fNameTextLayout;
    }

    public TextInputLayout getlNameTextLayout() {
        return lNameTextLayout;
    }

    public void setlNameTextLayout(TextInputLayout lNameTextLayout) {
        this.lNameTextLayout = lNameTextLayout;
    }
}
