package com.ofidy.ofidyshoppingbrowser.Materials.Events;




import com.ofidy.ofidyshoppingbrowser.Materials.model.Transaction;

import java.util.List;

public class TransactionsLoadedEvent {

    public final List<Transaction> items;

    public TransactionsLoadedEvent(List<Transaction> cartItems) {
        this.items = cartItems;
    }

}
