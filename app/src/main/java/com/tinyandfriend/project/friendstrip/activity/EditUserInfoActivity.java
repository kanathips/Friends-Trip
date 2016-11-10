package com.tinyandfriend.project.friendstrip.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tinyandfriend.project.friendstrip.R;
import com.tinyandfriend.project.friendstrip.info.TripInfo;
import com.tinyandfriend.project.friendstrip.info.UserInfo;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditUserInfoActivity extends AppCompatActivity {

    private static final int PHOTO_CHOOSE = 1;
    private static final String PROFILE_PHOTO_CHILD = "profilePhoto";
    private EditText emailEditText;
    private EditText displayNameEditText;
    private EditText phoneNumberEditText;
    private EditText bDateEditText;
    private EditText fNameEditText;
    private EditText lNameEditText;
    private TextView emailShow;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private static final String USERS_CHILD = "users";
    private static final String PROFILE_PHOTO_DIR = "profilePhoto";
    private CircleImageView circleImageView;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private Uri selectedImage;
    private StorageMetadata metadata;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(user.getDisplayName());

        setupVariable();

        String userEmail = user.getEmail();
        String userUid = user.getUid();

        setup(userUid);
        getEmailShow().setText(userEmail);

        setChangeProfilePhoto(userUid);
    }

    private void setup(String userUid) {
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

        setupConfirmButton(userUid);

        metadata = new StorageMetadata.Builder()
                .setContentType("image/jpeg")
                .build();
    }

    private void setupConfirmButton(final String userUid) {
        Button confirmButton = (Button) findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditUserInfoActivity.this);
                builder.setMessage("ยืนยันการเปลี่ยนข้อมูลผู้ใช้งานหรือไม่ ?");
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        final Map<String, Object> userInfoMap = getUserInfoMap();
                        userInfoMap.put(PROFILE_PHOTO_CHILD, null);
                        if (selectedImage != null) {
                            storageReference.child(PROFILE_PHOTO_DIR + "/" + user + ".jpg").putFile(selectedImage, metadata).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    if(task.isSuccessful()){
                                        userInfoMap.put(PROFILE_PHOTO_CHILD, task.getResult().getDownloadUrl());
                                    }
                                    reference.child(USERS_CHILD).child(userUid).updateChildren(userInfoMap);
                                    Toast.makeText(EditUserInfoActivity.this, "FIN", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
    }

    private void setChangeProfilePhoto(String userUid) {
        circleImageView = (CircleImageView) findViewById(R.id.user_profile_photo);
        reference.child(USERS_CHILD).child(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot profilePhotoChild = dataSnapshot.child(PROFILE_PHOTO_CHILD);
                if (profilePhotoChild.exists()) {
                    String profilePhotoURL = profilePhotoChild.getValue(String.class);
                    Glide.with(EditUserInfoActivity.this)
                            .load(profilePhotoURL).centerCrop()
                            .into(circleImageView);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, PHOTO_CHOOSE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PHOTO_CHOOSE:
                if (resultCode == RESULT_OK && null != data) {
                    selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);

                    assert cursor != null;
                    cursor.moveToFirst();
                    cursor.close();

                    circleImageView.setImageURI(selectedImage);
                    circleImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }
        }
    }

    private Map<String, Object> getUserInfoMap() {

        Map<String, Object> userInfoMap = new HashMap<>();

        String dateOfBirth = getbDateEditText().getText().toString();
        if (!dateOfBirth.trim().isEmpty()) {
            userInfoMap.put("dateOfBirth", dateOfBirth);
        }

        String email = getEmailEditText().getText().toString();
        if (!email.trim().isEmpty()) {
            userInfoMap.put("email", email);
        }

        String fName = getfNameEditText().getText().toString();
        if (!fName.trim().isEmpty()) {
            userInfoMap.put("fName", fName);
        }

        String lName = getlNameEditText().getText().toString();
        if (!lName.trim().isEmpty()) {
            userInfoMap.put("lName", lName);
        }

        String displayName = getDisplayNameEditText().getText().toString();
        if (!displayName.trim().isEmpty()) {
            userInfoMap.put("displayName", displayName);
        }

        String phoneNumber = getPhoneNumberEditText().getText().toString();
        if (!phoneNumber.trim().isEmpty()) {
            userInfoMap.put("phoneNumber", phoneNumber);
        }

        return userInfoMap;
    }

    private void setupVariable() {
        setEmailEditText((EditText) findViewById(R.id.email));
        setRePasswordEditText((EditText) findViewById(R.id.repassword));
        setPasswordEditText((EditText) findViewById(R.id.password));
        setDisplayNameEditText((EditText) findViewById(R.id.displayname));
        setbDateEditText((EditText) findViewById(R.id.birth_date));
        setfNameEditText((EditText) findViewById(R.id.first_name));
        setlNameEditText((EditText) findViewById(R.id.last_name));
        setEmailShow((TextView) findViewById(R.id.user_profile_short_bio));
        setPhoneNumberEditText((EditText) findViewById(R.id.phonenumber));
    }

    public TextView getEmailShow() {
        return emailShow;
    }

    public void setEmailShow(TextView emailShow) {
        this.emailShow = emailShow;
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

    private EditText getbDateEditText() {
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
