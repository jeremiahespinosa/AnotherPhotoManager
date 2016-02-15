package com.jeremiahespinosa.anotherphotomanager.util;

import com.jeremiahespinosa.anotherphotomanager.app.App;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

/**
 * Created by jespinosa on 2/13/16.
 */
public class StorageUtils {

    public static void copyInputStreamToFile( InputStream in, java.io.File file ) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Timber.i("wrote to "+file.getPath());
        App.updateMediaDirectory(file.getPath());
    }

    public static void downloadImageFromNetwork(String urlToDownload, File file) throws IOException{
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(urlToDownload)
                .build();

        Response response = client.newCall(request).execute();

        copyInputStreamToFile(response.body().byteStream(), file);
    }

}
