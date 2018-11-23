package com.gsma.mobileconnect.r2.android.clientsidelibrary.utils;

import com.gsma.mobileconnect.r2.android.clientsidelibrary.BuildConfig;

public class RequestUtils {

    public static String getCurrentLibVersion() {
        return StringUtils.isNullOrEmpty(BuildConfig.VERSION_NAME) ? "none" : BuildConfig.VERSION_NAME;
    }
}
