package com.ofidy.ofidyshoppingbrowser.Materials.Events;

public class LoadSpecialProductsEvent implements ApiCallEvent {

    public static final int BEST_SELLING = 1;
    public static final int TRENDING = 2;

    public boolean cacheData = false;
    public final int tab;
    private boolean loadCachedData;

    public LoadSpecialProductsEvent(int tab, boolean cacheData) {
        this.tab = tab;
        this.cacheData = cacheData;
    }

    @Override
    public void loadCachedData() {
        loadCachedData = true;
    }

}
