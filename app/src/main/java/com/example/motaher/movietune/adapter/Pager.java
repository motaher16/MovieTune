package com.example.motaher.movietune.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.example.motaher.movietune.fragment.NewRelease;
import com.example.motaher.movietune.fragment.TopRated;
import com.example.motaher.movietune.fragment.Upcoming;

/**
 * Created by motaher on 5/4/2017.
 */

public class Pager extends FragmentStatePagerAdapter {

    int tabCount;

    public Pager(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount= tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                NewRelease newrelease = new NewRelease();
                return newrelease;
            case 1:
                TopRated upcoming = new TopRated();
                return upcoming;
            case 2:

                Upcoming toprated = new Upcoming();
                return toprated;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}