package com.ofidy.ofidyshoppingbrowser.ofidyExtra.ui;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.ofidy.ofidyshoppingbrowser.R;
import com.ofidy.ofidyshoppingbrowser.bonsai.Action;
import com.ofidy.ofidyshoppingbrowser.bonsai.Observable;
import com.ofidy.ofidyshoppingbrowser.bonsai.Subscriber;


@SuppressWarnings("deprecation")
public class MainActivity extends BrowserActivity {

    @Override
    public Observable<Void> updateCookiePreference() {
        return Observable.create(new Action<Void>() {
            @Override
            public void onSubscribe(@NonNull Subscriber<Void> subscriber) {
                CookieManager cookieManager = CookieManager.getInstance();
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    CookieSyncManager.createInstance(MainActivity.this);
                }
                cookieManager.setAcceptCookie(mPreferences.getCookiesEnabled());
                subscriber.onComplete();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainb, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (isPanicTrigger(intent)) {
            panicClean();
        } else {
            handleNewIntent(intent);
            super.onNewIntent(intent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveOpenTabs();
    }

    @Override
    public void updateHistory(@Nullable String title, @NonNull String url) {
        addItemToHistory(title, url);
    }

    @Override
    public boolean isIncognito() {
        return false;
    }

    @Override
    public void closeActivity() {
        closeDrawers(() -> {
            performExitCleanUp();
            moveTaskToBack(true);
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
