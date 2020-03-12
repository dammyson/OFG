package com.ofidy.ofidyshoppingbrowser.Materials.Events;

public class LoadWishlistEvent implements ApiCallEvent {

    public boolean loadCachedData = false;
    public boolean cacheData = false;

    public LoadWishlistEvent(boolean cacheData) {
        this.cacheData = cacheData;
    }

    @Override
    public void loadCachedData() {
        loadCachedData = true;
    }

}
