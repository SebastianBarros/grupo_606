package com.example.barros_costa_tp2_2020;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import static androidx.core.content.ContextCompat.getSystemService;


public class InternetConnectionService {

    //Static method to check if the user is currently connected to a network. It doesn't matter if it's mobile data or wifi.

    public static boolean thereIsInternetConnection(Context context) {
        ConnectivityManager connectivityManager;
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
    }

}
