package com.tinyandfriend.project.friendstrip.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.tinyandfriend.project.friendstrip.R;
import com.tinyandfriend.project.friendstrip.info.MemberInfo;
import com.tinyandfriend.project.friendstrip.view.ProfileDialog;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MemberListAdapter extends RecyclerView.Adapter<MemberListAdapter.MemberHolder> {
    private final Context context;
    private final List<MemberInfo> memberList;
    private DatabaseReference reference;

    class MemberHolder extends RecyclerView.ViewHolder{

        View view;
        TextView memberName;
        CircleImageView memberPhoto;

        MemberHolder(View itemView) {
            super(itemView);
            view = itemView;
            memberPhoto = (CircleImageView)itemView.findViewById(R.id.member_photo);
            memberName = (TextView)itemView.findViewById(R.id.member_name);
        }
    }

    public MemberListAdapter(Context context, ArrayList<MemberInfo> memberList, DatabaseReference reference) {
        this.context = context;
        this.memberList = memberList;
        this.reference = reference;
    }

    @Override
    public MemberHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.member_item, parent, false);
        return new MemberHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MemberHolder holder, int position) {
        final MemberInfo memberInfo = memberList.get(position);
        holder.memberName.setText(memberInfo.getName());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileDialog profileDialog;
                profileDialog = new ProfileDialog(context, memberInfo.getUid(), reference);
                profileDialog.show();
            }
        });
        if (memberInfo.getPhoto() != null) {
            Glide.with(context)
                    .load(memberInfo.getPhoto()).centerCrop()
                    .into(holder.memberPhoto);
        }
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }

}
