package com.ofidy.ofidyshoppingbrowser;

import android.app.Activity;
import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.StatFs;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;

import com.crashlytics.android.Crashlytics;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.BusProvider;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Cart;
import com.ofidy.ofidyshoppingbrowser.Materials.pref.UserPrefs;
import com.ofidy.ofidyshoppingbrowser.Materials.utils.FileUtils;
import com.ofidy.ofidyshoppingbrowser.Materials.utils.MemoryLeakUtils;
import com.ofidy.ofidyshoppingbrowser.Materials.utils.OkHttp3Downloader;
import com.ofidy.ofidyshoppingbrowser.Materials.utils.ServerHelper;
import com.ofidy.ofidyshoppingbrowser.ofidyApp.analytics.AnalyticsService;
import com.ofidy.ofidyshoppingbrowser.ofidyExtra.preference.PreferenceManager;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.otto.Bus;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import co.paystack.android.PaystackSdk;
import io.fabric.sdk.android.Fabric;
import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Created by Damilola on 8/23/2018.
 */

public class Ofidy extends Application {
    private static final String TAG = Ofidy.class.getSimpleName();

    private static AppComponent mAppComponent;
   private static final Executor mIOThread = Executors.newSingleThreadExecutor();
   private static final Executor mTaskThread = Executors.newCachedThreadPool();
    // in milliseconds

    @Inject
    Bus mBus;
    @Inject
    PreferenceManager mPreferenceManager;
    public static ArrayList<Cart> cartItems;
    private static final String IMAGE_CACHE_PATH = "images";
    private static final int MIN_DISK_CACHE_SIZE = 5 * 1024 * 1024;     // in bytes
    private static final int MAX_DISK_CACHE_SIZE = 50 * 1024 * 1024;    // in bytes
    private static final int CONNECTION_TIMEOUT = 20 * 1000;            // in milliseconds

    protected static OkHttpClient mOkHttpClient = null;
    private String currency;

    protected Picasso mPicasso = null;

    private static Ofidy sInstance;



    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        Fabric.with(this, new Crashlytics());
        cartItems = new ArrayList<>();
        new ServerHelper().start(this, getOkHttpClient());
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
        }

       /* final Thread.UncaughtExceptionHandler defaultHandler = Thread.getDefaultUncaughtExceptionHandler();

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {

                if (BuildConfig.DEBUG) {
                    FileUtils.writeCrashToStorage(ex);
                }

                if (defaultHandler != null) {
                    defaultHandler.uncaughtException(thread, ex);
                } else {
                    System.exit(2);
                }
            }
        });*/

        mAppComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
        mAppComponent.inject(this);

        if (mPreferenceManager.getUseLeakCanary() && !isRelease()) {
            LeakCanary.install(this);
        }
        if (!isRelease() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        registerActivityLifecycleCallbacks(new MemoryLeakUtils.LifecycleAdapter() {
            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.d(TAG, "Cleaning up after the Android framework");
                MemoryLeakUtils.clearNextServedView(activity, Ofidy.this);
            }
        });





        Fabric.with(this, new Crashlytics());
        Crashlytics.log(Log.DEBUG, TAG, "APP LAUNCHED");
       BusProvider.getBus().register(this);

//        setupRealm();
//        setupFonts();

       // initOkHttpClient();
        initPicasso();
        PaystackSdk.initialize(getApplicationContext());
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            FacebookSdk.sdkInitialize(getApplicationContext());
//            AppEventsLogger.activateApp(this);
//        }
        AnalyticsService mAnalyticsService = new AnalyticsService(BusProvider.getBus());
        mAnalyticsService.start();
    }


    public static Ofidy getInstance() {
        return sInstance;
    }

  /*  protected void initOkHttpClient() {
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
    }*/

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


    @NonNull
    public static Ofidy get(@NonNull Context context) {
        return (Ofidy) context.getApplicationContext();
    }

    public static AppComponent getAppComponent() {
        return mAppComponent;
    }

    @NonNull
    public static Executor getIOThread() {
        return mIOThread;
    }

    @NonNull
    public static Executor getTaskThread() {
        return mTaskThread;
    }

    public static Bus getBus(@NonNull Context context) {
        return get(context).mBus;
    }

    /**
     * Determines whether this is a release build.
     *
     * @return true if this is a release build, false otherwise.
     */
    public static boolean isRelease() {
        return !BuildConfig.DEBUG || BuildConfig.BUILD_TYPE.toLowerCase().equals("release");
    }

    public static void copyToClipboard(@NonNull Context context, @NonNull String string) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("URL", string);
        clipboard.setPrimaryClip(clip);
    }

    public static OkHttpClient getOkHttpClient() {
        if (mOkHttpClient != null) {
            return mOkHttpClient;
        }
        mOkHttpClient = new OkHttpClient.Builder()
                .readTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                .build();
        return mOkHttpClient;
    }

}
