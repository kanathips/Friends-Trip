package com.tinyandfriend.project.friendstrip.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tinyandfriend.project.friendstrip.activity.JoinDetailActivity;
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
        CardView cardView;

        TripRoomHolder(View itemView) {
            super(itemView);
            titles = (TextView) itemView.findViewById(R.id.name_card);
            tripStart = (TextView) itemView.findViewById(R.id.trip_start);
            tripEnd = (TextView) itemView.findViewById(R.id.trip_end);
            count = (TextView) itemView.findViewById(R.id.count_people);
            thumbnail = (ImageView) itemView.findViewById(R.id.image_card);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
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

        final CardViewInfo album = albumList.get(position);

        holder.titles.setText(album.getName_card());
        holder.tripStart.setText(album.getTripStart());
        holder.tripEnd.setText(album.getTripEnd());
        holder.count.setText(Integer.toString(album.getCount_people()));

        if (album.getThumbnail() != null) {
            Glide.with(mContext)
                    .load(album.getThumbnail()).centerCrop()
                    .into(holder.thumbnail);

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, JoinDetailActivity.class);
                    intent.putExtra("key_room",album.getTripId());
                    intent.putExtra("start_date",holder.tripStart.getText().toString());
                    intent.putExtra("end_date",holder.tripEnd.getText().toString());
                    intent.putExtra("count_people",holder.count.getText().toString());
                    intent.putExtra("name_trip",holder.titles.getText().toString());
                    intent.putExtra("pic_thumbnail",album.getThumbnail().toString());

                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext, holder.thumbnail, "profile");

                    mContext.startActivity(intent, options.toBundle());


//                mContext.startActivity(new Intent(mContext,JoinDetailActivity.class));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }


}
