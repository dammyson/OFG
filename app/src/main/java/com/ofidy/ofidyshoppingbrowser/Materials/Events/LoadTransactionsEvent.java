package com.ofidy.ofidyshoppingbrowser.Materials.Events;

public class LoadTransactionsEvent implements ApiCallEvent {

    public boolean loadCachedData = false;
    public boolean cacheData = false;

    public LoadTransactionsEvent(boolean cacheData) {
        this.cacheData = cacheData;
    }

    @Override
    public void loadCachedData() {
        loadCachedData = true;
    }

}
