package com.octagon.octagondu;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

public class FragmentHome extends Fragment {
    private ImageView singleImageView;
    private boolean isImage1Shown = true;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_home, container, false);
        singleImageView = view.findViewById(R.id.click1);

        singleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isImage1Shown) {
                    singleImageView.setImageResource(R.drawable.arrow_upward);
                } else {
                    singleImageView.setImageResource(R.drawable.arrow_upward_green);
                }
                isImage1Shown = !isImage1Shown;
            }
        });

        return view;
    }
}