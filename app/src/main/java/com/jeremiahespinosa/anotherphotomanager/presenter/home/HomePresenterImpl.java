package com.jeremiahespinosa.anotherphotomanager.presenter.home;


import com.jeremiahespinosa.anotherphotomanager.data.api.PhotoEntity;
import com.jeremiahespinosa.anotherphotomanager.data.models.Album;
import com.jeremiahespinosa.anotherphotomanager.data.models.Photo;
import com.jeremiahespinosa.anotherphotomanager.ui.fragments.home.HomeView;
import com.jeremiahespinosa.anotherphotomanager.util.network.ApiService;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by jespinosa on 2/13/16.
 */
public class HomePresenterImpl implements HomePresenter  {

    private HomeView home;

    @Override
    public void getImagesFromCloud() {
        home.showProgressDialog();

        ApiService.Implementation.call()
                .getCloudPhotos()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new GetCloudImagesSubscriber());
    }

    @Override
    public void setHomeView(HomeView homeview) {
        home = homeview;
    }

    @Override
    public void destroyView() {
        home = null;
    }

    private final class GetCloudImagesSubscriber extends Subscriber<List<PhotoEntity>>{
        @Override
        public void onCompleted() {
            Timber.i("completed getting photos");
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            home.hideProgressDialog();
            home.showError("Error loading images from the cloud");
        }

        @Override
        public void onNext(List<PhotoEntity> photoResponse) {

            if (photoResponse != null && photoResponse.size() > 0) {
                LinkedHashMap<Integer, ArrayList<Photo>> linkedHashMap = new LinkedHashMap<>();

                ArrayList<Album> albums = new ArrayList<>();

                for(PhotoEntity p : photoResponse){

                    if(linkedHashMap.containsKey(p.getAlbumId())){
                        //album already in the mapping
                        //get the value and add a new photo to it
                        linkedHashMap.get(p.getAlbumId()).add(new Photo(p.getId(), p.getTitle(), p.getThumbnailUrl(), p.getUrl()));
                    }
                    else{
                        //album is not in the mapping
                        ArrayList<Photo> photos = new ArrayList<>();
                        photos.add(new Photo(p.getId(), p.getTitle(), p.getThumbnailUrl(), p.getUrl()));
                        linkedHashMap.put(p.getAlbumId(), photos);
                    }
                }

                //converting to list of albums to simplify adapter usage
                for(Map.Entry<Integer, ArrayList<Photo>> entry: linkedHashMap.entrySet()){
                    albums.add(new Album(entry.getKey(), entry.getValue()));
                }

                home.viewLoaded(albums);
            }
            else {
                home.viewLoaded(null);
            }

            home.hideProgressDialog();
        }
    }
}
