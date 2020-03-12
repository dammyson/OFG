package com.ofidy.ofidyshoppingbrowser.Materials.Events;





import com.ofidy.ofidyshoppingbrowser.Materials.model.Cart;

import java.util.List;

public class ShoppingCartLoadedEvent {

    public final List<Cart> cartItems;

    public ShoppingCartLoadedEvent(List<Cart> cartItems) {
        this.cartItems = cartItems;
    }

}
