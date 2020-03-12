package com.ofidy.ofidyshoppingbrowser.Materials.Events;



import com.ofidy.ofidyshoppingbrowser.Materials.model.Address;

import java.util.List;

public class AddressLoadedEvent {

    public final List<Address> addresses;

    public AddressLoadedEvent(List<Address> cartItems) {
        this.addresses = cartItems;
    }

}
