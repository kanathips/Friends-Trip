package com.tinyandfriend.project.friendstrip.info;

import android.net.Uri;

/**
 * Created by NewWy on 25/10/2559.
 */
public class FileInfo {

    private String fileName;
    private Uri uri;

    public FileInfo (){

    }


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
