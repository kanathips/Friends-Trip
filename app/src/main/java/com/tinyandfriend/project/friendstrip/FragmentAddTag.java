package com.tinyandfriend.project.friendstrip;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.tinyandfriend.project.friendstrip.adapter.FileCardViewAdapter;
import com.tinyandfriend.project.friendstrip.adapter.TagListViewAdapter;
import com.tinyandfriend.project.friendstrip.info.FileInfo;
import com.tinyandfriend.project.friendstrip.info.TripInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.app.Activity.RESULT_OK;

public class FragmentAddTag extends FragmentPager {


    private Context context;
    private static final String TAG = "ADD_TAG_FRAGMENT";
    private static final int ID_REQUEST = 99;
    private TagListViewAdapter regionTagAdapter;
    private TagListViewAdapter tripTagAdapter;
    private TagListViewAdapter placeTagAdapter;
    private ArrayList<FileInfo> fileInfos;
    private FileCardViewAdapter fileCardViewAdapter;
    private ImageView headerImage;
    private int[] tempHeaderImage = {
            R.drawable.pic1, R.drawable.pic2, R.drawable.pic3,
            R.drawable.pic4, R.drawable.pic5, R.drawable.pic6,
            R.drawable.pic7, R.drawable.pic8, R.drawable.pic9,
            R.drawable.pic10};
    private Uri thumbnailUri;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_tag, container, false);
        context = getContext();

        setUpTagListView(rootView);

        Button uploadButton = (Button) rootView.findViewById(R.id.upload_button);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        uploadFiles = new ArrayList<>();

        fileInfos = new ArrayList<>();
        fileCardViewAdapter = new FileCardViewAdapter(fileInfos);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.file_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(fileCardViewAdapter);

        Random random = new Random();
        int ranInt = random.nextInt(9);
        int imgResource = tempHeaderImage[ranInt];
        headerImage = (ImageView) rootView.findViewById(R.id.header_image);
        headerImage.setImageResource(imgResource);
        headerImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        thumbnailUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                context.getResources().getResourcePackageName(imgResource) + '/' +
                context.getResources().getResourceTypeName(imgResource) + '/' +
                context.getResources().getResourceEntryName(imgResource) );

        FloatingActionButton floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.choose_pic_room);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, ID_REQUEST);

            }

        });


        return rootView;
    }


    private void setUpTagListView(View view) {
        ListView regionListView = (ListView) view.findViewById(R.id.tag_region);
        regionTagAdapter = new TagListViewAdapter(context, R.array.tag_region, R.layout.tag_listview_row);
        regionListView.setAdapter(regionTagAdapter);

        ListView tripListView = (ListView) view.findViewById(R.id.tag_trip);
        tripTagAdapter = new TagListViewAdapter(context, R.array.tag_trip, R.layout.tag_listview_row);
        tripListView.setAdapter(tripTagAdapter);

        ListView placeListView = (ListView) view.findViewById(R.id.tag_place);
        placeTagAdapter = new TagListViewAdapter(context, R.array.tag_place, R.layout.tag_listview_row);
        placeListView.setAdapter(placeTagAdapter);
    }

    @Override
    boolean validateFrom() {
        return true;
    }

    public void onClickAddImgHeader(View view){

    }

    @Override
    void setInfo(Object info) {
        TripInfo tripInfo = (TripInfo) info;

        ArrayList<String> tagArray = new ArrayList<>();
        tagArray.addAll(placeTagAdapter.getSelectedTag());
        tagArray.addAll(regionTagAdapter.getSelectedTag());
        tagArray.addAll(tripTagAdapter.getSelectedTag());

        tripInfo.setTag(tagArray);
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
        if (Build.VERSION.SDK_INT < 19) {
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

    ArrayList<String> uploadFiles;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    FileInfo fileInfo = new FileInfo();
                    // Get the Uri of the selected file
                    fileInfo.setUri(data.getData());
                    File file = new File(fileInfo.getUri().getPath());
                    fileInfo.setFileName(file.getName());
                    fileInfos.add(fileInfo);
                    Log.d(TAG, fileInfo.getFileName() + " Uri: " + fileInfo.getUri());
                    fileCardViewAdapter.notifyDataSetChanged();
                }
                break;
            case ID_REQUEST:
                if (requestCode == ID_REQUEST && resultCode == RESULT_OK && null != data) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    Cursor cursor = context.getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    TripInfo tripInfo = new TripInfo();

                    ImageView imageView = (ImageView) getView().findViewById(R.id.header_image);
                    imageView.setImageURI(selectedImage);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    thumbnailUri = selectedImage;
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);


    }

    public Uri getThumbnailUri(){
        return thumbnailUri;
    }

    public ArrayList<FileInfo> getFileList() {
        return fileInfos;
    }
}
