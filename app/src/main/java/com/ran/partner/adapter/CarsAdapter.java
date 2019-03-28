package com.ran.partner.adapter;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ran.partner.CarsListFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CarsAdapter extends FragmentPagerAdapter {

    private Calendar date;

    public CarsAdapter(FragmentManager fm, Calendar date) {
        super(fm);
        this.date = date;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public Fragment getItem(int position) {
        return CarsListFragment.newInstance(new SimpleDateFormat("EEE, MMM d, yyyy").format(date.getTime()));
    }

    @Override
    public int getCount() {
        return 31;
    }
}