package com.ofidy.ofidyshoppingbrowser.Materials.Events;

public class LoadProductsEvent implements ApiCallEvent {

    //public final boolean forceNetworkCall;
    public boolean loadCachedData = false;
    public boolean cacheData = false;
    public final int category;
    public final int catGroup;
    public final String minPrice;
    public final String maxPrice;
    public final int region;
    public final String searchString;
    public final String currency;
    public final int page;
    public final int subCat;
    public final int brand;

    public LoadProductsEvent(int catGroup, int category, int subCat, String searchString, String minPrice,
                             String maxPrice, int region, String currency, int page, int brand, boolean cacheData) {
        this.catGroup = catGroup;
        this.category = category;
        this.subCat = subCat;
        this.searchString = searchString;
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
        this.region = region;
        this.page = page;
        this.cacheData = cacheData;
        this.currency = currency;
        this.brand = brand;
    }

    @Override
    public void loadCachedData() {
        loadCachedData = true;
    }

}
