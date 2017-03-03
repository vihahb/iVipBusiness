package com.xtel.ivipbusiness.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.xtel.ivipbusiness.model.entity.Gallery;
import com.xtel.ivipbusiness.view.fragment.ItemImageFragment;

import java.util.ArrayList;

/**
 * Created by Vulcl on 3/3/2017
 */

public class ShowImageAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Gallery> arrayList;

    public ShowImageAdapter(FragmentManager fm, ArrayList<Gallery> arrayList) {
        super(fm);
        this.arrayList = arrayList;
    }

    @Override
    public Fragment getItem(int position) {
        return ItemImageFragment.newInstance(arrayList.get(position).getUrl());
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }
}
