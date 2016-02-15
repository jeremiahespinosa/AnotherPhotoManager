package com.jeremiahespinosa.anotherphotomanager.ui.fragments.home;

import com.jeremiahespinosa.anotherphotomanager.data.models.Album;
import com.jeremiahespinosa.anotherphotomanager.ui.fragments.base.BaseView;

import java.util.ArrayList;

/**
 * Created by jespinosa on 2/13/16.
 */
public interface HomeView extends BaseView {
    void showError(String errorMessage);

    //callback when albums are done being processed based on api call
    void viewLoaded(ArrayList<Album> albums);

    //callback for when user taps an album to view the photos inside
    void onAlbumSelected(Album album);

}
