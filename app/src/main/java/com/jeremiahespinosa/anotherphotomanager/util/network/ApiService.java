package com.jeremiahespinosa.anotherphotomanager.util.network;

import com.jeremiahespinosa.anotherphotomanager.app.BaseConstants;
import com.jeremiahespinosa.anotherphotomanager.data.api.PhotoEntity;

import java.util.List;

import retrofit.RestAdapter;
import retrofit.http.GET;
import rx.Observable;

/**
 * Created by jespinosa on 2/13/16.
 */
public interface ApiService {

    class Implementation {
        public static ApiService call(){
            return getBuilder()
                    .build()
                    .create(ApiService.class);
        }
        public static RestAdapter.Builder getBuilder() {
            return new RestAdapter.Builder()
                    .setEndpoint(BaseConstants.API_ENDPOINT)
                    .setLogLevel(RestAdapter.LogLevel.BASIC);
        }
    }

    @GET("/photos")
    Observable<List<PhotoEntity>> getCloudPhotos();
}
