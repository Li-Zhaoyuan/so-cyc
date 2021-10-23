package com.dubailizards.so_cyc.entity;


import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dubailizards.so_cyc.R;

public class CustomListViewAdapter extends ArrayAdapter<EventDetails> {

    private final Activity context;
    private final EventDetails[] details;

    public CustomListViewAdapter(Activity context, EventDetails[] details) {
        super(context, R.layout.listview_row_custom, details);

        this.context = context;
        this.details = details;

    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_row_custom, null,true);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.r_icon);
        TextView titleText = (TextView) rowView.findViewById(R.id.r_title);
        TextView subtitleText = (TextView) rowView.findViewById(R.id.r_subtitle);

        if (details != null && details.length > 0) {
            EventDetails curr = details[position];
            Glide.with(context).load(curr.getProfilePictureURL()).into(imageView);
            titleText.setText(curr.getEventTitle());
            subtitleText.setText(curr.getHostDisplayName());
        }
        return rowView;

    };
}