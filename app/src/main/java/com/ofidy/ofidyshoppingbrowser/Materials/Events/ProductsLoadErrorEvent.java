package com.ofidy.ofidyshoppingbrowser.Materials.Events;

public class ProductsLoadErrorEvent {

    public final int productsFetchLimit;
    public final int page;
    public final String message;

    public ProductsLoadErrorEvent(String message, int productsFetchLimit, int page) {
        this.message = message;
        this.productsFetchLimit = productsFetchLimit;
        this.page = page;
    }

}
