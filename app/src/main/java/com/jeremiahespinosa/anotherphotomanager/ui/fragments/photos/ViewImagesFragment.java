package com.jeremiahespinosa.anotherphotomanager.ui.fragments.photos;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.jeremiahespinosa.anotherphotomanager.R;
import com.jeremiahespinosa.anotherphotomanager.app.App;
import com.jeremiahespinosa.anotherphotomanager.app.BaseConstants;
import com.jeremiahespinosa.anotherphotomanager.data.models.Photo;
import com.jeremiahespinosa.anotherphotomanager.presenter.photos.ViewImagesPresenter;
import com.jeremiahespinosa.anotherphotomanager.presenter.photos.ViewImagesPresenterImpl;
import com.jeremiahespinosa.anotherphotomanager.ui.activities.ViewImageActivity;
import com.jeremiahespinosa.anotherphotomanager.ui.fragments.base.BaseFragment;

import java.io.File;

import butterknife.Bind;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by jespinosa on 2/14/16.
 */
public class ViewImagesFragment extends BaseFragment implements ImagesView {

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL = 2;
    @Bind(R.id.photoTitleTextView)
    TextView photoTitleTextView;

    @Bind(R.id.photoImageView)
    ImageView photoImageView;

    @Bind(R.id.fab)
    FloatingActionButton fab;

    ViewImagesPresenter viewImagesPresenter;
    private Photo photo;

    public static ViewImagesFragment newInstance(Photo photo) {
        ViewImagesFragment f = new ViewImagesFragment();
        Bundle b = new Bundle();
        b.putParcelable(BaseConstants.PHOTO_EXTRA, photo);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewImagesPresenter = new ViewImagesPresenterImpl();

        if(getArguments() != null){
            photo = (Photo)getArguments().getParcelable(BaseConstants.PHOTO_EXTRA);

            viewImagesPresenter.setImagesView(this);

            if(photo != null){

                final Window window = getActivity().getWindow();

                // clear FLAG_TRANSLUCENT_STATUS flag:
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

                // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

                photoTitleTextView.setText(photo.getTitle());

                Glide.with(getActivity())
                        .load(photo.getUrl())
                        .asBitmap()
                        .into(new BitmapImageViewTarget(photoImageView) {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                super.onResourceReady(resource, glideAnimation);
                                Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                                    @Override
                                    public void onGenerated(Palette palette) {
                                        Palette.Swatch vibrant = palette.getDarkMutedSwatch();

                                        if (vibrant != null) {
                                            window.setStatusBarColor(vibrant.getRgb());
                                            ((ViewImageActivity) getActivity()).setToolbarColor(vibrant.getRgb());
                                        }
                                    }
                                });
                            }
                        });

            }
        }
        else{
            fab.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.fab)
    public void downloadImage(){

        Timber.i("clicked fab");

        if (Build.VERSION.SDK_INT >= 23) {
            // Marshmallow+
            checkPermissionThenDownload();
        } else {
            // Pre-Marshmallow
            downloadImageTask();
        }
    }

    private void checkPermissionThenDownload(){
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                Snackbar.make(photoImageView, "Permission is needed to download images", Snackbar.LENGTH_LONG)
                        .setAction(R.string.positive_button, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL);
                            }
                        })
                        .show();

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL);
            }
        }
        else{
            Timber.i("pre task");
            downloadImageTask();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    //  task you need to do.
                    downloadImageTask();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    App.showShortToast("Unfortunately you wont be able to download any images");
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void downloadImageTask(){
        Timber.d("download task");
        viewImagesPresenter.downloadImage(photo);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_details, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewImagesPresenter.destroyView();
    }

    @Override
    public void onImageDownloaded(File resultFile) {
        if(resultFile != null && resultFile.exists()){
            App.showShortToast("File downloaded successfully");
        }
        else{
            App.showShortToast("We could not save your file");
        }
    }

    @Override
    public void downloadingError(String msg) {
        showDialog("Error", msg, true);
    }
}
