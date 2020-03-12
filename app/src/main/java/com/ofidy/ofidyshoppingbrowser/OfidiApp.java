package com.ofidy.ofidyshoppingbrowser;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import com.ofidy.ofidyshoppingbrowser.Materials.Events.BusProvider;
import com.ofidy.ofidyshoppingbrowser.Materials.pref.UserPrefs;
import com.ofidy.ofidyshoppingbrowser.Materials.utils.OkHttp3Downloader;
import com.ofidy.ofidyshoppingbrowser.Materials.utils.ServerHelper;
import com.ofidy.ofidyshoppingbrowser.ofidyApp.analytics.AnalyticsService;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.concurrent.TimeUnit;

import co.paystack.android.PaystackSdk;
import io.fabric.sdk.android.Fabric;
import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Created by ari on 10/11/16.
 */
public class OfidiApp extends Application {

    private static final String TAG = "SpectreApplication";
    private static OfidiApp sInstance;

    private static final String IMAGE_CACHE_PATH = "images";
    private static final int MIN_DISK_CACHE_SIZE = 5 * 1024 * 1024;     // in bytes
    private static final int MAX_DISK_CACHE_SIZE = 50 * 1024 * 1024;    // in bytes
    private static final int CONNECTION_TIMEOUT = 20 * 1000;            // in milliseconds

    protected OkHttpClient mOkHttpClient = null;
    private String currency;

    protected Picasso mPicasso = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        Crashlytics.log(Log.DEBUG, TAG, "APP LAUNCHED");
        BusProvider.getBus().register(this);

        sInstance = this;

//        setupRealm();
//        setupFonts();

        initOkHttpClient();
        initPicasso();
        PaystackSdk.initialize(getApplicationContext());
        new ServerHelper().start(this, mOkHttpClient);
//
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            FacebookSdk.sdkInitialize(getApplicationContext());
//            AppEventsLogger.activateApp(this);
//        }
        AnalyticsService mAnalyticsService = new AnalyticsService(BusProvider.getBus());
        mAnalyticsService.start();
    }

    public static OfidiApp getInstance() {
        return sInstance;
    }

    protected void initOkHttpClient() {
        if (mOkHttpClient != null) {
            return;
        }
        File cacheDir = createCacheDir(this, IMAGE_CACHE_PATH);
        long size = calculateDiskCacheSize(cacheDir);
        Cache cache = new Cache(cacheDir, size);
        mOkHttpClient = new OkHttpClient.Builder()
                .cache(cache)
                .readTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                .build();
    }

    public void setCurrency(String c){
        currency = c;
    }

    /**
     * Returns current currency selection.
     */
    public String getCurrency(){
        if(TextUtils.isEmpty(currency))
            return UserPrefs.getInstance(this).getString(UserPrefs.Key.CURRENCY);
        return currency;
    }

    @SuppressWarnings("WeakerAccess")
    protected void initPicasso() {
        if (mPicasso != null) {
            return;
        }
        mPicasso = new Picasso.Builder(this)
                .downloader(new OkHttp3Downloader(mOkHttpClient))
                .listener((picasso, uri, exception) -> Log.e("Picasso", "Failed to load image: " + uri + "\n"
                        + Log.getStackTraceString(exception)))
                .build();
    }

    /**
     * Returns picasso object.
     */
    public Picasso getPicasso() {
        return mPicasso;
    }

    /**
     * Determines whether or not the input year has already passed.
     *
     * @param dir, file object
     * @return {@code true} if the year has passed, {@code false} otherwise.
     */
    private static long calculateDiskCacheSize(File dir) {
        long size = MIN_DISK_CACHE_SIZE;
        try {
            StatFs statFs = new StatFs(dir.getAbsolutePath());
            long available;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                available = statFs.getBlockCountLong() * statFs.getBlockSizeLong();
            } else {
                // checked at runtime
                //noinspection deprecation
                available = statFs.getBlockCount() * statFs.getBlockSize();
            }
            // Target 2% of the total space.
            size = available / 50;
        } catch (IllegalArgumentException ignored) {
        }
        // Bound inside min/max size for disk cache.
        return Math.max(Math.min(size, MAX_DISK_CACHE_SIZE), MIN_DISK_CACHE_SIZE);
    }

    /**
     * Creates file cache directory in local file system.
     *
     * @param context
     * @param path file path, string
     */
    private static File createCacheDir(Context context, String path) {
        File cacheDir = context.getApplicationContext().getExternalCacheDir();
        if (cacheDir == null)
            cacheDir = context.getApplicationContext().getCacheDir();
        File cache = new File(cacheDir, path);
        if (!cache.exists()) {
            cache.mkdirs();
        }
        return cache;
    }
}
