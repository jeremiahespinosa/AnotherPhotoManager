package com.jeremiahespinosa.anotherphotomanager.ui.fragments.photos;

import com.jeremiahespinosa.anotherphotomanager.data.models.Photo;
import com.jeremiahespinosa.anotherphotomanager.ui.fragments.base.BaseView;

import java.util.ArrayList;

/**
 * Created by jespinosa on 2/14/16.
 */
public interface PhotosView extends BaseView {

    //callback when photo is clicked in grid view
    void onPhotoClicked(Photo photo, int position);

    //callback when photos are done being found
    void onLocalPhotosLoaded(ArrayList<String> photosLocs);

    //update the swipe refresh ui
    void stopRefreshing();
}
