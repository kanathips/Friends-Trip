package com.tinyandfriend.project.friendstrip.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tinyandfriend.project.friendstrip.info.SignUpInfo;

import com.tinyandfriend.project.friendstrip.R;
import com.tinyandfriend.project.friendstrip.info.UserInfo;

public class UserInfoActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText displayNameEditText;
    private EditText phoneNumberEditText;
    private EditText bDateEditText;
    private EditText fNameEditText;
    private EditText lNameEditText;
    private TextView email_show;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private static final String USERS_CHILD = "users";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(user.getDisplayName());

        setEmailEditText((EditText) findViewById(R.id.email));
        setRePasswordEditText((EditText) findViewById(R.id.repassword));
        setPasswordEditText((EditText) findViewById(R.id.password));
        setDisplayNameEditText((EditText) findViewById(R.id.displayname));
        setbDateEditText((EditText) findViewById(R.id.birth_date));
        setfNameEditText((EditText) findViewById(R.id.first_name));
        setlNameEditText((EditText) findViewById(R.id.last_name));
        setEmail_show((TextView) findViewById(R.id.user_profile_short_bio));
        setPhoneNumberEditText((EditText) findViewById(R.id.phonenumber));
        String userEmail = user.getEmail();
        String userUid = user.getUid();

        getEmail_show().setText(userEmail);

        reference.child(USERS_CHILD).child(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
                getfNameEditText().setText(userInfo.getfName());
                getlNameEditText().setText(userInfo.getlName());
                getEmailEditText().setText(userInfo.getEmail());
                getDisplayNameEditText().setText(userInfo.getDisplayName());
                getbDateEditText().setText(userInfo.getDateOfBirth());
                getPhoneNumberEditText().setText(userInfo.getPhoneNumber());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public TextView getEmail_show() {
        return email_show;
    }

    public void setEmail_show(TextView email_show) {
        this.email_show = email_show;
    }

    public EditText getEmailEditText() {
        return emailEditText;
    }

    public void setEmailEditText(EditText emailEditText) {
        this.emailEditText = emailEditText;
    }

    public void setRePasswordEditText(EditText rePasswordEditText) {
    }

    public void setPasswordEditText(EditText passwordEditText) {
    }

    public EditText getDisplayNameEditText() {
        return displayNameEditText;
    }

    public void setDisplayNameEditText(EditText displayNameEditText) {
        this.displayNameEditText = displayNameEditText;
    }

    public EditText getPhoneNumberEditText() {
        return phoneNumberEditText;
    }

    public void setPhoneNumberEditText(EditText phoneNumberEditText) {
        this.phoneNumberEditText = phoneNumberEditText;
    }

    public EditText getbDateEditText() {
        return bDateEditText;
    }

    public void setbDateEditText(EditText bDateEditText) {
        this.bDateEditText = bDateEditText;
    }

    public EditText getfNameEditText() {
        return fNameEditText;
    }

    public void setfNameEditText(EditText fNameEditText) {
        this.fNameEditText = fNameEditText;
    }

    public EditText getlNameEditText() {
        return lNameEditText;
    }

    public void setlNameEditText(EditText lNameEditText) {
        this.lNameEditText = lNameEditText;
    }
}
