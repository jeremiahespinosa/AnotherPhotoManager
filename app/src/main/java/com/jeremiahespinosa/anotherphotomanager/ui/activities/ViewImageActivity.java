package com.jeremiahespinosa.anotherphotomanager.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.jeremiahespinosa.anotherphotomanager.R;
import com.jeremiahespinosa.anotherphotomanager.app.BaseConstants;
import com.jeremiahespinosa.anotherphotomanager.data.models.Album;
import com.jeremiahespinosa.anotherphotomanager.data.models.Photo;
import com.jeremiahespinosa.anotherphotomanager.ui.adapter.ImagePagerAdapter;
import butterknife.Bind;

/**
 * Created by jespinosa on 2/13/16.
 */
public class ViewImageActivity extends BaseActivity implements View.OnClickListener {

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

//            imagesViewPager.setOnSystemUiVisibilityChangeListener(
//                    new View.OnSystemUiVisibilityChangeListener() {
//                        @Override
//                        public void onSystemUiVisibilityChange(int vis) {
//                            if ((vis & View.SYSTEM_UI_FLAG_LOW_PROFILE) != 0) {
//                                getSupportActionBar().hide();
//                            } else {
//                                getSupportActionBar().show();
//                            }
//                        }
//                    });

            if (position != -1) {
                imagesViewPager.setCurrentItem(position);
            }
        }

        // Set up activity to go full screen
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void setToolbarColor(int color){
        mToolbar.setBackgroundColor(color);
    }

    @Override
    public void onClick(View v) {
//        final int vis = imagesViewPager.getSystemUiVisibility();
//        if ((vis & View.SYSTEM_UI_FLAG_LOW_PROFILE) != 0) {
//            imagesViewPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
//        } else {
//            imagesViewPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
//        }
    }
}
