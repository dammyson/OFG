package com.ofidy.ofidyshoppingbrowser.Materials.Events;

import com.ofidy.ofidyshoppingbrowser.Materials.model.Customer;

public class UpdateCustomerEvent implements ApiCallEvent {

    public boolean loadCachedData = false;
    public final Customer customer;

    public UpdateCustomerEvent(Customer customer) {
        this.customer = customer;
    }

    @Override
    public void loadCachedData() {
        loadCachedData = true;
    }

}
