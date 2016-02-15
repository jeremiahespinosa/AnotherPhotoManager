package com.jeremiahespinosa.anotherphotomanager.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.jeremiahespinosa.anotherphotomanager.data.models.Album;
import com.jeremiahespinosa.anotherphotomanager.ui.fragments.image.ViewImagesFragment;

/**
 * Created by jespinosa on 2/14/16.
 */
public class ImagePagerAdapter extends FragmentStatePagerAdapter {
    private final int mSize;
    private Album album;

    public ImagePagerAdapter(FragmentManager fm, Album album) {
        super(fm);
        this.mSize = album.getPhotos().size();
        this.album = album;
    }

    @Override
    public int getCount() {
        return mSize;
    }

    @Override
    public Fragment getItem(int position) {

        return ViewImagesFragment.newInstance(album.getPhotos().get(position));
    }
}
