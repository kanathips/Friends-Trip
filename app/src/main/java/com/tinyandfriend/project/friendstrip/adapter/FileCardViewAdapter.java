//package com.tinyandfriend.project.friendstrip.adapter;
//
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.tinyandfriend.project.friendstrip.R;
//import com.tinyandfriend.project.friendstrip.info.FileInfo;
//
//import java.util.ArrayList;
//
//public class FileCardViewAdapter extends RecyclerView.Adapter<FileCardViewAdapter.FileHolder> {
//
//    private final ArrayList<FileInfo> fileInfos;
//
//    class FileHolder extends RecyclerView.ViewHolder {
//        TextView fileName;
//        ImageView deleteButton;
//
//        FileHolder(View itemView) {
//            super(itemView);
//            fileName = (TextView) itemView.findViewById(R.id.file_name);
//            deleteButton = (ImageView)itemView.findViewById(R.id.delete);
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return fileInfos.size();
//    }
//
//    public FileCardViewAdapter(ArrayList<FileInfo> fileInfos) {
//        this.fileInfos = fileInfos;
//    }
//
//    @Override
//    public FileCardViewAdapter.FileHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.file_row, parent, false);
//        return new FileCardViewAdapter.FileHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(final FileCardViewAdapter.FileHolder holder, final int position) {
//        final FileInfo fileInfo = fileInfos.get(position);
//        holder.fileName.setText(fileInfo.getFileName());
//        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fileInfos.remove(position);
//                FileCardViewAdapter.super.notifyDataSetChanged();
//            }
//        });
//    }
//}


package com.tinyandfriend.project.friendstrip.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tinyandfriend.project.friendstrip.R;
import com.tinyandfriend.project.friendstrip.info.FileInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by StandAlone on 21/10/2559.
 */

public class FileCardViewAdapter extends RecyclerView.Adapter<FileCardViewAdapter.FileHolder> {

    private ArrayList<FileInfo> fileInfos;

    class FileHolder extends RecyclerView.ViewHolder {
        ImageView deleteButton;
        TextView fileName;

        FileHolder(View itemView) {
            super(itemView);
            fileName = (TextView) itemView.findViewById(R.id.file_name);
            deleteButton = (ImageView) itemView.findViewById(R.id.delete);
        }
    }

    public FileCardViewAdapter(ArrayList<FileInfo> fileInfos) {
        this.fileInfos = fileInfos;
    }

    @Override
    public FileHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.file_row, parent, false);

        return new FileHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FileHolder holder, final int position) {

        final FileInfo fileInfo = fileInfos.get(position);
        holder.fileName.setText(fileInfo.getFileName());
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileInfos.remove(fileInfo);
                FileCardViewAdapter.super.notifyItemRemoved(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return fileInfos.size();
    }


}
