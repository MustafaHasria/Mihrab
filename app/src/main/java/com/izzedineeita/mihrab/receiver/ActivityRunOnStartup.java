package com.izzedineeita.mihrab.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import com.izzedineeita.mihrab.MainActivity;

public class ActivityRunOnStartup extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("XXX1", "11111");
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.e("XXX2", "11111");
            Intent activityIntent = new Intent(context, MainActivity.class);
            activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(activityIntent);
        }
//        final String action = intent.getAction();
//        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
//            Log.e("XXX", "network change");
//            if (NetworkUtils.isConnect(context)) {
//                Log.e("XXX", "Connect");
//            }
//        }
    }

    public static class NetworkUtils {

        static boolean isConnect(Context context) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Network[] netArray = connectivityManager.getAllNetworks();
                NetworkInfo netInfo;
                for (Network net : netArray) {
                    netInfo = connectivityManager.getNetworkInfo(net);
                    if ((netInfo.getTypeName().equalsIgnoreCase("WIFI") || netInfo.getTypeName().equalsIgnoreCase("MOBILE")) && netInfo.isConnected() && netInfo.isAvailable()) {
                        //if (netInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                        Log.d("XXX", "NETWORKNAME: " + netInfo.getTypeName());
                        return true;
                    }
                }
            } else {
                if (connectivityManager != null) {
                    @SuppressWarnings("deprecation")
                    NetworkInfo[] netInfoArray = connectivityManager.getAllNetworkInfo();
                    if (netInfoArray != null) {
                        for (NetworkInfo netInfo : netInfoArray) {
                            if ((netInfo.getTypeName().equalsIgnoreCase("WIFI") || netInfo.getTypeName().equalsIgnoreCase("MOBILE")) && netInfo.isConnected() && netInfo.isAvailable()) {
                                //if (netInfo.getState() == NetworkInfo.State.CONNECTED) {
                                Log.d("XXX", "NETWORKNAME: " + netInfo.getTypeName());
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
        }
    }
}



