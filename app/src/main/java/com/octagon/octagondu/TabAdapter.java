//package com.octagon.octagondu;
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentActivity;
//import androidx.viewpager2.adapter.FragmentStateAdapter;
//
//public class TabAdapter extends FragmentStateAdapter {
//    public TabAdapter(@NonNull FragmentActivity fragmentActivity) {
//        super(fragmentActivity);
//    }
//
//    @NonNull
//    @Override
//    public Fragment createFragment(int position) {
//        switch (position){
//            case 1:
//                return new TabUpScFragment();
//            case 2:
//                return new TabDownScFragment();
//            default:
//                return new TabNoticeFragment();
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return 3;
//    }
//}
package com.octagon.octagondu;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TabAdapter extends FragmentStateAdapter {
    private String busName;
    private String flag;

    public TabAdapter(@NonNull FragmentActivity fragmentActivity, String busName, String flag) {
        super(fragmentActivity);
        this.busName = busName;
        this.flag = flag;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        switch (position) {
            case 1:
                fragment = new TabUpScFragment();
                break;
            case 2:
                fragment = new TabDownScFragment();
                break;
            default:
                fragment = new TabNoticeFragment();
                break;
        }

        // Set the arguments for each fragment
        Bundle args = new Bundle();
        args.putString("BUSNAME", busName);
        args.putString("FLAG", flag);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
