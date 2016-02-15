package com.jeremiahespinosa.anotherphotomanager.ui.fragments.photos;

import com.jeremiahespinosa.anotherphotomanager.ui.fragments.base.BaseView;

import java.io.File;

/**
 * Created by jespinosa on 2/14/16.
 */
public interface ImagesView extends BaseView{
    void onImageDownloaded(File resultFile);
    void downloadingError(String msg);
}
