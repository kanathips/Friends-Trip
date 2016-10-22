package com.tinyandfriend.project.friendstrip;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.CancellableTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.UploadTask;
import com.tinyandfriend.project.friendstrip.adapter.TagListViewAdapter;
import com.tinyandfriend.project.friendstrip.info.CreateTripInfo;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.TreeMap;

import static android.app.Activity.RESULT_OK;

public class AddTagFragment extends FragmentPager {


    private Context context;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private String TAG = "ADD_TAG_FRAGMENT";
    private StorageMetadata metadata;
    private boolean isUploading = false;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_add_tag, container, false);
        context = getContext();
        ListView regionListView = (ListView) rootView.findViewById(R.id.tag_region);
        TagListViewAdapter regionTagAdapter = new TagListViewAdapter(context, R.array.tag_region, R.layout.tag_listview_row);
        regionListView.setAdapter(regionTagAdapter);

        ListView tripListView = (ListView) rootView.findViewById(R.id.tag_trip);
        TagListViewAdapter tripTagAdapter = new TagListViewAdapter(context, R.array.tag_trip, R.layout.tag_listview_row);
        tripListView.setAdapter(tripTagAdapter);

        ListView placeListView = (ListView) rootView.findViewById(R.id.tag_place);
        TagListViewAdapter placeTagAdapter = new TagListViewAdapter(context, R.array.tag_place, R.layout.tag_listview_row);
        placeListView.setAdapter(placeTagAdapter);

        Button uploadButton = (Button) rootView.findViewById(R.id.upload_button);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        metadata = new StorageMetadata.Builder()
                .setContentType("application/pdf")
                .build();
        uploadFiles = new TreeMap<>();

        return rootView;
    }

    @Override
    boolean validateFrom() {
        return !isUploading;
    }

    @Override
    void setInfo(Object info) {
        CreateTripInfo tripInfo = (CreateTripInfo)info;

        EditText editText;

        editText = (EditText)rootView.findViewById(R.id.trip_spoil);
        tripInfo.setTripSpoil(getEditTextInput(editText));
        tripInfo.setFiles(uploadFiles);
    }

    private String getEditTextInput(EditText editText){
        String input;
        input = editText.getText().toString();
        if(input.isEmpty())
            input = null;
        return input;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private static final int FILE_SELECT_CODE = 0;

    private void showFileChooser() {
        Intent intent;
        if (Build.VERSION.SDK_INT <19){
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        }

        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(context, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }

    TreeMap<String, String> uploadFiles;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    Log.d(TAG, "File Uri: " + uri.toString());

                    File file = new File(uri.getPath());
                    Log.d(TAG, "File Name: " + file.getName());
                    isUploading = true;
                    UploadTask uploadTask = storage.getReference().child("images/"+file.getName()).putFile(uri, metadata);
                    uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            Log.i(TAG, "Upload is " + progress + "% done");

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "UPLOAD ERROR", Toast.LENGTH_SHORT).show();
                            isUploading = false;
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            uploadFiles.put(taskSnapshot.getMetadata().getName(), taskSnapshot.getDownloadUrl().toString());
                            isUploading = false;
                            Log.i(TAG, taskSnapshot.getMetadata().getName() + ": is upload success" );
                            Toast.makeText(context, taskSnapshot.getMetadata().getName() + ": is upload success", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
