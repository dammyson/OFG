package com.ofidy.ofidyshoppingbrowser.Materials.Events;


import com.ofidy.ofidyshoppingbrowser.ofidyApp.ui.MainThreadBus;
import com.squareup.otto.Bus;

public class BusProvider {

    private static final MainThreadBus mBus = new MainThreadBus();

    private BusProvider() {}

    public static Bus getBus() { return mBus; }

}
