/*
 * Copyright 2014 A.C.R. Development
 */
package com.ofidy.ofidyshoppingbrowser.ofidyExtra.controller;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Message;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient.CustomViewCallback;

import com.ofidy.ofidyshoppingbrowser.ofidyExtra.database.HistoryItem;
import com.ofidy.ofidyshoppingbrowser.ofidyExtra.ui.TabsManager;
import com.ofidy.ofidyshoppingbrowser.ofidyExtra.ui.view.LightningView;


public interface UIController {

    void changeToolbarBackground(@NonNull Bitmap favicon, @Nullable Drawable drawable);

    @ColorInt
    int getUiColor();

    boolean getUseDarkTheme();

    void updateUrl(@Nullable String title, boolean shortUrl);

    void updateProgress(int n);

    void updateHistory(@Nullable String title, @NonNull String url);

    void openFileChooser(ValueCallback<Uri> uploadMsg);

    void onShowCustomView(View view, CustomViewCallback callback);

    void onShowCustomView(View view, CustomViewCallback callback, int requestedOrienation);

    void onHideCustomView();

    void onCreateWindow(Message resultMsg);

    void onCloseWindow(LightningView view);

    void hideActionBar();

    void showActionBar();

    void showFileChooser(ValueCallback<Uri[]> filePathCallback);

    void closeEmptyTab();

    void showCloseDialog(int position);

    void newTabButtonClicked();

    void tabCloseClicked(int position);

    void tabClicked(int position);

    void newTabButtonLongClicked();

    void bookmarkButtonClicked();

    void bookmarkItemClicked(@NonNull HistoryItem item);

    void closeBookmarksDrawer();

    void setForwardButtonEnabled(boolean enabled);

    void setBackButtonEnabled(boolean enabled);

    void tabChanged(LightningView tab);

    void onAddToCartClicked();

    TabsManager getTabModel();

    void onBackButtonPressed();

    void onForwardButtonPressed();

    void onHomeButtonPressed();

    void onCartButtonPressed();

    void showAddToCartButton(boolean show);

}
