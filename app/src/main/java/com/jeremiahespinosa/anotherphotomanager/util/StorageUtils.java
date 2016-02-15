package com.jeremiahespinosa.anotherphotomanager.util;

import android.os.Environment;
import com.jeremiahespinosa.anotherphotomanager.app.App;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

/**
 * Created by jespinosa on 2/13/16.
 */
public class StorageUtils {

    public static void writeFile(String path, byte[] data) {
        FileOutputStream out = null;

        try {
            out = new FileOutputStream(path);
            out.write(data);
        }
        catch (Exception e) {
            Timber.e( "Failed to write data");
            e.printStackTrace();
        }
        finally {
            try {
                out.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        data = null;
    }

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

    public static File getStorageDirectory(String folderName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), folderName);
        if (!file.mkdirs()) {
            Timber.e("Directory not created");
        }
        return file;
    }

    public static InputStream downloadFileFromNetwork(String urlToDownload) throws IOException{
        URL url = new URL(urlToDownload);
        URLConnection urlConnection = url.openConnection();

        return new BufferedInputStream(urlConnection.getInputStream());
    }

    public static void downloadImageFromNetwork(String urlToDownload, File file) throws IOException{
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(urlToDownload)
                .build();

        Response response = client.newCall(request).execute();

        copyInputStreamToFile(response.body().byteStream(), file);
    }

}
