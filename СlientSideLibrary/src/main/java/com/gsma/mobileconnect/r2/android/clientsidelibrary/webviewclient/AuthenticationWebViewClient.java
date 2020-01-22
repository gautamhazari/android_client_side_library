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

import com.gsma.mobileconnect.r2.android.clientsidelibrary.R;
import com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants;
import com.gsma.mobileconnect.r2.android.clientsidelibrary.interfaces.IAuthenticationWebViewClient;
import com.gsma.mobileconnect.r2.android.clientsidelibrary.interfaces.ICallback;
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
                            System.out.println("HERE!");
                            System.out.println(html);
//                            html = "{\"status\":\"failure\",\"action\":\"error\",\"clientName\":null,\"url\":null,\"sdkSession\":null,\"state\":null,\"nonce\":null,\"subscriberId\":null,\"token\":{\"timeReceived\":\"2020-01-22T12:28:22.375+0000\",\"accessToken\":\"73148a35-188d-4a54-9819-5a7b63f0041b\",\"tokenType\":\"Bearer\",\"idToken\":\"eyJhbGciOiJSUzI1NiIsImtpZCI6Im9wZXJhdG9yLWIiLCJ0eXAiOiJKV1QifQ.eyJhY3IiOiIyIiwiYW1yIjpbIlNJTV9PSyJdLCJhdF9oYXNoIjoiZ1NfTVV3LWJ5enhIY1VtakNFR2JxUSIsImF1ZCI6WyJObUkxWkRnM01HTXROakkwWlMwME5qUmlMV0k0TWpZdFltUXlNV0V4TXpVM056QXpPbTl3WlhKaGRHOXlMV0k9Il0sImF1dGhfdGltZSI6MTU3OTY5NjEwMS4wLCJhenAiOiJObUkxWkRnM01HTXROakkwWlMwME5qUmlMV0k0TWpZdFltUXlNV0V4TXpVM056QXpPbTl3WlhKaGRHOXlMV0k9IiwiZGlzcGxheWVkX2RhdGEiOiJkZW1vQXBwU0RLLWRlbW8gYmluZGluZy1kZW1vIGNvbnRleHQiLCJleHAiOjE1Nzk2OTYxNjEsImhhc2hlZF9sb2dpbl9oaW50IjoiYjQ2YjA3Mjc0NWVkOTVkNzNmOWFjMjJmOTAzYTc2YmQwMDYwNzE5YWI4ZTVhZTg2NDM4YWI1YTZmMTBkZDdiYiIsImlhdCI6MTU3OTY5NjEwMSwiaXNzIjoiaHR0cHM6Ly9vcGVyYXRvci1iLnNhbmRib3gubW9iaWxlY29ubmVjdC5pbyIsIm5vbmNlIjoiOGVmN2UxNmItMTJmYy00YzIwLWFmNjgtZmIwOTQ0MTQ2N2E4Iiwic3ViIjoiMThjYzNjM2ItMGMwNy00NDJhLWI0NWMtNDI4ZmUwNDUzM2UwIn0.Vv7ReAyP7RAFCSN93-4oUlkRRlAwtTFOS6X9NMtwdFMzzGCzX4LNvvc_eU3QFyHbiSiwTQPepFJBvBcOvv-Xg_mr0XDb7kBE70IJ-kIfk9IKA2Cpxk50f_tZlegmk3EBB_sah4dcBqgxPYyDlRxAVLpHZVIDRuQbFsDYen03CgaDX0tmoPQT7dQ_xvdeAOjrkuQS7ixLh3gmo5lSK5bSTb_Tx7GhUn967F-QXJmYlEUjVdBBPGHefIEpjYphFxsXFDzPv0Yd9Df5jDqPE5fBlwLjKHQIey2JC7YyfHvliFMFwdEENMNwBLNLPsdm4GzDTOI-xsuljjklWNQeGkZINw\",\"refreshToken\":null,\"expiry\":\"2020-01-22T12:29:22.375+0000\",\"expiresIn\":60,\"correlationId\":null},\"tokenValidated\":false,\"identity\":{\"sub\": \"18cc3c3b-0c07-442a-b45c-428fe04533e0\", \"phone_number\": \"+447700900911\", \"updated_at\": \"2020-01-22T12:28:22.950211\"},\"error\":\"Invalid Id Token\",\"description\":\"Token validation failed\",\"outcome\":null}";
                            try {
                                html = new String(html.getBytes(), StandardCharsets.US_ASCII.name());

                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            Document doc = Jsoup.parse(html);
                            String content = doc.body().getElementsByTag("pre").text();
                            System.out.println(content);
                            callback.onComplete(content);
                            try {
                                new JSONObject(content);
                            } catch (JSONException ex) {}
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
