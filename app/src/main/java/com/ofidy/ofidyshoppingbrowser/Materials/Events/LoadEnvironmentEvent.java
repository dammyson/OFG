package com.ofidy.ofidyshoppingbrowser.Materials.Events;

public class LoadEnvironmentEvent implements ApiCallEvent {

    //public final boolean forceNetworkCall;
    public boolean loadCachedData = false;

    public LoadEnvironmentEvent(boolean cacheData) {
        this.loadCachedData = cacheData;
    }

    @Override
    public void loadCachedData() {
        loadCachedData = true;
    }

}
