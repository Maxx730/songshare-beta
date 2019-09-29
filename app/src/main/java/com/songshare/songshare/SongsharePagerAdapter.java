package com.songshare.songshare;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

//Class that handles the displaying of the seperate app fragments, i.e settings/users and shared.
public class SongsharePagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 3;

    public SongsharePagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return new FeedFragment();
            case 1:
                return new UsersFragment();
            case 2:
                return new SettingsFragment();
            default :
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
}
