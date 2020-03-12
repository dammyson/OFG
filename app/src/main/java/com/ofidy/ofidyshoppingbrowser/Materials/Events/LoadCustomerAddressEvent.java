package com.ofidy.ofidyshoppingbrowser.Materials.Events;

public class LoadCustomerAddressEvent implements ApiCallEvent {

    public boolean loadCachedData = false;
    public boolean cacheData = false;

    public LoadCustomerAddressEvent(boolean cacheData) {
        this.cacheData = cacheData;
    }

    @Override
    public void loadCachedData() {
        loadCachedData = true;
    }

}
