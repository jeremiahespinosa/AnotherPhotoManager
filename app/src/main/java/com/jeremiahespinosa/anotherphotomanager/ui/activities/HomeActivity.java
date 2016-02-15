package com.jeremiahespinosa.anotherphotomanager.ui.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.jeremiahespinosa.anotherphotomanager.R;
import com.jeremiahespinosa.anotherphotomanager.ui.adapter.HomeAdapter;
import com.jeremiahespinosa.anotherphotomanager.ui.fragments.home.HomeFragment;
import com.jeremiahespinosa.anotherphotomanager.ui.fragments.photos.PhotosFragment;

import butterknife.Bind;

/**
 * Created by jespinosa on 2/13/16.
 */
public class HomeActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.tabs)
    TabLayout tabLayout;

    @Bind(R.id.viewPagerContainer)
    ViewPager viewPager;

    private ViewPager.SimpleOnPageChangeListener mSimpleOnPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            tabLayout.getTabAt(position).select();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        setSupportActionBar(mToolbar);

        setupViewPager();
    }

    private void setupViewPager(){
        HomeAdapter homeAdapter = new HomeAdapter(getSupportFragmentManager());

        homeAdapter.addFragment(HomeFragment.newInstance(getString(R.string.homeFragmentTitle)), getString(R.string.homeFragmentTitle));
        homeAdapter.addFragment(PhotosFragment.newInstance(true), getString(R.string.downloadsFragmentTitle));

        viewPager.setAdapter(homeAdapter);
        viewPager.addOnPageChangeListener(mSimpleOnPageChangeListener);

        setupTabLayout(tabLayout);
    }

    private void setupTabLayout(final TabLayout tabLayout){
        tabLayout.setupWithViewPager(viewPager);
    }
}
