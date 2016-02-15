package com.jeremiahespinosa.anotherphotomanager.ui.fragments.photos;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeremiahespinosa.anotherphotomanager.R;
import com.jeremiahespinosa.anotherphotomanager.app.BaseConstants;
import com.jeremiahespinosa.anotherphotomanager.data.models.Album;
import com.jeremiahespinosa.anotherphotomanager.data.models.Photo;
import com.jeremiahespinosa.anotherphotomanager.presenter.photos.PhotosPresenter;
import com.jeremiahespinosa.anotherphotomanager.presenter.photos.PhotosPresenterImpl;
import com.jeremiahespinosa.anotherphotomanager.ui.activities.ViewImageActivity;
import com.jeremiahespinosa.anotherphotomanager.ui.adapter.EmptyPhotosAdapter;
import com.jeremiahespinosa.anotherphotomanager.ui.adapter.PhotosAdapter;
import com.jeremiahespinosa.anotherphotomanager.ui.fragments.base.BaseFragment;
import com.jeremiahespinosa.anotherphotomanager.ui.widgets.GridItemDividerDecoration;
import com.jeremiahespinosa.anotherphotomanager.util.SystemUtil;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by jespinosa on 2/14/16.
 */
public class PhotosFragment extends BaseFragment implements PhotosView{

    @Bind(R.id.main_swipe_refresh_layout)
    SwipeRefreshLayout main_swipe_refresh_layout;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    private Album album;

    boolean loadFromLocal = false;

    private PhotosPresenter photosPresenter;

    public static PhotosFragment newInstance(Album album) {
        PhotosFragment f = new PhotosFragment();
        Bundle b = new Bundle();
        b.putParcelable(BaseConstants.ALBUM_EXTRA, album);
        f.setArguments(b);
        return f;
    }

    //trying to load photos from local storage
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
        photosPresenter = new PhotosPresenterImpl();
        photosPresenter.setHomeView(this);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        if(getArguments() != null && getArguments().containsKey(BaseConstants.ALBUM_EXTRA)){
            album = getArguments().getParcelable(BaseConstants.ALBUM_EXTRA);

            //disable swipe to refresh since loading based on urls passed in
            main_swipe_refresh_layout.setEnabled(false);

            if(album != null && album.getPhotos() != null && album.getPhotos().size() > 0 ){
                PhotosAdapter adapter = new PhotosAdapter(album.getPhotos(), this);
                recyclerView.setAdapter(adapter);
                recyclerView.setHasFixedSize(true);
                recyclerView.addItemDecoration(new GridItemDividerDecoration());
            }
            else{
                //no photos available
                showEmptyLayout();
            }
        }
        else{
            if(getArguments() != null && getArguments().containsKey(BaseConstants.PHOTO_LOCS_EXTRA)){
                loadFromLocal = getArguments().getBoolean(BaseConstants.PHOTO_LOCS_EXTRA, false);

                if(loadFromLocal){
                    loadFromLocal();
                }
            }
            //load from local
        }

        main_swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //photos came from main fragment not from network call
                if (loadFromLocal)
                    loadFromLocal();
            }
        });

    }

    private void showEmptyLayout(){
        stopRefreshing();

        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        EmptyPhotosAdapter emptyPhotosAdapter = new EmptyPhotosAdapter();
        recyclerView.setAdapter(emptyPhotosAdapter);
    }

    @Override
    public void stopRefreshing(){
        main_swipe_refresh_layout.setRefreshing(false);
    }

    private void loadFromLocal(){
        photosPresenter.getImagesFromLocal();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        photosPresenter.destroyView();
    }

    @Override
    public void onPhotoClicked(Photo photo, int position) {
            Intent intent = ViewImageActivity.intentInstance(getActivity(), album, position);
            SystemUtil.startActivity(getActivity(), intent, false);
    }

    @Override
    public void onLocalPhotosLoaded(ArrayList<String> photos) {

        stopRefreshing();

        if(photos != null && photos.size() >0){
            album = new Album();
            album.setId(1);

            ArrayList<Photo> photosList = new ArrayList<Photo>();

            for(String p : photos){
                photosList.add(new Photo(1, "", p, p));
            }

            album.setPhotos(photosList);

            PhotosAdapter adapter = new PhotosAdapter(album.getPhotos(), PhotosFragment.this);

            recyclerView.setAdapter(adapter);
            recyclerView.setHasFixedSize(true);
            recyclerView.addItemDecoration(new GridItemDividerDecoration());
        }
        else{
            showEmptyLayout();
        }
    }
}
