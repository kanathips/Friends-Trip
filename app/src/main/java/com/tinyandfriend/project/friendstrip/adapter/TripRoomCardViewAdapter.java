package com.tinyandfriend.project.friendstrip.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.tinyandfriend.project.friendstrip.JoinDetail;
import com.tinyandfriend.project.friendstrip.R;
import com.tinyandfriend.project.friendstrip.info.CardViewInfo;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class TripRoomCardViewAdapter extends RecyclerView.Adapter<TripRoomCardViewAdapter.TripRoomHolder> {

    private Context mContext;
    private List<CardViewInfo> albumList;

    class TripRoomHolder extends RecyclerView.ViewHolder {
        TextView titles,date_card,count;
        ImageView thumbnail;
        CardView cardView;

        TripRoomHolder(View itemView) {
            super(itemView);
            titles = (TextView) itemView.findViewById(R.id.name_card);
            date_card = (TextView) itemView.findViewById(R.id.date_card);
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

        CardViewInfo album = albumList.get(position);

        holder.titles.setText(album.getName_card());
        holder.date_card.setText(album.getDate_card());
        holder.count.setText(album.getCount_people());

        if (album.getThumbnail() != null) {
            System.out.println(album.getName_card() + "     " + album.getThumbnail());
//            Picasso.with(mContext).load(album.getThumbnail()).into(holder.thumbnail);
            Picasso.with(mContext)
                    .load(album.getThumbnail()).networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(holder.thumbnail);
            holder.thumbnail.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //implement onClick
                Intent intent = new Intent(mContext, JoinDetail.class);
                intent.putExtra("name_trip",holder.titles.getText().toString());
                intent.putExtra("pic_thumbnail",holder.thumbnail.getId());

                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext, holder.thumbnail, "profile");

                mContext.startActivity(intent, options.toBundle());


//                mContext.startActivity(new Intent(mContext,JoinDetail.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }


}
