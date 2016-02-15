package com.jeremiahespinosa.anotherphotomanager.presenter.photos;

import com.jeremiahespinosa.anotherphotomanager.data.models.Photo;
import com.jeremiahespinosa.anotherphotomanager.ui.fragments.photos.ImagesView;

/**
 * Created by jespinosa on 2/14/16.
 */
public interface ViewImagesPresenter {

    void setImagesView(ImagesView imagesView);
    void destroyView();
    void downloadImage(Photo photo);

}