package com.gsma.mobileconnect.r2.android.clientside.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gsma.mobileconnect.r2.android.clientside.R;

import org.json.JSONException;
import org.json.JSONObject;


public class ResultsActivity extends BaseActivity {

    private static final String TAG = ResultsActivity.class.getSimpleName();

    private TextView tvTokenId;
    private TextView tvStatus;
    private TextView tvAccessToken;
    private Toolbar toolbar;
    private LinearLayout userInfoLayout;
    private LinearLayout identityLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        configureToolbar(toolbar, getString(R.string.results_activity_title));
        init();
        userInfoLayout.setVisibility(View.GONE);
        identityLayout.setVisibility(View.GONE);
        displayResponse(getIntent().getExtras().getString(getString(R.string.results_key)));
    }

    protected void init() {
        tvTokenId = (TextView) findViewById(R.id.txbTokenId);
        tvStatus = (TextView) findViewById(R.id.txbStatus);
        tvAccessToken = (TextView) findViewById(R.id.txbAccessToken);
        userInfoLayout = (LinearLayout) findViewById(R.id.layout_user_info);
        identityLayout = (LinearLayout) findViewById(R.id.layout_identity);
    }

    private void displayIdentityResponse(final JSONObject jsonObject, final boolean isUserInfo) {
        try {
            if (isUserInfo) {
                userInfoLayout.setVisibility(View.VISIBLE);
            } else {
                if (userInfoLayout.getVisibility()==View.GONE) {
                    identityLayout.setVisibility(View.VISIBLE);
                }
            }
            for (int i = 0; i < jsonObject.names().length(); i++) {
                final String name = (String) jsonObject.names().get(i);
                String value;
                if (jsonObject.names().get(i).equals(getString(R.string.address))) {
                    JSONObject addressObject = new JSONObject(jsonObject.get(name).toString());
                    for (int j = 0; j < addressObject.names().length(); j++) {
                        final String addressName = (String)addressObject.names().get(j);
                        String addressValue;
                        try {
                            addressValue = (String)addressObject.get(addressName);
                        }
                        catch (Exception exception) {
                            addressValue = getString(R.string.not_string_mess);
                        }
                        addElement(addressName, addressValue, isUserInfo);
                    }
                    continue;
                }
                try {
                    value = String.valueOf(jsonObject.get(name));
                }
                catch (Exception exception) {
                    value = getString(R.string.not_string_mess);
                }
                addElement(name, value, isUserInfo);
            }
        }
        catch (final JSONException e) {
            e.printStackTrace();
        }
    }

    private void addElement (String name, String value, boolean isUserInfo) {
        final TextView nameTextView = new TextView(this);
        nameTextView.setText(optimizeName(name));
        nameTextView.setTypeface(Typeface.DEFAULT_BOLD);
        final TextView valueTextView = new TextView(this);
        valueTextView.setText(value);

        if (isUserInfo) {
            final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(userInfoLayout
                    .getLayoutParams());
            params.leftMargin = 16;
            valueTextView.setLayoutParams(params);
            userInfoLayout.addView(nameTextView);
            userInfoLayout.addView(valueTextView);
        }
        else {
            final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(identityLayout
                    .getLayoutParams());
            params.leftMargin = 16;
            valueTextView.setLayoutParams(params);
            identityLayout.addView(nameTextView);
            identityLayout.addView(valueTextView);
        }
    }

    private String optimizeName (String name) {
        name = name.replaceAll("_", " ");
        char [] chars = name.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == ' ') {
                chars[i+1] = Character.toUpperCase(chars[i+1]);
            }
        }
        return String.valueOf(chars);
    }

    private void displayResponse(String results) {
        JSONObject resultsJSON = createJSONObject(results);
        JSONObject tokenJSON = getTokenJSON(resultsJSON);
        if (resultsJSON != null) {
            displayValue(tvAccessToken, getString(R.string.access_token_key), getString(R.string.failed_receive_token), tokenJSON);
            displayValue(tvTokenId, getString(R.string.id_token_key), getString(R.string.failed_receive_id_token), tokenJSON);
            displayValue(tvStatus, getString(R.string.status_key), getString(R.string.empty_string), resultsJSON);
            if (resultsJSON.has(getString(R.string.identity_key))) {
                displayIdentityResponse(createJObjectFromJObject(resultsJSON, getString(R.string.identity_key)), false);
            } else if (resultsJSON.has(getString(R.string.user_info_key))) {
                displayIdentityResponse(createJObjectFromJObject(resultsJSON, getString(R.string.user_info_key)), true);
            }
        }
    }

    private JSONObject createJObjectFromJObject(JSONObject jsonObject, String name) {
        try {
            return jsonObject.getJSONObject(name);
        } catch (JSONException e) {
            Log.d(TAG, "Unable to get json");
        }
        return null;
    }

    private JSONObject getTokenJSON(JSONObject jsonObject) {
        try {
            return jsonObject.getJSONObject(getString(R.string.token_key));
        } catch (JSONException e) {
            Log.d(TAG, "Unable to get token object from JSON");
            e.printStackTrace();
            return null;
        }
    }

    private JSONObject createJSONObject(String json) {
        try {
            return new JSONObject(json);
        } catch (JSONException e) {
            Log.d(TAG, "Not valid json");
        }
        return null;
    }

    private void displayValue(TextView textView, String parameter, String failMessage, JSONObject resultsJson) {
        try {
            String value = resultsJson.getString(parameter);
            if (value != null) {
                textView.setText(value);
            } else {
                textView.setText(failMessage);
            }
        } catch (JSONException e) {
            Log.d(TAG, "Error occurred parsing " + parameter);
            e.printStackTrace();
        }
    }
}
