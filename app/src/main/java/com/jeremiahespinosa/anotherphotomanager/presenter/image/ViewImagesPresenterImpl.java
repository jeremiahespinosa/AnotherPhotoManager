package com.jeremiahespinosa.anotherphotomanager.presenter.image;

import android.os.Environment;

import com.jeremiahespinosa.anotherphotomanager.R;
import com.jeremiahespinosa.anotherphotomanager.app.App;
import com.jeremiahespinosa.anotherphotomanager.data.models.Photo;
import com.jeremiahespinosa.anotherphotomanager.ui.fragments.image.ImagesView;
import com.jeremiahespinosa.anotherphotomanager.util.StorageUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;


/**
 * Created by jespinosa on 2/14/16.
 */
public class ViewImagesPresenterImpl implements ViewImagesPresenter{

    private ImagesView imagesView;

    @Override
    public void setImagesView(ImagesView imagesView) {
        this.imagesView = imagesView;
    }

    @Override
    public void destroyView() {
        imagesView = null;
    }

    @Override
    public void downloadImage(Photo photo) {
        imagesView.showProgressDialog();

         Observable.just(photo).map(new Func1<Photo, File>() {
            @Override
            public File call(Photo photo) {
                File locAlbum = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), App.getStringById(R.string.app_name).replace(" ", ""));

                if (!locAlbum.mkdirs()) {
                    Timber.e("Directory not created");
                }

                String fileName = new SimpleDateFormat("yyyyMMddhhmmss'.jpg'", Locale.US).format(new Date());

                File file = new File (locAlbum, fileName);

                try {
                    StorageUtils.downloadImageFromNetwork(photo.getUrl(), file);

                }catch(Exception e){
                    e.printStackTrace();
                }


                return file;
            }
            })
             .subscribeOn(Schedulers.io())
             .observeOn(AndroidSchedulers.mainThread())
             .subscribe(new DownloadImageSubscriber());
    }

    private final class DownloadImageSubscriber extends Subscriber<File>{

        @Override
        public void onCompleted() {
            Timber.i("completed task");
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();

            imagesView.hideProgressDialog();
            imagesView.downloadingError("Error downloading the image");
        }

        @Override
        public void onNext(File file) {
            imagesView.hideProgressDialog();

            imagesView.onImageDownloaded(file);
        }
    }

}
