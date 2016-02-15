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
import com.jeremiahespinosa.anotherphotomanager.data.models.Album;
import com.jeremiahespinosa.anotherphotomanager.ui.fragments.home.HomeView;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by jespinosa on 2/13/16.
 */
public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.ViewHolder>{

    private ArrayList<Album> mAlbums = new ArrayList<>();
    private HomeView homeView;

    public AlbumsAdapter(ArrayList<Album> albums, HomeView homeView) {
        mAlbums = albums;
        this.homeView = homeView;
    }

    @Override
    public AlbumsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_album_item, parent, false);
        return new ViewHolder(v, homeView);
    }

    @Override
    public void onBindViewHolder(final AlbumsAdapter.ViewHolder viewHolder, int position) {
        Album album = mAlbums.get(position);

        viewHolder.albumTitle.setText(viewHolder.albumTitle.getContext().getString(R.string.album_number_formatted, album.getId()));
        viewHolder.albumCount.setText(viewHolder.albumCount.getContext().getString(R.string.images_count_formatted, album.getPhotos().size()));

        //for each album get the first image in its list of photos
        Glide.with(viewHolder.imageView.getContext())
                .load(album.getPhotos().get(0).getThumbnailUrl())
                .asBitmap()
                .into(new BitmapImageViewTarget(viewHolder.imageView){
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        super.onResourceReady(resource, glideAnimation);
                        Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                Palette.Swatch vibrant = palette.getVibrantSwatch();
                                if(vibrant != null){
                                    viewHolder.imageView.setBackgroundColor(vibrant.getRgb());
                                }
                            }
                        });
                    }
                });

        viewHolder.bindAlbum(album);
    }

    @Override
    public int getItemCount() {
        if(mAlbums != null)
            return mAlbums.size();
        else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView albumTitle;
        TextView albumCount;
        ImageView imageView;
        Album album;

        public ViewHolder(View itemView, final HomeView homeView) {
            super(itemView);

            albumTitle = (TextView) itemView.findViewById(R.id.albumTitleTextView);
            albumCount = (TextView) itemView.findViewById(R.id.albumCountTextView);
            imageView = (ImageView) itemView.findViewById(R.id.albumImageView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    homeView.onAlbumSelected(album);
                }
            });
        }

        public void bindAlbum(Album album){
            this.album = album;
        }
    }
}
