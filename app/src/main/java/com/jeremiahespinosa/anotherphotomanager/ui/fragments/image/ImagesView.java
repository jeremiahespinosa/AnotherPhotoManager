package com.jeremiahespinosa.anotherphotomanager.ui.fragments.image;

import com.jeremiahespinosa.anotherphotomanager.ui.fragments.base.BaseView;

import java.io.File;

/**
 * Created by jespinosa on 2/14/16.
 */
public interface ImagesView extends BaseView{

    //callback to let ui know that download process is completed
    void onImageDownloaded(File resultFile);

    //error occurred
    void downloadingError(String msg);
}
