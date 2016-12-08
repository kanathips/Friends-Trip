package com.tinyandfriend.project.friendstrip.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.UploadTask;
import com.tinyandfriend.project.friendstrip.ConstantValue;
import com.tinyandfriend.project.friendstrip.fragment.FragmentAddPlace;
import com.tinyandfriend.project.friendstrip.fragment.FragmentAddTag;
import com.tinyandfriend.project.friendstrip.FragmentPager;
import com.tinyandfriend.project.friendstrip.R;
import com.tinyandfriend.project.friendstrip.adapter.FragmentPagerAdapter;
import com.tinyandfriend.project.friendstrip.info.FileInfo;
import com.tinyandfriend.project.friendstrip.info.TripInfo;
import com.tinyandfriend.project.friendstrip.view.NoSwipeViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.tinyandfriend.project.friendstrip.R.drawable.user;


public class CreateTripActivity extends AppCompatActivity {

    private static final String TAG = "CREATE_TRIP_ACTIVITY";
    private NoSwipeViewPager viewPager;
    private ArrayList<FragmentPager> fragmentPagers;
    private TripInfo tripInfo;
    private StorageMetadata metadata;
    private DatabaseReference reference;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_trip_page);

        metadata = new StorageMetadata.Builder()
                .setContentType("application/pdf")
                .build();

        FragmentManager fragmentManager = getSupportFragmentManager();


        fragmentPagers = new ArrayList<>();
        FragmentAddPlace fragmentAddPlace = new FragmentAddPlace();

        FragmentAddTag addTagFragment = new FragmentAddTag();

        fragmentPagers.add(fragmentAddPlace);
        fragmentPagers.add(addTagFragment);
        FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(fragmentManager, fragmentPagers);

        viewPager = (NoSwipeViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(pagerAdapter);
        tripInfo = new TripInfo();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.next_page_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final FragmentPager currentFragment = fragmentPagers.get(viewPager.getCurrentItem());
        switch (item.getItemId()) {
            case (R.id.action_next):
                if (viewPager.getCurrentItem() == 0 && currentFragment.validateFrom()) {
                    currentFragment.setInfo(tripInfo);
                    viewPager.setCurrentItem(1);
                } else if (viewPager.getCurrentItem() == 1) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(this);
                    adb.setTitle("ยืนยัน");
                    adb.setMessage("ยินยันการสร้างห้องหรือไม่");
                    adb.setIcon(android.R.drawable.ic_dialog_alert);
                    adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (currentFragment.validateFrom()) {
                                final ProgressDialog processDialog = ProgressDialog.show(CreateTripActivity.this, null, "กำลังสร้างทริป โปรดรอสักครู่");
                                currentFragment.setInfo(tripInfo);

                                final FragmentAddTag addTagFragment = (FragmentAddTag) currentFragment;
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                tripInfo.setOwnerUID(user.getUid());
                                reference = FirebaseDatabase.getInstance().getReference();
                                final DatabaseReference tripRoomReference = reference.child("tripRoom").push();
                                String tripId = tripRoomReference.getKey();
                                pushTagIndex(reference, tripInfo.getTag().keySet(), tripId);
                                upload(addTagFragment, tripRoomReference,tripRoomReference, processDialog);
                            }
                        }
                    });
                    adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    adb.show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 1) {
            viewPager.setCurrentItem(0);
        } else {
            super.onBackPressed();
        }
    }

    private void pushTagIndex(DatabaseReference reference, Set<String> tags, String tripId){
        for(String tagName: tags){
            Map<String, Object> map = new HashMap<>();
            map.put(tripId, true);
            reference.child(ConstantValue.TAG_INDEX_CHILD).child(tagName).updateChildren(map);
        }
    }

    private void upload(final FragmentAddTag fragmentAddTag, final DatabaseReference databaseReference, final DatabaseReference tripRoomReference, final ProgressDialog processDialog) {
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpeg")
                .build();



        Uri thumbNailUri = fragmentAddTag.getThumbnailUri();
        UploadTask uploadTask = storage.getReference().child(tripRoomReference.getKey() + "/thumbnail/thumbnail.jpg").putFile(thumbNailUri, metadata);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                tripInfo.setThumbnail(taskSnapshot.getDownloadUrl().toString());
                ArrayList<FileInfo> fileInfos = fragmentAddTag.getFileList();
                ArrayList<String> successFiles = new ArrayList<>();
                if (fileInfos.size() == 0) {
                    tripRoomReference.setValue(tripInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            updateTripRoom(databaseReference, tripRoomReference.getKey(), user.getUid());
                            processDialog.dismiss();
                            CreateTripActivity.this.finish();
                        }
                    });

                } else if (fileInfos.size() > 0) {
                    processDialog.setMessage("กำลังอัพโหลดไฟล์");
                    for (int i = 0; i < fileInfos.size(); i++) {
                        uploadFile(fileInfos, i, tripRoomReference, processDialog, successFiles);
                    }
                }
            }
        });
    }

    FirebaseStorage storage = FirebaseStorage.getInstance();

    private void uploadFile(final ArrayList<FileInfo> fileInfos, final int position, final DatabaseReference tripReference, final ProgressDialog processDialog, final ArrayList<String> successFiles) {
        Uri uri = fileInfos.get(position).getUri();
        final String fileName = fileInfos.get(position).getFileName();

        UploadTask uploadTask = storage.getReference().child(tripReference.getKey() + "/trip_document/" + fileName).putFile(uri, metadata);
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                int progress = (int) ((100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
                processDialog.setMessage(fileName + " : " + progress + "%");
                Log.i(TAG, fileName + " upload is " + progress + "% done");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateTripActivity.this, "UPLOAD ERROR", Toast.LENGTH_SHORT).show();
                processDialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.i(TAG, taskSnapshot.getMetadata().getName() + ": is upload success");
                successFiles.add(taskSnapshot.getDownloadUrl().toString());

                if (position + 1 == fileInfos.size()) {
                    processDialog.setMessage("อัพโหลดเสร็จสิ้น");
                    tripInfo.setFiles(successFiles);
                    tripReference.setValue(tripInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            updateTripRoom(tripReference, tripReference.getKey(), user.getUid());
                            processDialog.dismiss();
                            CreateTripActivity.this.finish();
                        }
                    });

                }
            }
        });
    }


    private void updateTripRoom(DatabaseReference tripReference, final String tripId, final String userUid){

        tripReference = reference.child(ConstantValue.TRIP_ROOM_CHILD).child(tripId);
        final DatabaseReference finalTripReference = tripReference;
        tripReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Long maxMember = (Long) dataSnapshot.child("maxMember").getValue();
                    if(dataSnapshot.child("members").getChildrenCount() < maxMember){
                        Map<String, Object> map = new HashMap<>();
                        map.put("name", user.getDisplayName());
                        map.put("uid", user.getUid());
                        map.put("photo", user.getPhotoUrl().toString());
                        finalTripReference.child("members").child(userUid).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                updateUserProfile(reference, tripId, userUid);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(CreateTripActivity.this, "ไม่สามารถเข้าร่วมได้ กรุณาลองใหม่อีกครั้ง (1)", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        Toast.makeText(CreateTripActivity.this, "ทริปเต็มแล้ว", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference tripMemberReference = tripReference.child("members");
        Map<String, Object> map = new HashMap<>();
        map.put(userUid, true);
        tripMemberReference.updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                updateUserProfile(reference, tripId, userUid);
            }
        });
    }

    private void updateUserProfile(DatabaseReference reference, String tripId, String userUid){
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("tripId", tripId);
        reference.child("users").child(userUid).updateChildren(updateMap);
    }

}
