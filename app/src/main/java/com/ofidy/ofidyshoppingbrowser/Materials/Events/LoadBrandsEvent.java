package com.ofidy.ofidyshoppingbrowser.Materials.Events;

/**
 * Created by damil on 8/2/2017.
 */
public class LoadBrandsEvent implements ApiCallEvent {
    public boolean loadCachedData = false;
    public boolean cacheData = false;
    public final int cat1;
    public final int cat2;
    public final int cat3;
    public final int cat4;
    public final int cat5;


    public LoadBrandsEvent(int cat1, int cat2, int cat3, int cat4, int cat5,  boolean cacheData) {
        this.cat1 = cat1;
        this.cat2 = cat2;
        this.cat3 = cat3;
        this.cat4 = cat4;
        this.cat5 = cat5;
        this.cacheData = cacheData;

    }


    @Override
    public void loadCachedData() {
        loadCachedData = true;
    }
}

