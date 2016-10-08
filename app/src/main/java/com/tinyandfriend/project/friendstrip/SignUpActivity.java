package com.tinyandfriend.project.friendstrip;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by NewWy on 3/10/2559.
 */
public class SignUpActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText citizenIdEditText;
    private EditText rePasswordEditText;
    private EditText passwordEditText;
    private EditText displayNameEditText;
    private FirebaseAuth firebaseAuth;
    private EditText phoneNumberEditText;
    private EditText bDateEditText;
    private EditText fNameEditText;
    private EditText lNameEditText;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setEmailEditText((EditText) findViewById(R.id.email));

        setRePasswordEditText((EditText) findViewById(R.id.repassword));

        setPasswordEditText((EditText) findViewById(R.id.password));

        setDisplayNameEditText((EditText) findViewById(R.id.displayname));

        setCitizenIdEditText((EditText) findViewById(R.id.citizen_id));

        setbDateEditText((EditText)findViewById(R.id.birth_date));

        setfNameEditText((EditText)findViewById(R.id.first_name));

        setlNameEditText((EditText)findViewById(R.id.last_name));

        setPhoneNumberEditText((EditText)findViewById(R.id.phonenumber));

        firebaseAuth = FirebaseAuth.getInstance();

        getRePasswordEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
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

        final EditText bDateEditText = (EditText) findViewById(R.id.birth_date);
        bDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePicker = new DatePickerDialog(SignUpActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String date = String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear)
                                + "-" + String.valueOf(year);
                        bDateEditText.setText(date);
                    }
                }, yy, mm, dd);
                datePicker.show();
            }

        });

    }

    public void onClickSignUp(View view) {
        //TODO Implement Validate From Function here
        if (!validateFrom()) {
            return;
        }


        final SignUpInfo signUpInfo = new SignUpInfo();
        try {
            signUpInfo.setEmail(getEmailText());
            signUpInfo.setPassword(getPasswordText(), getRePasswordText());
            signUpInfo.setDisplayName(getDisplayNameText());
            signUpInfo.setCitizenId(getCitizenIdText());
            signUpInfo.setDateOfBirth(getBDateText());
            signUpInfo.setfName(getFNameText());
            signUpInfo.setlName(getLNameText());
            signUpInfo.setPhoneNumber(getPhoneNumberText());

            AuthAdapter authAdapter = new AuthAdapter(firebaseAuth);
            authAdapter.signUp(signUpInfo).addOnCompleteListener(
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.i("SignUpActiviy", "SignUp:OnComplete: " + task.isSuccessful());
                            //TODO Change this line to show the user about a result of sign up

                            Exception exception = task.getException();

                            if (!task.isSuccessful()) {
                                //TODO Change this line to show user abount a Sign up Failure
                                Toast.makeText(SignUpActivity.this, "Sign Up Fail", Toast.LENGTH_SHORT).show();
                                if (exception != null)
                                    Toast.makeText(SignUpActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                FirebaseUser user = task.getResult().getUser();
                                user.sendEmailVerification();
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(signUpInfo.getDisplayName()).build();

                                user.updateProfile(profileUpdates);
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                databaseReference.child("users").child(user.getUid()).setValue(signUpInfo);

                                //TODO Change this line to show user abount a Sign up Successful
                                Toast.makeText(SignUpActivity.this, "Sign Up Success", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                                finish();
                            }
                        }
                    });
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("SignUpInfo", e.getMessage());
        }
    }

    private boolean validateFrom() {


        boolean valid = true;
        String errorMessage;

        String fName = getFNameText();

        if(fName.isEmpty()){
            valid = false;
            errorMessage = "จำเป็นต้องใส่";
        }else{
            errorMessage = null;
        }

        getfNameEditText().setError(errorMessage);

        String lName = getLNameText();
        if(lName.isEmpty()){
            valid = false;
            errorMessage = "จำเป็นต้องใส่";
        }else{
            errorMessage = null;
        }
        getlNameEditText().setError(errorMessage);

        String bDate = getBDateText();
        if(bDate.isEmpty()){
            valid = false;
            errorMessage = "จำเป็นต้องใส่วันเกิด";
        }else{
            errorMessage = null;
        }
        getbDateEditText().setError(errorMessage);

        String phoneNumber = getPhoneNumberText();
        if(phoneNumber.isEmpty()){
            valid = false;
            errorMessage = "จำเป็นต้องใส่เบอร์โทรศัพท์";
        }else{
            errorMessage = null;
        }
        getPhoneNumberEditText().setError(errorMessage);

            String email = getEmailText();

        if (email.isEmpty()) {
            valid = false;
            errorMessage = "จำเป็นต้องใส่อีเมลล์";
        } else if (!Validator.validateEmail(email)) {
            valid = false;
            errorMessage = "รูปแบบอีเมลล์ไม่ถูกต้อง";
        } else {
            errorMessage = null;
        }
        getEmailEditText().setError(errorMessage);

        String password = getPasswordText();
        if (password.isEmpty()) {
            valid = false;
            errorMessage = "จำเป็นต้องใส่รหัสผ่าน";
        } else {
            errorMessage = null;
        }
        getPasswordEditText().setError(errorMessage);

        String rePassword = getRePasswordText();
        if (rePassword.isEmpty()) {
            valid = false;
            errorMessage = "จำเป็นต้องยืนยันรหัสผ่าน";
        } else if (!rePassword.equals(password)) {
            valid = false;
            errorMessage = "รหัสผ่านไม่ตรงกัน";
        } else {
            errorMessage = null;
        }
        getRePasswordEditText().setError(errorMessage);

        String displayName = getDisplayNameText();
        if (displayName.isEmpty()) {
            valid = false;
            errorMessage = "จำเป็นต่องใส่ผู้ใช้งาน";
        } else {
            errorMessage = null;
        }
        getDisplayNameEditText().setError(errorMessage);

        String citizenId = getCitizenIdText();
        if (citizenId.isEmpty()) {
            valid = false;
            errorMessage = "จำเป็นต่องใสรหัสประจำตัวประชาชน";
        } else if (!Validator.validateCitizenId(citizenId)) {
            valid = false;
            errorMessage = "รหัสประจำตัวประชาชนไม่ถูกต้อง";
        } else {
            errorMessage = null;
        }
        getCitizenIdEditText().setError(errorMessage);
        return valid;
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

    private String getFNameText(){
        return getfNameEditText().getText().toString();
    }

    private String getBDateText(){
        return getbDateEditText().getText().toString();
    }

    private String getPhoneNumberText(){
        return getPhoneNumberEditText().getText().toString();
    }

    private String getLNameText(){
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

}
