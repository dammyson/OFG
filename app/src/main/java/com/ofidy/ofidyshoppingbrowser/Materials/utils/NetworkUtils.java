package com.ofidy.ofidyshoppingbrowser.Materials.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils {

    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    public static final int NETWORK_STATUS_NOT_CONNECTED=0,NETWORK_STAUS_WIFI=1,NETWORK_STATUS_MOBILE=2;
    /**
     * Check whether there is any network with a usable connection.
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean hasActiveInternetConnection(Context context) {
        if (isConnected(context)) {
            try {
                HttpURLConnection urlc = (HttpURLConnection)
                        (new URL("http://clients3.google.com/generate_204")
                                .openConnection());
                urlc.setRequestProperty("User-Agent", "Android");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(3000);
                urlc.setReadTimeout(4000);
                urlc.connect();
                return (urlc.getResponseCode() == 200);
//                return (urlc.getResponseCode() == 204 &&
//                        urlc.getContentLength() == 0);
            } catch (IOException e) {
                e.printStackTrace();
                //Log.e(TAG, "Error checking internet connection", e);
            }
        } else {
            //Log.d(TAG, "No network available!");

        }
        return false;
    }

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == TYPE_WIFI)
                return TYPE_WIFI;

            if(activeNetwork.getType() == TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static int getConnectivityStatusString(Context context) {
        int conn = getConnectivityStatus(context);
        int status = 0;
        if (conn == TYPE_WIFI) {
            status = NETWORK_STAUS_WIFI;
        } else if (conn == TYPE_MOBILE) {
            status =NETWORK_STATUS_MOBILE;
        } else if (conn == TYPE_NOT_CONNECTED) {
            status = NETWORK_STATUS_NOT_CONNECTED;
        }
        return status;
    }

}
