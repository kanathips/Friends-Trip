package com.tinyandfriend.project.friendstrip.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.tinyandfriend.project.friendstrip.Fragment_Join;
import com.tinyandfriend.project.friendstrip.R;
import com.tinyandfriend.project.friendstrip.info.CardView_Info;

import java.util.List;

import static com.tinyandfriend.project.friendstrip.R.id.imageView;

/**
 * Created by StandAlone on 21/10/2559.
 */

public class CardView_Adapter extends RecyclerView.Adapter<CardView_Adapter.MyViewHolder> {

    private Context mContext;
    private List<CardView_Info> albumList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView titles,date_card,count;
        public ImageView thumbnail;

        public MyViewHolder(View itemView) {
            super(itemView);
            titles = (TextView) itemView.findViewById(R.id.name_card);
            date_card = (TextView) itemView.findViewById(R.id.date_card);
            count = (TextView) itemView.findViewById(R.id.count_people);
            thumbnail = (ImageView) itemView.findViewById(R.id.image_card);

        }
    }

    public CardView_Adapter(Context mContext, List<CardView_Info> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public CardView_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        CardView_Info album = albumList.get(position);

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
