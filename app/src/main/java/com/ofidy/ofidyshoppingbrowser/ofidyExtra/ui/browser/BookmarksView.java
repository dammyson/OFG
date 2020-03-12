package com.ofidy.ofidyshoppingbrowser.ofidyExtra.ui.browser;

import android.support.annotation.NonNull;

public interface BookmarksView {

    void navigateBack();

    void handleUpdatedUrl(@NonNull String url);

}
