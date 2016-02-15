package com.jeremiahespinosa.anotherphotomanager.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import android.widget.FrameLayout;

import com.jeremiahespinosa.anotherphotomanager.R;
import com.jeremiahespinosa.anotherphotomanager.app.BaseConstants;
import com.jeremiahespinosa.anotherphotomanager.data.models.Album;
import com.jeremiahespinosa.anotherphotomanager.ui.fragments.photos.PhotosFragment;

import butterknife.Bind;
import timber.log.Timber;

/**
 * Created by jespinosa on 2/14/16.
 */
public class PhotosActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.container)
    FrameLayout container;

    public static Intent intentInstance(Context context, Album album){
        Timber.i("intentInstance() called with: " + " album = [" + album + "]");
        Intent intent = new Intent(context, PhotosActivity.class);
        intent.putExtra(BaseConstants.ALBUM_EXTRA, album);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_photos);
        setSupportActionBar(mToolbar);

        Album album = (Album)getIntent().getExtras().getParcelable(BaseConstants.ALBUM_EXTRA);

        if(album != null){
            setTitle(getString(R.string.album_number_formatted, album.getId()));
        }

        if(savedInstanceState == null){
            hostFragment();
        }

    }

    protected void hostFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container,
                PhotosFragment.newInstance((Album)getIntent().getExtras().getParcelable(BaseConstants.ALBUM_EXTRA)),
                PhotosFragment.class.getName());
        ft.commit();
    }
}
