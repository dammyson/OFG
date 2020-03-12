package com.ofidy.ofidyshoppingbrowser.Materials.Events;





import com.ofidy.ofidyshoppingbrowser.Materials.model.CartDetails;

import java.util.List;

/**
 * Created by Damilola on 7/16/2018.
 */

public class ShoppingCartDetailsLoadedEvent {

    public final List<CartDetails> cartItems;

    public ShoppingCartDetailsLoadedEvent(List<CartDetails> cartItems) {
        this.cartItems = cartItems;
    }
}
