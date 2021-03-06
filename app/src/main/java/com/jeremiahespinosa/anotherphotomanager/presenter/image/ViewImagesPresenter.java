package com.jeremiahespinosa.anotherphotomanager.presenter.image;

import com.jeremiahespinosa.anotherphotomanager.data.models.Photo;
import com.jeremiahespinosa.anotherphotomanager.ui.fragments.image.ImagesView;

/**
 * Created by jespinosa on 2/14/16.
 */
public interface ViewImagesPresenter {

    //set the callback to control ui elements
    void setImagesView(ImagesView imagesView);

    void destroyView();

    //function to download an image from the cloud
    void downloadImage(Photo photo);

}
