package com.ofidy.ofidyshoppingbrowser.Materials.Events;


import com.ofidy.ofidyshoppingbrowser.Materials.model.OrderInvoice;

public class LoadPaymentInfoDoneEvent {

    public final OrderInvoice orderInvoice;

    public LoadPaymentInfoDoneEvent(OrderInvoice dest) {
        this.orderInvoice = dest;
    }

}
