package it.unimi.unimiplaces;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * NetworkManager
 */
public class NetworkManager {
    Context context;

    public NetworkManager(Context context){
        this.context = context;
    }

    /**
     * Check if a network connection is available
     * @return true if there's an internet connection, false otherwise
     */
    public boolean isNetworkConnected(){
        ConnectivityManager cm      = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork   = cm.getActiveNetworkInfo();
        return activeNetwork !=null && activeNetwork.isConnectedOrConnecting();
    }
}
