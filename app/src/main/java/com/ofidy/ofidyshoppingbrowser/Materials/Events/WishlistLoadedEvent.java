package com.ofidy.ofidyshoppingbrowser.Materials.Events;




import com.ofidy.ofidyshoppingbrowser.Materials.model.Product;

import java.util.List;

public class WishlistLoadedEvent {

    public final List<Product> items;

    public WishlistLoadedEvent(List<Product> cartItems) {
        this.items = cartItems;
    }

}
