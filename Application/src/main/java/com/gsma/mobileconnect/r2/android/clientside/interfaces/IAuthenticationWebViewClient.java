package com.gsma.mobileconnect.r2.android.clientside.interfaces;


public interface IAuthenticationWebViewClient {

    boolean qualifyUrl(final String url);

    void handleError(Exception e);

}
