package com.brogrammers.tutionbd.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.brogrammers.tutionbd.views.fragments.AllFindTuitionFragment;
import com.brogrammers.tutionbd.views.fragments.NearestFindTuition;

public class TabAdapter extends FragmentStatePagerAdapter {
    private String[] tabTittle = {"All","Nearest"};
    public TabAdapter(@NonNull FragmentManager fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position==0) return new AllFindTuitionFragment();
        if (position==1) return new NearestFindTuition();

        return null;
    }

    @Override
    public int getCount() {
        return tabTittle.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTittle[position];
    }
}
