package com.ofidy.ofidyshoppingbrowser.Materials.Events;


import com.ofidy.ofidyshoppingbrowser.Materials.model.Product;

public class ProductLoadedEvent {

    public final Product product;

    public ProductLoadedEvent(Product product) {
        this.product = product;
    }

}
