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

/**
 *  Entity Class, Extension of the ArrayAdapter class
 *  Customized array adapter that generates list rows with customized layouts
 */
public class CustomListViewAdapter extends ArrayAdapter<EventDetails> {

    /**
     *  private Activity variable
     *  An entity that holds the context of the activity that uses this object
     */
    private final Activity context;

    /**
     *  private EventDetails[] variable
     *  An entity that holds EventDetails objects used to construct the list view
     */
    private final EventDetails[] details;

    /**
     * A public constructor
     * Sets up private parameters based on passed input
     * @param context is the current activity's context
     * @param details is the array of event details that is used to generate the list view
     */
    public CustomListViewAdapter(Activity context, EventDetails[] details) {
        super(context, R.layout.listview_row_custom, details);

        this.context = context;
        this.details = details;

    }

    /**
     * A public View function
     * On call, returns the view of the current object in the adapter after setting it up
     * based on the corresponding event details in the details array
     * @param position is the index of the current event details
     * @param view is the current view
     * @param parent is the parent viewgroup of the current view
     */
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