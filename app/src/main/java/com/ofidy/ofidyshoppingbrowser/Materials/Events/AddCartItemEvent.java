package com.ofidy.ofidyshoppingbrowser.Materials.Events;


import com.ofidy.ofidyshoppingbrowser.Materials.model.Product;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Seller;

public class AddCartItemEvent {

    public final Product product;
    public final Seller seller;
    public final int quantity;
    public final String size;

    public AddCartItemEvent(Product product, Seller seller, int quantity, String size) {
        this.product = product;
        this.seller = seller;
        this.quantity = quantity;
        this.size = size;
    }

}
