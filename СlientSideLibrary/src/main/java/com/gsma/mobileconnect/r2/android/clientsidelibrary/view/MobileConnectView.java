package com.gsma.mobileconnect.r2.android.clientsidelibrary.view;


import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.gsma.mobileconnect.r2.android.clientsidelibrary.R;
import com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants;
import com.gsma.mobileconnect.r2.android.clientsidelibrary.utils.RequestUtils;
import com.gsma.mobileconnect.r2.android.clientsidelibrary.webviewclient.AuthenticationWebViewClient;

import java.util.Map;


public class MobileConnectView {

    private static final String TAG = MobileConnectView.class.getSimpleName();

    public void startAuthentication(@NonNull Context activityContext, @NonNull String authenticationUrl) {
        initWebView(activityContext, authenticationUrl);
    }

    private void initWebView(@NonNull Context activityContext, @NonNull String authenticationUrl) {
        final RelativeLayout webViewLayout = (RelativeLayout) LayoutInflater.from(activityContext)
                .inflate(R.layout.layout_web_view, null);
        final InteractiveWebView webView = webViewLayout.findViewById(R.id.web_view);
        final ProgressBar progressBar = webViewLayout.findViewById(R.id.progressBar);
        final DiscoveryAuthenticationDialog dialog = new DiscoveryAuthenticationDialog(activityContext);

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(final DialogInterface dialogInterface)
            {
                Log.i(TAG, "Closing Dialog");
                closeWebView(webView);
            }
        });
        dialog.setContentView(webViewLayout);
        webView.setWebChromeClient(new WebChromeClient());
        Uri uri = Uri.parse(authenticationUrl);
        String redirectUrl = uri.getQueryParameter("redirect_uri");
        final AuthenticationWebViewClient webViewClient = new AuthenticationWebViewClient(dialog, progressBar, redirectUrl, activityContext);
        Map<String, String> additionalHttpHeaders = new ArrayMap<>();
        String version = String.format("Android-%s", RequestUtils.getCurrentLibVersion());
        additionalHttpHeaders.put(Constants.CLIENT_SIDE_VERSION_HEADER, version);
        webView.setWebViewClient(webViewClient);
        webView.loadUrl(authenticationUrl, additionalHttpHeaders);

        try {
            dialog.show();
        } catch (final WindowManager.BadTokenException exception) {
            Log.i(TAG, String.format("Failed to show Dialog", exception));
        }
    }

    /**
     * Close {@link InteractiveWebView}.
     * @param webView custom web view.
     */
    private void closeWebView(final InteractiveWebView webView) {
        Log.i(TAG, "Closing WebView");
        webView.stopLoading();
        webView.loadData(Constants.EMPTY_STRING, Constants.MimeTypes.TEXT_HTML, null);
    }
}
