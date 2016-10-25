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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import java.util.HashMap;
import java.util.Map;


public class CreateTripActivity extends AppCompatActivity {

    private static final String TAG = "CREATE_TRIP_ACTIVITY";
    private NoSwipeViewPager viewPager;
    private ArrayList<FragmentPager> fragmentPagers;
    private TripInfo tripInfo;
    private StorageMetadata metadata;
    private ArrayList<String> uploadedFiles;

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

                                FragmentAddTag addTagFragment = (FragmentAddTag) currentFragment;
                                final ArrayList<FileInfo> fileInfos = addTagFragment.getFileList();
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                tripInfo.setOwnerUID(user.getUid());
                                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("tripRoom").push();
                                reference.setValue(tripInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (fileInfos.size() == 0) {
                                            CreateTripActivity.this.finish();
                                        } else if (fileInfos.size() > 0) {
                                            processDialog.setMessage("กำลังอัพโหลดไฟล์");
                                            for (int i = 0; i < fileInfos.size(); i++) {
                                                uploadFile(fileInfos, i, reference, processDialog);
                                            }
                                        }
                                    }
                                });
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

    FirebaseStorage storage = FirebaseStorage.getInstance();

    private void uploadFile(final ArrayList<FileInfo> fileInfos, final int position, final DatabaseReference reference, final ProgressDialog processDialog) {
        Uri uri = fileInfos.get(position).getUri();
        final String fileName = fileInfos.get(position).getFileName();
        UploadTask uploadTask = storage.getReference().child("trip_document/" + reference.getKey() + "/" + fileName).putFile(uri, metadata);
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                int progress = (int) ((100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
                processDialog.setMessage(fileName + " : " + progress + "%");
                Log.i(TAG, fileName+ " upload is " + progress + "% done");
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

                Map<String, Object> map = new HashMap<>();
                map.put(Integer.toString(position), taskSnapshot.getDownloadUrl().toString());
                reference.child("files").updateChildren(map);

                if (position + 1 == fileInfos.size()) {
                    processDialog.setMessage("อัพโหลดเสร็จสิ้น");
                    processDialog.dismiss();
                    CreateTripActivity.this.finish();
                }
            }
        });


    }
}
