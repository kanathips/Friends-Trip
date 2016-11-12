package com.tinyandfriend.project.friendstrip.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tinyandfriend.project.friendstrip.R;
import com.tinyandfriend.project.friendstrip.info.FriendInfo;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.FriendListHolder> {
    private final Context context;
    private final List<FriendInfo> friendList;

    class FriendListHolder extends RecyclerView.ViewHolder{

        TextView friendName;
        CircleImageView friendPhoto;

        FriendListHolder(View itemView) {
            super(itemView);
            friendPhoto = (CircleImageView)itemView.findViewById(R.id.friend_photo);
            friendName = (TextView)itemView.findViewById(R.id.friend_name);
        }
    }

    public FriendListAdapter(Context context, List<FriendInfo> friendList) {
        this.context = context;
        this.friendList = friendList;
    }

    @Override
    public FriendListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_list_row, parent, false);
        return new FriendListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FriendListHolder holder, int position) {

        FriendInfo friendInfo = friendList.get(position);
        holder.friendName.setText(friendInfo.getFriendName());

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
