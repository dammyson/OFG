package com.ofidy.ofidyshoppingbrowser.Materials.Events;

public class LoadCategoryEvent implements ApiCallEvent {

    public final boolean forceNetworkCall;
    public boolean loadCachedData = false;

    public LoadCategoryEvent(boolean forceNetworkCall) {
        this.forceNetworkCall = forceNetworkCall;
    }

    @Override
    public void loadCachedData() {
        loadCachedData = true;
    }

}
