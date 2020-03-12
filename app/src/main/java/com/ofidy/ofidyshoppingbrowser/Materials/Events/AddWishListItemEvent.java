package com.ofidy.ofidyshoppingbrowser.Materials.Events;


import com.ofidy.ofidyshoppingbrowser.Materials.model.Product;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Seller;

public class AddWishListItemEvent {

    public final Product product;
    public final Seller seller;

    public AddWishListItemEvent(Product product, Seller seller) {
        this.product = product;
        this.seller = seller;
    }

}
