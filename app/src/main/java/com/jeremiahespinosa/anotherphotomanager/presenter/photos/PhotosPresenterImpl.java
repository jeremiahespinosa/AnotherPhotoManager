package com.jeremiahespinosa.anotherphotomanager.presenter.photos;

import android.os.Environment;

import com.jeremiahespinosa.anotherphotomanager.R;
import com.jeremiahespinosa.anotherphotomanager.app.App;
import com.jeremiahespinosa.anotherphotomanager.ui.fragments.photos.PhotosView;

import java.io.File;
import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by jespinosa on 2/15/16.
 */
public class PhotosPresenterImpl implements PhotosPresenter {
    PhotosView photosView;

    @Override
    public void getImagesFromLocal() {

        photosView.showProgressDialog();

        final File locAlbumDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), App.getStringById(R.string.app_name).replace(" ", ""));

        if (!locAlbumDir.mkdirs()) {
            Timber.e("Directory not created");
        }

        Observable.just(locAlbumDir).map(new Func1<File, ArrayList<String>>() {
            @Override
            public ArrayList<String> call(File album) {

                ArrayList<String> photoLocations = new ArrayList<String>();

                File[] files = album.listFiles();

                if(files != null && files.length > 0){
                    for (File file : files) {
                        if (file.isFile() && file.getName().toLowerCase().endsWith("jpg") && file.length() > 0) {
                            //looking for jpgs
                            photoLocations.add(file.getPath());
                        }
                    }
                }

                return photoLocations;
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new GetLocalImagesSubscriber());
    }

    @Override
    public void setHomeView(PhotosView photosView) {
        this.photosView = photosView;
    }

    @Override
    public void destroyView() {
        photosView = null;
    }

    private final class GetLocalImagesSubscriber extends Subscriber<ArrayList<String>>{
        @Override
        public void onCompleted() {
            Timber.i("completed task");
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();

            photosView.stopRefreshing();

            photosView.hideProgressDialog();

            photosView.showDialog("Error", "Error downloading the image", true);
        }

        @Override
        public void onNext(ArrayList<String> photos) {
            photosView.hideProgressDialog();

            //finished finding local photos
            photosView.onLocalPhotosLoaded(photos);
        }
    }
}
