package com.gsma.mobileconnect.r2.android.clientside.webviewclient;


import android.annotation.TargetApi;
import android.net.UrlQuerySanitizer;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gsma.mobileconnect.r2.android.clientside.R;
import com.gsma.mobileconnect.r2.android.clientside.constants.Constants;
import com.gsma.mobileconnect.r2.android.clientside.interfaces.IAuthenticationWebViewClient;
import com.gsma.mobileconnect.r2.android.clientside.tasks.AuthenticationTask;
import com.gsma.mobileconnect.r2.android.clientside.view.DiscoveryAuthenticationDialog;

/**
 * A custom {@link android.webkit.WebViewClient} to handle Authorization in a {@link android.webkit.WebView}
 */
public class AuthenticationWebViewClient extends WebViewClient implements IAuthenticationWebViewClient {

    private ProgressBar progressBar;
    private DiscoveryAuthenticationDialog dialog;
    private String redirectUrl;

    private static final String TAG = AuthenticationWebViewClient.class.getSimpleName();

    public AuthenticationWebViewClient(final DiscoveryAuthenticationDialog dialog, final ProgressBar progressBar,
                                final String redirectUrl) {
        this.progressBar = progressBar;
        this.dialog = dialog;
        this.redirectUrl = redirectUrl;
    }

    @Override
    public void onReceivedError(final WebView view, final int errorCode, final String description, final String failingUrl) {
        Log.i(TAG, String.format("onReceivedError description=%s, failingUrl=%s", description, failingUrl));
        this.dialog.cancel();
        this.handleError(new Exception(String.format("onReceivedError description=%s, failingUrl=%s", description, failingUrl)));
    }

    @TargetApi(android.os.Build.VERSION_CODES.M)
    @Override
    public void onReceivedError(final WebView view, final WebResourceRequest request, final WebResourceError error) {
        dialog.cancel();
        handleError(new Exception(getErrorStatus(request.getUrl().toString())));
    }

    private String getErrorStatus(final String url) {
        final UrlQuerySanitizer sanitizer = new UrlQuerySanitizer(url);
        final String error = sanitizer.getValue(Constants.ERROR);
        String errorDescription = sanitizer.getValue(Constants.ERROR_DESCRIPTION);
        if (errorDescription == null) {
            errorDescription = sanitizer.getValue(Constants.DESCRIPTION);
        }
        if (errorDescription == null) {
            errorDescription = "An error occurred.";
        }
        return errorDescription;
    }

    @Override
    public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
        super.shouldOverrideUrlLoading(view, url);
        Log.i(TAG, String.format("Loading URL=%s", url));
        this.progressBar.setVisibility(View.VISIBLE);
        if (url.contains(Constants.CODE_PARAM) && url.contains(Constants.STATE_PARAM)) {
            view.loadUrl(url);
            Toast.makeText(view.getContext(), view.getContext().getString(R.string.dialog_closed), Toast.LENGTH_SHORT).show();
            return true;
        } else {
            view.loadUrl(url);
            return false;
        }
    }

    @Override
    public void onPageFinished(final WebView view, final String url) {
        super.onPageFinished(view, url);
        Log.i(TAG, String.format("onPageFinished=%s", url));
        this.progressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean qualifyUrl(String url) {
        Log.i(TAG, String.format("Qualifying Authentication Url=%s", url));
        return url.contains(Constants.CODE);
    }

    @Override
    public void handleError(Exception e) {
        Log.i(TAG, "Authentication Failed." + e.getMessage());
    }

}
