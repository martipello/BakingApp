package com.sealstudios.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.sealstudios.bakinapp.DetailPagingFragment;
import com.sealstudios.objects.Step;

import java.util.ArrayList;

public class CustomFragmentAdapter extends FragmentStatePagerAdapter {

    private Context mContext;
    private ArrayList<Step> steps;

    public CustomFragmentAdapter(FragmentManager fm, ArrayList<Step> steps) {
        super(fm);
        this.steps = steps;
    }

    @Override
    public Fragment getItem(int position) {
        return DetailPagingFragment.init(position, steps.get(position));
    }

    @Override
    public int getCount() {
        return steps.size();
    }
}
