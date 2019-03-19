package com.ran.partner.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ran.partner.PartnersFragment;
import com.ran.partner.PlacesFragment;
import com.ran.partner.UsersFragment;

public class ConnectionsAdapter extends FragmentPagerAdapter {

    private int totalTabs;

    public ConnectionsAdapter(FragmentManager fm, int totalTabs) {
        super(fm);
        this.totalTabs = totalTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new UsersFragment();
            case 1:
                return new PartnersFragment();
            case 2:
                return new PlacesFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}