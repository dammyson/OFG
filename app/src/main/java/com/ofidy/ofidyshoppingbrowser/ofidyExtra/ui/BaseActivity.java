package com.ofidy.ofidyshoppingbrowser.ofidyExtra.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.crashlytics.android.Crashlytics;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.BusProvider;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.InterntStatusChangedEvent;
import com.ofidy.ofidyshoppingbrowser.R;
import com.squareup.otto.Bus;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    public static final int LIST_DISPLAY = 0;
    public static final int TEXT_DISPLAY = 1;
    public static final int PROGRESS_DISPLAY = 2;

    private static final String TAG = "BaseActivity";
    public static final int SHOP_TYPE_PRODUCT = 1;
    public static final int CATALOG_TYPE_PRODUCT = 2;
    protected boolean searchShowing;
    protected MenuItem searchMenu;
    protected MenuItem cartMenu;
    protected MenuItem signMenu;
    protected Toolbar toolbar;
    protected int badgeCount;
    private boolean connected = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Crashlytics.log(Log.DEBUG, TAG, this.getClass().getSimpleName() + "#onCreate()");
        getBus().register(this);

    }

    protected void setLayout(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Crashlytics.log(Log.DEBUG, TAG, this.getClass().getSimpleName() + "#onStart()");
//        if (! (this instanceof LoginActivity)) {
//            mPasswordChangedEventHandler = new PasswordChangedEventHandler(this);
//            getBus().register(mPasswordChangedEventHandler);
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Crashlytics.log(Log.DEBUG, TAG, this.getClass().getSimpleName() + "#onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Crashlytics.log(Log.DEBUG, TAG, this.getClass().getSimpleName() + "#onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Crashlytics.log(Log.DEBUG, TAG, this.getClass().getSimpleName() + "#onStop()");
//        if (mPasswordChangedEventHandler != null) {
//            getBus().unregister(mPasswordChangedEventHandler);
//            mPasswordChangedEventHandler = null;
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cartMenu = null;
        Crashlytics.log(Log.DEBUG, TAG, this.getClass().getSimpleName() + "#onDestroy()");
        getBus().unregister(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Crashlytics.log(Log.DEBUG, TAG, this.getClass().getSimpleName() + "#onLowMemory()");
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Crashlytics.log(Log.DEBUG, TAG, this.getClass().getSimpleName() + "#onTrimMemory()");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void inflateCommonMenu(Menu menu){
        //searchMenu = menu.findItem(R.id.menu_search);
    }

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
//    }

    protected void openUrl(String url) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    protected void onInterntStatusChangedEvent(InterntStatusChangedEvent event) {
        if(event.connected != connected) {
            connected = event.connected;
            if(!event.connected)
                new Handler().postDelayed(() -> {
                    Snackbar bar = null;
                    Snackbar finalBar = bar;
                    bar = Snackbar.make(toolbar, "No internet connection, app will not work properly", Snackbar.LENGTH_INDEFINITE)
                            .setAction("Dismiss", v -> finalBar.dismiss());

                    bar.show();
                }, 5000);
        }
    }

    protected Bus getBus() {
        return BusProvider.getBus();
    }

}
