package com.jeremiahespinosa.anotherphotomanager.presenter.home;

import com.jeremiahespinosa.anotherphotomanager.ui.fragments.home.HomeView;

/**
 * Created by jespinosa on 2/13/16.
 */
public interface HomePresenter {

    //function to get the images from the api call
    void getImagesFromCloud();

    //set the callback to control ui elements
    void setHomeView(HomeView homeview);
    void destroyView();
}
