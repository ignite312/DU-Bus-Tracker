package com.octagon.octagondu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class TabNoticeFragment extends Fragment {
    String busName = "Khonika", flag = "SC";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_notice_fragment, container, false);
        /*Got the Bus Name and Flag*/
        Bundle args = getArguments();
        if (args != null) {
            busName = args.getString("busName");
            flag = args.getString("flag");
        }
        return rootView;
    }
    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void showCustomToast(String message) {
        View layout = getLayoutInflater().inflate(R.layout.custom_toast, (ViewGroup) getView().findViewById(R.id.toast_layout_root));

        TextView text = layout.findViewById(R.id.custom_toast_text);
        text.setText(message);

        Toast toast = new Toast(getContext());
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 160); // Adjust margins as needed
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}
