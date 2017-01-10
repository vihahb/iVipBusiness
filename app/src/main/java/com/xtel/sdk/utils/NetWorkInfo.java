package com.xtel.sdk.utils;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Vũ Hà Vi on 11/4/2016
 */
public class NetWorkInfo {

    public static boolean isOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();
        } catch (Exception e) {
            return false;
        }
    }

//    public static void checkGPS(final Context context) {
//        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//        boolean gps_enabled = false;
//        boolean network_enabled = false;
//
//        try {
//            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//        try {
//            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//        if (!gps_enabled && !network_enabled) {
//            // notify user
//            AlertDialog.Builder dialog = new AlertDialog.Builder(context, R.style.TimePicker);
//            dialog.setMessage("Để ứng dụng trợ giúp bạn một cách hiệu quả nhất xin vui lòng bật GPS?");
//            dialog.setPositiveButton("Cài đặt", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                    // TODO Auto-generated method stub
//                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                    context.startActivity(myIntent);
//                    //get gps
//                }
//            });
//            dialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
//
//                @Override
//                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                    // TODO Auto-generated method stub
//
//                }
//            });
//            dialog.show();
//        }
//    }
}