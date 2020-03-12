package com.ofidy.ofidyshoppingbrowser.Materials.Events;




import com.ofidy.ofidyshoppingbrowser.Materials.model.Product;

import java.util.List;

public class ProductsLoadedEvent {

    public final List<Product> products;
    public final int productsFetchLimit;
    public final int page;

    public ProductsLoadedEvent(List<Product> products, int productsFetchLimit, int page) {
        this.products = products;
        this.productsFetchLimit = productsFetchLimit;
        this.page = page;
    }

}
