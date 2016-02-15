package com.jeremiahespinosa.anotherphotomanager.data.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Photo object returned from http://jsonplaceholder.typicode.com/photos
 *
 * Created by jespinosa on 2/13/16.
 */
public class PhotoResponse {
    /**[
            {
                "albumId": 1,
                "id": 1,
                "title": "accusamus beatae ad facilis cum similique qui sunt",
                "url": "http://placehold.it/600/92c952",
                "thumbnailUrl": "http://placehold.it/150/30ac17"
            },
            ...
       ]
     */

    private List<DataEntity> data;

    public List<DataEntity> getData() {
        return data;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public static class DataEntity {

        @SerializedName("albumId")
        private int albumId;

        @SerializedName("id")
        private int id;

        @SerializedName("title")
        private String title;

        @SerializedName("url")
        private String url;

        @SerializedName("thumbnailUrl")
        private String thumbnailUrl;

        public int getAlbumId() {
            return albumId;
        }

        public void setAlbumId(int albumId) {
            this.albumId = albumId;
        }

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

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getThumbnailUrl() {
            return thumbnailUrl;
        }

        public void setThumbnailUrl(String thumbnailUrl) {
            this.thumbnailUrl = thumbnailUrl;
        }

    }

}
