package com.tinyandfriend.project.friendstrip;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.UploadTask;
import com.tinyandfriend.project.friendstrip.adapter.FragmentPagerAdapter;
import com.tinyandfriend.project.friendstrip.info.FileInfo;
import com.tinyandfriend.project.friendstrip.info.TripInfo;
import com.tinyandfriend.project.friendstrip.view.NoSwipeViewPager;

import java.util.ArrayList;


public class CreateTripActivity extends AppCompatActivity {

    private static final String TAG = "CREATE_TRIP_ACTIVITY";
    private NoSwipeViewPager viewPager;
    private ArrayList<FragmentPager> fragmentPagers;
    private TripInfo tripInfo;
    private StorageMetadata metadata;
    private boolean isThumbnailFinish = false;

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
                                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("tripRoom").push();

                                upload(addTagFragment, reference, processDialog);
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

    private void upload(final FragmentAddTag fragmentAddTag, final DatabaseReference databaseReference, final ProgressDialog processDialog) {
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpeg")
                .build();

        Uri thumbNailUri = fragmentAddTag.getThumbnailUri();
        UploadTask uploadTask = storage.getReference().child(databaseReference.getKey() + "/thumbnail/thumbnail.jpg").putFile(thumbNailUri, metadata);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                tripInfo.setThumbnail(taskSnapshot.getDownloadUrl().toString());
                ArrayList<FileInfo> fileInfos = fragmentAddTag.getFileList();
                ArrayList<String> successFiles = new ArrayList<String>();
                if (fileInfos.size() == 0) {
                    databaseReference.setValue(tripInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            processDialog.dismiss();
                            CreateTripActivity.this.finish();
                        }
                    });

                } else if (fileInfos.size() > 0) {
                    processDialog.setMessage("กำลังอัพโหลดไฟล์");
                    for (int i = 0; i < fileInfos.size(); i++) {
                        uploadFile(fileInfos, i, databaseReference, processDialog, successFiles);
                    }
                }
            }
        });
    }

    FirebaseStorage storage = FirebaseStorage.getInstance();

    private void uploadFile(final ArrayList<FileInfo> fileInfos, final int position, final DatabaseReference reference, final ProgressDialog processDialog, final ArrayList<String> successFiles) {
        Uri uri = fileInfos.get(position).getUri();
        final String fileName = fileInfos.get(position).getFileName();

        UploadTask uploadTask = storage.getReference().child(reference.getKey() + "/trip_document/" + fileName).putFile(uri, metadata);
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
                    reference.setValue(tripInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            processDialog.dismiss();
                            CreateTripActivity.this.finish();
                        }
                    });

                }
            }
        });


    }
}
