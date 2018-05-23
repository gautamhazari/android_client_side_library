package com.gsma.mobileconnect.r2.android.clientside.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.SubscriptionManager;

import com.gsma.mobileconnect.r2.android.clientside.BuildConfig;


public class PhoneNumberUtils {

    @SuppressLint("HardwareIds")
    public static String getPhoneNumber(Context context, Activity activity) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            return "EMPTY";
        }
        String number = NetworkUtils.getTelephonyManager(context).getLine1Number();
        if (number.equals("")) {

        }
        return number;

    }


}
