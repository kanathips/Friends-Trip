package com.tinyandfriend.project.friendstrip.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tinyandfriend.project.friendstrip.R;

/**
 * Created by NewWy on 10/10/2559.
 */

public class TagListViewAdapter extends BaseAdapter {

    private String[] tag;
    private int tagLayout;
    private Context context;

    public TagListViewAdapter(Context context, int tag, int tagLayout){
        this.tag = context.getResources().getStringArray(tag);
        this.context = context;
        this.tagLayout = tagLayout;
    }

    public TagListViewAdapter(Context context, String[] tag, int tagLayout){
        this.tag = tag;
        this.tagLayout = tagLayout;
        this.context  = context;
    }

    @Override
    public int getCount() {
        return tag.length;
    }

    @Override
    public Object getItem(int position) {
        return tag[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(tagLayout, parent, false);
        TextView textView = (TextView)view.findViewById(R.id.tag_name);
        textView.setText(tag[position]);
        return view;
    }
}
