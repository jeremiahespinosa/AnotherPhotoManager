package com.jeremiahespinosa.anotherphotomanager.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jespinosa on 2/13/16.
 */
public class HomeAdapter extends FragmentPagerAdapter {
    //holds the fragments on the home screen

    private final List<Fragment> fragments = new ArrayList<>();
    private final List<String> fragmentTitles = new ArrayList<>();

    public HomeAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment f, String title){
        fragments.add(f);
        fragmentTitles.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitles.get(position);
    }
}
