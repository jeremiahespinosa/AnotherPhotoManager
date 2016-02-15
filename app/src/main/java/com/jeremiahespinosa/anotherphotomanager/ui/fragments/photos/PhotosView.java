package com.jeremiahespinosa.anotherphotomanager.ui.fragments.photos;

import com.jeremiahespinosa.anotherphotomanager.data.models.Photo;
import com.jeremiahespinosa.anotherphotomanager.ui.fragments.base.BaseView;

import java.util.ArrayList;

/**
 * Created by jespinosa on 2/14/16.
 */
public interface PhotosView extends BaseView {
    void onPhotoClicked(Photo photo, int position);
    void onLocalPhotosLoaded(ArrayList<String> photosLocs);
    void stopRefreshing();
}
