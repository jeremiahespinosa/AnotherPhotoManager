package com.jeremiahespinosa.anotherphotomanager.data.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jespinosa on 2/14/16.
 */
public class Photo implements Parcelable{
    private int id;
    private String title = "";
    private String thumbnailUrl = "";
    private String url = "";

    public Photo(int id, String title, String thumbnailUrl, String url) {
        this.id = id;
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
        this.url = url;
    }

    protected Photo(Parcel in) {
        id = in.readInt();
        title = in.readString();
        thumbnailUrl = in.readString();
        url = in.readString();
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(thumbnailUrl);
        dest.writeString(url);
    }
}
