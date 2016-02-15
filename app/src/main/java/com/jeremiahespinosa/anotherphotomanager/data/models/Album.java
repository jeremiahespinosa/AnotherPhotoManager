package com.jeremiahespinosa.anotherphotomanager.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jespinosa on 2/14/16.
 */
public class Album implements Parcelable {
    private int id;
    private ArrayList<Photo> photos = new ArrayList<>();

    public Album() {
    }

    public Album(int id, ArrayList<Photo> photos) {
        this.id = id;
        this.photos = photos;
    }

    protected Album(Parcel in) {
        id = in.readInt();
        in.readTypedList(photos, Photo.CREATOR);
    }

    public static final Creator<Album> CREATOR = new Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel in) {
            return new Album(in);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<Photo> photos) {
        this.photos = photos;
    }

    public void addPhoto(Photo photo){
        this.photos.add(photo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeTypedList(photos);
    }
}
