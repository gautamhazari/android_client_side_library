package com.gsma.mobileconnect.r2.android.clientsidelibrary.webviewclient;


import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gsma.mobileconnect.r2.android.clientsidelibrary.R;
import com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants;
import com.gsma.mobileconnect.r2.android.clientsidelibrary.interfaces.IAuthenticationWebViewClient;
import com.gsma.mobileconnect.r2.android.clientsidelibrary.interfaces.ICallback;
import com.gsma.mobileconnect.r2.android.clientsidelibrary.utils.StringUtils;
import com.gsma.mobileconnect.r2.android.clientsidelibrary.view.DiscoveryAuthenticationDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * A custom {@link android.webkit.WebViewClient} to handle Authorization in a {@link android.webkit.WebView}
 */
public class AuthenticationWebViewClient extends WebViewClient implements IAuthenticationWebViewClient {

    private ProgressBar progressBar;
    private DiscoveryAuthenticationDialog dialog;
    private String redirectUrl;
    private ICallback callback;
    private Context activityContext;

    private static final String TAG = AuthenticationWebViewClient.class.getSimpleName();

    public AuthenticationWebViewClient(final DiscoveryAuthenticationDialog dialog, final ProgressBar progressBar,
                                final String redirectUrl, final Context context) {
        this.progressBar = progressBar;
        this.dialog = dialog;
        this.redirectUrl = redirectUrl;
        this.activityContext = context;
        this.callback = (ICallback) activityContext;
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
        } else if (url.contains(Constants.ERROR_PARAM)) {
            String error = Uri.parse(url).getQueryParameter(Constants.ERROR);
            String errorDescription = getErrorStatus(url);
            Log.w(TAG, String.format("Error while authorisation session: %s - %s", error, errorDescription));
            dialog.cancel();
            Toast.makeText(view.getContext(), String.format("Error occurred while authorisation session: %s, %s", error, errorDescription), Toast.LENGTH_SHORT).show();
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
        if (url.contains(Constants.CODE_PARAM) && url.contains(Constants.STATE_PARAM)) {
            view.evaluateJavascript(
                    "(function() { return ('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>'); })();",
                    new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String html) {
                            Log.d("HTML", html);
                            html = StringUtils.removeUTFCharacters(html);
                            String content = Jsoup.parse(html).body().getElementsByTag("pre").text();
                            try {
                                new JSONObject(content);
                                callback.onComplete(content);
                            } catch (JSONException ex) {
                                Log.d("doesn't content json", html);
                            }
                        }
                    });
        }
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
