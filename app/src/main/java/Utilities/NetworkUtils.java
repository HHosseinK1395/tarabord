package Utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import static android.content.Context.CONNECTIVITY_SERVICE;
/**
 * Created by N550J on 1/7/2019.
 */

public class NetworkUtils {
    public static boolean hasInternetAccess(Context context) {
        boolean connected;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService
                (CONNECTIVITY_SERVICE);
        //we are connected to a network
        NetworkInfo mobileNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo lanNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
//        NetworkInfo ni = connectivityManager.getActiveNetworkInfo();
//        connected = ni.getState() != null && ni.getState() == NetworkInfo.State.CONNECTED;
        connected = (mobileNetworkInfo != null && mobileNetworkInfo.getState() == NetworkInfo.State.CONNECTED) ||
                (wifiNetworkInfo != null && wifiNetworkInfo.getState() == NetworkInfo.State.CONNECTED)||
                (lanNetworkInfo != null && lanNetworkInfo.getState() == NetworkInfo.State.CONNECTED)
        ;
        return connected;
    }
}