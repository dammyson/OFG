package com.ofidy.ofidyshoppingbrowser.Materials.Events;

public class LoadShoppingCartEvent implements ApiCallEvent {

    public boolean loadCachedData = false;
    public boolean cacheData = false;

    public LoadShoppingCartEvent(boolean cacheData) {
        this.cacheData = cacheData;
    }

    @Override
    public void loadCachedData() {
        loadCachedData = true;
    }

}
