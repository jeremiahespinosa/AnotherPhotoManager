package com.jeremiahespinosa.anotherphotomanager.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeremiahespinosa.anotherphotomanager.R;
import com.jeremiahespinosa.anotherphotomanager.data.api.PhotoResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jespinosa on 2/13/16.
 */
public class EmptyPhotosAdapter extends RecyclerView.Adapter<EmptyPhotosAdapter.ViewHolder> {
    private List<PhotoResponse.DataEntity> mPhotos;

    public EmptyPhotosAdapter() {
        mPhotos = new ArrayList<>();
        mPhotos.add(new PhotoResponse.DataEntity());    //adding empty photo response
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_empty_photos_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
