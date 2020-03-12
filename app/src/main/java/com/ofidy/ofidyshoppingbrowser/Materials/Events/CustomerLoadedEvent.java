package com.ofidy.ofidyshoppingbrowser.Materials.Events;


import com.ofidy.ofidyshoppingbrowser.Materials.model.Customer;

public class CustomerLoadedEvent {

    public final Customer user;

    public CustomerLoadedEvent(Customer user) {
        this.user = user;
    }

}
