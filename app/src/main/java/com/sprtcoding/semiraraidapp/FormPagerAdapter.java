package com.sprtcoding.semiraraidapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class FormPagerAdapter extends FragmentPagerAdapter {
    public FormPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new StepOneFragment();
            case 1:
                return new StepTwoFragment();
            case 2:
                return new StepThreeFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Step 1";
            case 1:
                return "Step 2";
            case 2:
                return "Step 3";
            default:
                return null;
        }
    }
}
