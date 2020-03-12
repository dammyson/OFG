package com.ofidy.ofidyshoppingbrowser.Materials.Events;

public class SpecialProductsLoadErrorEvent {

    public final int productsFetchLimit;
    public final String message;
    public final int tab;

    public SpecialProductsLoadErrorEvent(int tab, String message, int productsFetchLimit) {
        this.message = message;
        this.productsFetchLimit = productsFetchLimit;
        this.tab = tab;
    }

}
