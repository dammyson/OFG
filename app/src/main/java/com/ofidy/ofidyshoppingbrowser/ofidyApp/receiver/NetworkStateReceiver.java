package com.ofidy.ofidyshoppingbrowser.ofidyApp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.ofidy.ofidyshoppingbrowser.Materials.Events.InterntStatusChangedEvent;

import static com.ofidy.ofidyshoppingbrowser.Materials.Events.BusProvider.getBus;


/**
 * Created by Ari on 02/12/2016.
 *
 * Broadcast receiver for network state
 */

public class NetworkStateReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        Log.d("app","Network connectivity change");
        if(intent.getExtras()!=null) {
            NetworkInfo ni=(NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
            if(ni!=null && ni.getState()== NetworkInfo.State.CONNECTED) {
                Log.i("app","Network "+ni.getTypeName()+" connected");
                getBus().post(new InterntStatusChangedEvent(true));
            }
        }
        if(intent.getExtras().getBoolean(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
            Log.d("app","There's no network connectivity");
            getBus().post(new InterntStatusChangedEvent(false));
        }
    }
}
