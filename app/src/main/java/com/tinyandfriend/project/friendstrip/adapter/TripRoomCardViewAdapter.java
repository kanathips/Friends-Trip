package com.tinyandfriend.project.friendstrip.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tinyandfriend.project.friendstrip.R;
import com.tinyandfriend.project.friendstrip.info.CardViewInfo;

import java.util.List;

/**
 * Created by StandAlone on 21/10/2559.
 */

public class TripRoomCardViewAdapter extends RecyclerView.Adapter<TripRoomCardViewAdapter.TripRoomHolder> {

    private Context mContext;
    private List<CardViewInfo> albumList;

    class TripRoomHolder extends RecyclerView.ViewHolder {
        TextView titles,date_card,count;
        ImageView thumbnail;

        TripRoomHolder(View itemView) {
            super(itemView);
            titles = (TextView) itemView.findViewById(R.id.name_card);
            date_card = (TextView) itemView.findViewById(R.id.date_card);
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
        holder.date_card.setText(album.getDate_card());
        holder.count.setText(album.getCount_people());
        holder.thumbnail.setImageResource(album.getPic_id());
        holder.thumbnail.setScaleType(ImageView.ScaleType.CENTER_CROP);

    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }


}
