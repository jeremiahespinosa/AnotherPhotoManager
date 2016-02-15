package com.jeremiahespinosa.anotherphotomanager.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.jeremiahespinosa.anotherphotomanager.R;
import com.jeremiahespinosa.anotherphotomanager.app.BaseConstants;
import com.jeremiahespinosa.anotherphotomanager.data.models.Album;
import com.jeremiahespinosa.anotherphotomanager.ui.adapter.ImagePagerAdapter;
import butterknife.Bind;

/**
 * Created by jespinosa on 2/13/16.
 */
public class ViewImageActivity extends BaseActivity{

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.imagesViewPager)
    ViewPager imagesViewPager;

    public static Intent intentInstance(Context context, Album album, int position){
        Intent intent = new Intent(context, ViewImageActivity.class);
        intent.putExtra(BaseConstants.ALBUM_EXTRA, album);
        intent.putExtra(BaseConstants.POSITION_IN_ALBUM_EXTRA, position);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_image_details);

        setSupportActionBar(mToolbar);

        if(getIntent().getExtras() != null && getIntent().getExtras().getParcelable(BaseConstants.ALBUM_EXTRA) != null){

            int position = getIntent().getExtras().getInt(BaseConstants.POSITION_IN_ALBUM_EXTRA, -1);

            Album album = (Album)getIntent().getExtras().getParcelable(BaseConstants.ALBUM_EXTRA);

            setTitle(getString(R.string.album_number_formatted, album.getId()));

            ImagePagerAdapter mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), album);
            imagesViewPager.setAdapter(mAdapter);
            imagesViewPager.setPageMargin((int) getResources().getDimension(R.dimen.activity_horizontal_margin));
            imagesViewPager.setOffscreenPageLimit(2);

            if (position != -1) {
                imagesViewPager.setCurrentItem(position);
            }
        }

    }

    public void setToolbarColor(int color){
        mToolbar.setBackgroundColor(color);
    }
}
