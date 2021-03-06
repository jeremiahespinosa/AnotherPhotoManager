package com.jeremiahespinosa.anotherphotomanager.presenter.photos;

import com.jeremiahespinosa.anotherphotomanager.ui.fragments.photos.PhotosView;

/**
 * Created by jespinosa on 2/15/16.
 */
public interface PhotosPresenter {
    void getImagesFromLocal();

    //set the callback to control ui elements
    void setHomeView(PhotosView photosView);
    void destroyView();
}
