package com.gsma.mobileconnect.r2.android.clientside.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.gsma.mobileconnect.r2.android.clientside.utils.SettingsUtils;

/**
 * A custom {@link WebView} to enable Javascript
 */
public class InteractiveWebView extends WebView {

    public InteractiveWebView(final Context context) {
        super(context);
        initialise();
    }

    public InteractiveWebView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        initialise();
    }

    public InteractiveWebView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialise();
    }

    private void initialise() {
        if (!isInEditMode()) {
            if (Build.VERSION.SDK_INT >= 19) {
                setLayerType(View.LAYER_TYPE_HARDWARE, null);
            }
            else {
                setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }

            setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
            setFocusable(true);
            setFocusableInTouchMode(true);
            requestFocus(View.FOCUS_DOWN);
            getSettings().setJavaScriptEnabled(true);
            getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            getSettings().setSupportMultipleWindows(false);
            getSettings().setDomStorageEnabled(true);
            getSettings().setDatabaseEnabled(true);
            getSettings().setSupportZoom(false);
            getSettings().setUseWideViewPort(false);
//            final WebSettings settings = SettingsUtils.getDefaultWebSettings(getSettings());
        }
    }
}
