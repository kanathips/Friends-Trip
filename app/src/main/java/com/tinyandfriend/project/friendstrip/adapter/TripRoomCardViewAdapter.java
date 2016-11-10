package com.tinyandfriend.project.friendstrip.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tinyandfriend.project.friendstrip.R;
import com.tinyandfriend.project.friendstrip.info.CardViewInfo;

import java.util.List;

public class TripRoomCardViewAdapter extends RecyclerView.Adapter<TripRoomCardViewAdapter.TripRoomHolder> {

    private Context mContext;
    private List<CardViewInfo> albumList;

    class TripRoomHolder extends RecyclerView.ViewHolder {
        TextView titles, tripStart,count;
        ImageView thumbnail;
        TextView tripEnd;

        TripRoomHolder(View itemView) {
            super(itemView);
            titles = (TextView) itemView.findViewById(R.id.name_card);
            tripStart = (TextView) itemView.findViewById(R.id.trip_start);
            tripEnd = (TextView) itemView.findViewById(R.id.trip_end);
            count = (TextView) itemView.findViewById(R.id.count_people);
            thumbnail = (ImageView) itemView.findViewById(R.id.image_card);
        }
    }

    public TripRoomCardViewAdapter(Context mContext, List<CardViewInfo> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public TripRoomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_row, parent, false);

        return new TripRoomHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TripRoomHolder holder, int position) {

        CardViewInfo album = albumList.get(position);

        holder.titles.setText(album.getName_card());
        holder.tripStart.setText(album.getTripStart());
        holder.tripEnd.setText(album.getTripEnd());
        holder.count.setText(Integer.toString(album.getCount_people()));

        if (album.getThumbnail() != null) {
            Glide.with(mContext)
                    .load(album.getThumbnail()).centerCrop()
                    .into(holder.thumbnail);
        }
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }


}
