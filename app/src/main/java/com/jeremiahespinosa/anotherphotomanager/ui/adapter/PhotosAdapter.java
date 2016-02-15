package com.jeremiahespinosa.anotherphotomanager.ui.adapter;

import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.jeremiahespinosa.anotherphotomanager.R;
import com.jeremiahespinosa.anotherphotomanager.data.models.Photo;
import com.jeremiahespinosa.anotherphotomanager.ui.fragments.photos.PhotosView;

import java.util.ArrayList;

/**
 * Created by jespinosa on 2/14/16.
 */
public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ViewHolder> {

    private ArrayList<Photo> photos = new ArrayList<>();
    private PhotosView photosView;

    public PhotosAdapter(ArrayList<Photo> photos, PhotosView photosView) {
        this.photos = photos;
        this.photosView = photosView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_photo_item, parent, false);
        return new ViewHolder(v, photosView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        Photo photo = photos.get(position);

        if(photo.getTitle().isEmpty())
            viewHolder.imageTitle.setVisibility(View.GONE);
        else
            viewHolder.imageTitle.setText(photo.getTitle());

        Glide.with(viewHolder.imagePreview.getContext())
                .load(photo.getThumbnailUrl())
                .asBitmap()
                .into(new BitmapImageViewTarget(viewHolder.imagePreview) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        super.onResourceReady(resource, glideAnimation);
                        Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                Palette.Swatch vibrant = palette.getVibrantSwatch();
                                if (vibrant != null) {
                                    viewHolder.imagePreview.setBackgroundColor(vibrant.getRgb());
                                }
                            }
                        });
                    }
                });

        viewHolder.bindPhoto(photo, position);
    }

    @Override
    public int getItemCount() {
        if(photos == null)
            return 0;
        else
            return photos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView imageTitle;
        ImageView imagePreview;
        Photo photo;
        int position;

        public ViewHolder(View itemView, final PhotosView photosView) {
            super(itemView);

            imageTitle = (TextView) itemView.findViewById(R.id.imageTitle);
            imagePreview = (ImageView) itemView.findViewById(R.id.imagePreview);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    photosView.onPhotoClicked(photo, position);
                }
            });
        }

        public void bindPhoto(Photo photo, int position){
            this.photo = photo;
            this.position = position;
        }
    }
}
