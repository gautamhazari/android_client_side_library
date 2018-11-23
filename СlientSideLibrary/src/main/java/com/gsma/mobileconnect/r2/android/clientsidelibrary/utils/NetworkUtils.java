package com.gsma.mobileconnect.r2.android.clientsidelibrary.utils;


import android.content.Context;
import android.telephony.TelephonyManager;


public class NetworkUtils {

    /**
     * @param context - application context
     * @return the numeric name (MCC+MNC) of the current SIM card operator.
     */
    private static String getNetworkOperator(Context context) {
        return getTelephonyManager(context).getSimOperator();
    }

    /**
     * Management the telephony features of the device.
     * @param context - application context
     * @return telephony features of the device.
     */
    public static TelephonyManager getTelephonyManager(Context context) {
        return (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    /**
     * @param context - application context
     * @return MCC of the current SIM card operator.
     */
    public static String getMCC(Context context) {
        String network = getNetworkOperator(context);
        return !StringUtils.isNullOrEmpty(network) ? network.substring(0, 3) : null;
    }

    /**
     * @param context - application context
     * @return MNC of the current SIM card operator.
     */
    public static String getMNC(Context context) {
        String network = getNetworkOperator(context);
        return !StringUtils.isNullOrEmpty(network) ? network.substring(3) : null;
    }
}
