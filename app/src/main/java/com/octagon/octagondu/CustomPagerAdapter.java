package com.octagon.octagondu;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class CustomPagerAdapter extends FragmentPagerAdapter {
    public CustomPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int pos){
        if(pos == 1){
            Fragment fragment = new AboutFragment();
            return fragment;
        }
        else{
            Fragment fragment = new ContributionFragment();
            return fragment;
        }
    }

    @Override
    public int getCount(){
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int pos){
        if(pos == 1){
            return "About";
        }
        else {
            return "Contributions";
        }
    }
}
