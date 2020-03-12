package com.ofidy.ofidyshoppingbrowser.Materials.Events;




import com.ofidy.ofidyshoppingbrowser.Materials.model.Product;

import java.util.List;

public class SpecialProductsLoadedEvent {

    public final List<Product> products;
    public final int productsFetchLimit;
    public final int tab;

    public SpecialProductsLoadedEvent(List<Product> products, int tab, int productsFetchLimit) {
        this.products = products;
        this.productsFetchLimit = productsFetchLimit;
        this.tab = tab;
    }

}
