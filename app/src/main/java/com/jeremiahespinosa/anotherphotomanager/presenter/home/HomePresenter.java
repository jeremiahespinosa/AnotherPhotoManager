package com.jeremiahespinosa.anotherphotomanager.presenter.home;

import com.jeremiahespinosa.anotherphotomanager.ui.fragments.home.HomeView;

/**
 * Created by jespinosa on 2/13/16.
 */
public interface HomePresenter {
    void getImagesFromLocal();
    void getImagesFromCloud();
    void setHomeView(HomeView homeview);

    void destroyView();
}
