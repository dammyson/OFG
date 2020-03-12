package com.ofidy.ofidyshoppingbrowser.Materials.Events;

public class LoadProductEvent implements ApiCallEvent {

    //public final boolean forceNetworkCall;
    public boolean loadCachedData = false;
    public boolean cacheData = false;
    public final String id;

    public LoadProductEvent(String id, boolean cacheData) {
        this.id = id;
        this.cacheData = cacheData;
    }

    @Override
    public void loadCachedData() {
        loadCachedData = true;
    }

}
