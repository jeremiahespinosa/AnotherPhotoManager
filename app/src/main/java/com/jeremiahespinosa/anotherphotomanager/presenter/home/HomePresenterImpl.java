package com.jeremiahespinosa.anotherphotomanager.presenter.home;

import android.os.Environment;

import com.jeremiahespinosa.anotherphotomanager.R;
import com.jeremiahespinosa.anotherphotomanager.app.App;
import com.jeremiahespinosa.anotherphotomanager.data.api.PhotoEntity;
import com.jeremiahespinosa.anotherphotomanager.data.models.Album;
import com.jeremiahespinosa.anotherphotomanager.data.models.Photo;
import com.jeremiahespinosa.anotherphotomanager.ui.fragments.home.HomeView;
import com.jeremiahespinosa.anotherphotomanager.util.network.ApiService;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by jespinosa on 2/13/16.
 */
public class HomePresenterImpl implements HomePresenter  {

    private HomeView home;

    @Override
    public void getImagesFromLocal() {

        final File locAlbum = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), App.getStringById(R.string.app_name).replace(" ", ""));

        if (!locAlbum.mkdirs()) {
            Timber.e("Directory not created");
        }

        Observable.just(locAlbum).map(new Func1<File, ArrayList<String>>() {
                  @Override
                  public ArrayList<String> call(File album) {

                      ArrayList<String> photoLocations = new ArrayList<String>();

                      File[] files = album.listFiles();

                      for (File file : files) {
                          if (file.isFile() && file.getName().toLowerCase().endsWith("jpg")) {
                              //looking for jpgs
                              photoLocations.add(file.getPath());
                          }
                      }
                      return photoLocations;
                  }
          })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<ArrayList<String>>() {
            @Override
            public void onCompleted() {
                Timber.i("completed task");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();

                home.hideProgressDialog();
                home.showError("Error downloading the image");
            }

            @Override
            public void onNext(ArrayList<String> photos) {
                home.hideProgressDialog();

//                imagesView.onImageDownloaded(file);
            }
        });

    }

    @Override
    public void getImagesFromCloud() {
        home.showProgressDialog();

        ApiService.Implementation.call()
                .getCloudPhotos()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(new Subscriber<List<PhotoEntity>>() {
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

                        LinkedHashMap<Integer, ArrayList<Photo>> linkedHashMap = new LinkedHashMap<>();

                        ArrayList<Album> albums = new ArrayList<>();

                        if (photoResponse != null && photoResponse.size() > 0) {

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
                });
    }

    @Override
    public void setHomeView(HomeView homeview) {
        home = homeview;
    }

    @Override
    public void destroyView() {
        home = null;
    }
}
