package com.tinyandfriend.project.friendstrip.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.tinyandfriend.project.friendstrip.R;
import com.tinyandfriend.project.friendstrip.info.FriendInfo;
import com.tinyandfriend.project.friendstrip.view.ProfileDialog;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.FriendListHolder> {
    private final Context context;
    private final List<FriendInfo> friendList;
    private DatabaseReference reference;

    class FriendListHolder extends RecyclerView.ViewHolder{

        View view;
        TextView friendName;
        CircleImageView friendPhoto;

        FriendListHolder(View itemView) {
            super(itemView);
            view = itemView;
            friendPhoto = (CircleImageView)itemView.findViewById(R.id.friend_photo);
            friendName = (TextView)itemView.findViewById(R.id.friend_name);
        }
    }
    public FriendListAdapter(Context context, List<FriendInfo> friendList, DatabaseReference reference) {
        this.context = context;
        this.friendList = friendList;
        this.reference = reference;
    }

    @Override
    public FriendListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_list_row, parent, false);
        return new FriendListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FriendListHolder holder, int position) {
        final FriendInfo friendInfo = friendList.get(position);
        holder.friendName.setText(friendInfo.getFriendName());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileDialog profileDialog;
                profileDialog = new ProfileDialog(context, friendInfo.getFriendUid(), reference);
                profileDialog.show();
            }
        });
        if (friendInfo.getFriendPhotoUrl() != null) {
            Glide.with(context)
                    .load(friendInfo.getFriendPhotoUrl()).centerCrop()
                    .into(holder.friendPhoto);
        }
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

}
