package com.jeremiahespinosa.anotherphotomanager.app;

import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.view.KeyEvent;
import android.widget.Toast;

import com.jeremiahespinosa.anotherphotomanager.BuildConfig;
import com.jeremiahespinosa.anotherphotomanager.R;
import com.squareup.leakcanary.LeakCanary;

import java.io.File;

import timber.log.Timber;

/**
 * Created by jespinosa on 2/13/16.
 */
public class App extends Application {
    private static App mInstance;
    private static Context mContext;
    private SharedPreferences mSharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        mInstance = this;
        mSharedPreferences = mContext.getSharedPreferences(getPackageName(), MODE_PRIVATE);
        LeakCanary.install(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());

        }
    }

    public static SharedPreferences getSharedPreferences() {
        return mInstance.mSharedPreferences;
    }

    public static String getSharedPrefString(String key, String defValue) {
        return mInstance.mSharedPreferences.getString(key, defValue);
    }

    public static String getSharedPrefString(String key) {
        return mInstance.mSharedPreferences.getString(key, "");
    }

    public static int getSharedPrefInteger(String key, int defValue) {
        return mInstance.mSharedPreferences.getInt(key, defValue);
    }

    public static long getSharedPrefLong(String key, long defValue) {
        return mInstance.mSharedPreferences.getLong(key, defValue);
    }

    public static boolean getSharedPrefBool(String key, boolean defValue) {
        return mInstance.mSharedPreferences.getBoolean(key, defValue);
    }

    public static void putSharedPref(String key, String value) {
        mInstance.mSharedPreferences.edit().putString(key, value).apply();
    }

    public static void putSharedPref(String key, boolean value) {
        mInstance.mSharedPreferences.edit().putBoolean(key, value).apply();
    }

    public static void putSharedPref(String key, int value) {
        mInstance.mSharedPreferences.edit().putInt(key, value).apply();
    }

    public static void putSharedPref(String key, long value) {
        mInstance.mSharedPreferences.edit().putLong(key, value).apply();
    }

    public static String getStringById(int stringId){
        return mContext.getResources().getString(stringId);
    }

    public static void showShortToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    public static boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * start the media scanner to update a given directory.
     * helps to make the android gallery update the thumbnails.
     *
     * @param path
     */
    public static void updateMediaDirectory(String path) {
        File file = new File(path);
        Uri uri = Uri.fromFile(file);
        Intent scanFileIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
        mInstance.sendBroadcast(scanFileIntent);
    }

    /**
     * @param context
     * @param message
     * @return a built progress dialog
     */
    public static ProgressDialog getProgressDialog(Context context, String message) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_SEARCH) {
                    return true;
                }
                return false;
            }
        });

        return progressDialog;
    }

    public static AlertDialog.Builder getDialogBuilder(Context context,
                                                       String title, String message, boolean cancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(cancelable)
                .setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        // Create the AlertDialog object and return it
        return builder;
    }
}
