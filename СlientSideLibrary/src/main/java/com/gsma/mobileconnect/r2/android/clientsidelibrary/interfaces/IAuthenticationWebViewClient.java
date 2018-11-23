package com.gsma.mobileconnect.r2.android.clientsidelibrary.interfaces;


public interface IAuthenticationWebViewClient {

    boolean qualifyUrl(final String url);

    void handleError(Exception e);

}
