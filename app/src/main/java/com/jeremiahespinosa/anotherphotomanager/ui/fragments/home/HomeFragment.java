package com.jeremiahespinosa.anotherphotomanager.ui.fragments.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeremiahespinosa.anotherphotomanager.R;

import com.jeremiahespinosa.anotherphotomanager.app.App;
import com.jeremiahespinosa.anotherphotomanager.app.BaseConstants;
import com.jeremiahespinosa.anotherphotomanager.data.models.Album;
import com.jeremiahespinosa.anotherphotomanager.presenter.home.HomePresenter;
import com.jeremiahespinosa.anotherphotomanager.presenter.home.HomePresenterImpl;
import com.jeremiahespinosa.anotherphotomanager.ui.activities.PhotosActivity;
import com.jeremiahespinosa.anotherphotomanager.ui.adapter.AlbumsAdapter;
import com.jeremiahespinosa.anotherphotomanager.ui.adapter.EmptyPhotosAdapter;
import com.jeremiahespinosa.anotherphotomanager.ui.fragments.base.BaseFragment;
import com.jeremiahespinosa.anotherphotomanager.util.SystemUtil;

import java.util.ArrayList;
import butterknife.Bind;
import timber.log.Timber;

/**
 * Created by jespinosa on 2/13/16.
 */
public class HomeFragment extends BaseFragment implements HomeView{

    @Bind(R.id.main_swipe_refresh_layout)
    SwipeRefreshLayout main_swipe_refresh_layout;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    public static HomeFragment newInstance(String fragmentTitle) {
        HomeFragment f = new HomeFragment();
        Bundle b = new Bundle();
        b.putString(BaseConstants.EXTRA_FRAGMENT_TITLE, fragmentTitle);
        f.setArguments(b);
        return f;
    }

    HomePresenter homePresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homePresenter = new HomePresenterImpl();
        homePresenter.setHomeView(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        //check arguments to see what type of loading we need to do

        //if downloads then we are going to have to look at file structure to see how to build our ui

        //if Home then we are going to load from the cloud

        if(getArguments() != null && getArguments().containsKey(BaseConstants.EXTRA_FRAGMENT_TITLE)){
            String fragmentType = getArguments().getString(BaseConstants.EXTRA_FRAGMENT_TITLE);

            if(fragmentType != null && fragmentType.equals(getString(R.string.homeFragmentTitle)) && App.isOnline()){
                Timber.i("loading from cloud");
                //load from cloud
                startLoadingFromCloud();
            }
            else{
                showEmptyLayout();
            }
        }

        main_swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                startLoadingFromCloud();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        homePresenter.destroyView();
    }

    private void startLoadingFromCloud(){
        homePresenter.getImagesFromCloud();
    }

    private void showEmptyLayout(){
        stopRefreshing();
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        EmptyPhotosAdapter emptyPhotosAdapter = new EmptyPhotosAdapter();
        recyclerView.setAdapter(emptyPhotosAdapter);
    }

    @Override
    public void showError(String errorMessage) {
        showDialog("Error", errorMessage, true);
        stopRefreshing();
    }

    private void stopRefreshing(){
        main_swipe_refresh_layout.setRefreshing(false);
    }

    @Override
    public void viewLoaded(ArrayList<Album> albums) {
        stopRefreshing();
        if(albums != null && albums.size() > 0){
            AlbumsAdapter albumsAdapter = new AlbumsAdapter(albums, this);
            recyclerView.setAdapter(albumsAdapter);
        }
        else{
            EmptyPhotosAdapter emptyPhotosAdapter = new EmptyPhotosAdapter();
            recyclerView.setAdapter(emptyPhotosAdapter);
        }
    }

    @Override
    public void onAlbumSelected(Album album) {
        //go to photos view
        Intent intent = PhotosActivity.intentInstance(getActivity(), album);
        SystemUtil.startActivity(getActivity(), intent, false);
    }
}
