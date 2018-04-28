package com.gsma.mobileconnect.r2.android.clientside.utils;


import android.util.Log;

import com.gsma.mobileconnect.r2.android.clientside.constants.Constants;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;


public class HttpUtils {

    private static final String TAG = HttpUtils.class.getSimpleName();

    /**
     * generates GET request to the server side endpoint (from your configuration).
     * @param url
     * @param params
     * @return
     */
    public static String createGetWithParams(String url, List<NameValuePair> params) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < params.size(); i++) {
            Object value = params.get(i).getValue();
            if (value != null) {
                try {
                    value = URLEncoder.encode(String.valueOf(value), HTTP.UTF_8);
                    if (builder.length() > 0)
                        builder.append("&");
                    builder.append(params.get(i).getName()).append("=").append(params.get(i).getValue());
                }
                catch (UnsupportedEncodingException e) {
                    Log.d(TAG, "Unable to create parameters for GET request");
                }
            }
        }
        return (url += "?" + builder.toString());
    }

    /**
     * Creates list of parameters in {@link NameValuePair} format.
     * @param msisdn
     * @param mcc
     * @param mnc
     * @param ipAddress
     * @return list of parameters for request.
     */
    public static List<NameValuePair> prepareParameters(String msisdn, String mcc, String mnc, String ipAddress) {
        List<NameValuePair> params = new LinkedList<>();
        if (!StringUtils.isNullOrEmpty(msisdn)) {
            params.add(new BasicNameValuePair(Constants.MSISDN, msisdn));
        } else if (!StringUtils.isNullOrEmpty(mcc) && !StringUtils.isNullOrEmpty(mnc)) {
            params.add(new BasicNameValuePair(Constants.MCC, mcc));
            params.add(new BasicNameValuePair(Constants.MNC, mnc));
        }
        if (!StringUtils.isNullOrEmpty(ipAddress)) {
            params.add(new BasicNameValuePair(Constants.SOURCE_IP, ipAddress));
        }
        return params;
    }
}
