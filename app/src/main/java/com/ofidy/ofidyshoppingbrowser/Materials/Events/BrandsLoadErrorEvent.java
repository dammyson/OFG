package com.ofidy.ofidyshoppingbrowser.Materials.Events;

/**
 * Created by damil on 8/2/2017.
 */
public class BrandsLoadErrorEvent {
    public final int BrandsFetchLimit;
    public final int page;
    public final String message;

    public BrandsLoadErrorEvent(String message, int brandsFetchLimit, int page ) {
        BrandsFetchLimit = brandsFetchLimit;
        this.page = page;
        this.message = message;
    }
}
