package com.jeremiahespinosa.anotherphotomanager.ui.fragments.photos;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeremiahespinosa.anotherphotomanager.R;
import com.jeremiahespinosa.anotherphotomanager.app.App;
import com.jeremiahespinosa.anotherphotomanager.app.BaseConstants;
import com.jeremiahespinosa.anotherphotomanager.data.models.Album;
import com.jeremiahespinosa.anotherphotomanager.data.models.Photo;
import com.jeremiahespinosa.anotherphotomanager.ui.activities.PhotosActivity;
import com.jeremiahespinosa.anotherphotomanager.ui.activities.ViewImageActivity;
import com.jeremiahespinosa.anotherphotomanager.ui.adapter.EmptyPhotosAdapter;
import com.jeremiahespinosa.anotherphotomanager.ui.adapter.LocalPhotosAdapter;
import com.jeremiahespinosa.anotherphotomanager.ui.adapter.PhotosAdapter;
import com.jeremiahespinosa.anotherphotomanager.ui.fragments.base.BaseFragment;
import com.jeremiahespinosa.anotherphotomanager.ui.widgets.GridItemDividerDecoration;
import com.jeremiahespinosa.anotherphotomanager.util.SystemUtil;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by jespinosa on 2/14/16.
 */
public class PhotosFragment extends BaseFragment implements PhotosView{

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    private Album album;
    private Album localAlbum;
    boolean loadFromLocal = false;

    public static PhotosFragment newInstance(Album album) {
        PhotosFragment f = new PhotosFragment();
        Bundle b = new Bundle();
        b.putParcelable(BaseConstants.ALBUM_EXTRA, album);
        f.setArguments(b);
        return f;
    }

    public static PhotosFragment newInstance(boolean shouldLoadFromLocal) {

        PhotosFragment f = new PhotosFragment();
        Bundle b = new Bundle();
        b.putBoolean(BaseConstants.PHOTO_LOCS_EXTRA, shouldLoadFromLocal);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        if(getArguments() != null && getArguments().containsKey(BaseConstants.ALBUM_EXTRA)){
            album = getArguments().getParcelable(BaseConstants.ALBUM_EXTRA);

            if(album != null && album.getPhotos() != null && album.getPhotos().size() > 0 ){
                PhotosAdapter adapter = new PhotosAdapter(album.getPhotos(), this);
                recyclerView.setAdapter(adapter);
                recyclerView.setHasFixedSize(true);
                recyclerView.addItemDecoration(new GridItemDividerDecoration());
            }
            else{
                //update the recylcerview
                showEmptyLayout();
            }
        }
        else{
            if(getArguments() != null && getArguments().containsKey(BaseConstants.PHOTO_LOCS_EXTRA)){
                loadFromLocal = getArguments().getBoolean(BaseConstants.PHOTO_LOCS_EXTRA, false);

                if(loadFromLocal){
                    Timber.i("loading from local");
                    loadFromLocal();
                }
            }
            //load from local
        }

    }

    private void showEmptyLayout(){
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        EmptyPhotosAdapter emptyPhotosAdapter = new EmptyPhotosAdapter();
        recyclerView.setAdapter(emptyPhotosAdapter);
    }

    private void loadFromLocal(){

        showProgressDialog();

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
                    if (file.isFile() && file.getName().toLowerCase().endsWith("jpg") && file.length() > 0) {
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

                hideProgressDialog();

                showDialog("Error","Error downloading the image", true);
            }

            @Override
            public void onNext(ArrayList<String> photos) {
                hideProgressDialog();

                if(photos != null && photos.size() >0){
                    localAlbum = new Album();
                    localAlbum.setId(1);

                    ArrayList<Photo> photosList = new ArrayList<Photo>();

                    for(String p : photos){
                        photosList.add(new Photo(1, "", p, p));

                    }

                    localAlbum.setPhotos(photosList);

                    PhotosAdapter adapter = new PhotosAdapter(localAlbum.getPhotos(), PhotosFragment.this);

                    recyclerView.setAdapter(adapter);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.addItemDecoration(new GridItemDividerDecoration());
                }
                else{
                    showEmptyLayout();
                }
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        homePresenter.destroyView();
    }

    @Override
    public void onPhotoClicked(Photo photo, int position) {
        if(loadFromLocal){
            Intent intent = ViewImageActivity.intentInstance(getActivity(), localAlbum, position);
            SystemUtil.startActivity(getActivity(), intent, false);
        }
        else{
            Intent intent = ViewImageActivity.intentInstance(getActivity(), album, position);
            SystemUtil.startActivity(getActivity(), intent, false);
        }

    }

    @Override
    public void onPhotoClicked(String photo, int position) {

        Intent intent = ViewImageActivity.intentInstance(getActivity(), localAlbum, position);
        SystemUtil.startActivity(getActivity(), intent, false);
    }
}
