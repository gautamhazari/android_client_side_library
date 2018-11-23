package com.gsma.mobileconnect.r2.android.clientsidelibrary.handlers;

import android.content.Context;

import com.gsma.mobileconnect.r2.android.clientsidelibrary.utils.HttpUtils;
import com.gsma.mobileconnect.r2.android.clientsidelibrary.view.MobileConnectView;

import org.apache.http.NameValuePair;

import java.util.List;


public class SessionHandler {

    public void startWithoutDiscoverySession(Context context, String msisdn, String serverSideWithoutDiscoveryEndpoint) {
        List<NameValuePair> params = HttpUtils.prepareParameters(msisdn);
        final String requestUrl = HttpUtils.createGetWithParams(serverSideWithoutDiscoveryEndpoint, params);
        MobileConnectView mobileConnectView = new MobileConnectView();
        mobileConnectView.startAuthentication(context, requestUrl);
    }

    public void startDiscoverySession(Context context, String msisdn, String mcc, String mnc, String ipAddress, String serverSideWithoutDiscoveryEndpoint) {
        List<NameValuePair> params = HttpUtils.prepareParameters(msisdn, mcc, mnc, ipAddress);
        final String requestUrl = HttpUtils.createGetWithParams(serverSideWithoutDiscoveryEndpoint, params);
        MobileConnectView mobileConnectView = new MobileConnectView();
        mobileConnectView.startAuthentication(context, requestUrl);
    }
}
