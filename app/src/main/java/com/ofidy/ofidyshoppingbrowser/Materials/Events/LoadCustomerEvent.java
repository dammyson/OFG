package com.ofidy.ofidyshoppingbrowser.Materials.Events;

public class LoadCustomerEvent implements ApiCallEvent {

    public final boolean forceNetworkCall;
    public boolean loadCachedData = false;

    public LoadCustomerEvent(boolean forceNetworkCall) {
        this.forceNetworkCall = forceNetworkCall;
    }

    @Override
    public void loadCachedData() {
        loadCachedData = true;
    }

}
