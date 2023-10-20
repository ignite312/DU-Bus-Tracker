package com.octagon.octagondu;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

public class AdapterProfilePic extends ArrayAdapter<Integer> {
    private Context context;
    private List<Integer> imageResourceIds;

    public AdapterProfilePic(Context context, int resource, List<Integer> imageResourceIds) {
        super(context, resource, imageResourceIds);
        this.context = context;
        this.imageResourceIds = imageResourceIds;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(imageResourceIds.get(position));
        imageView.setLayoutParams(new ViewGroup.LayoutParams(100, 100)); // Adjust the size as needed
        return imageView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}

