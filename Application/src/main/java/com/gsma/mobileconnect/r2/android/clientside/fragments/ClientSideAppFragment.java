package com.gsma.mobileconnect.r2.android.clientside.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.gsma.mobileconnect.r2.android.clientside.R;
import com.gsma.mobileconnect.r2.android.clientside.activity.ResultsActivity;
import com.gsma.mobileconnect.r2.android.clientside.filters.InputFilters;
import com.gsma.mobileconnect.r2.android.clientside.interfaces.ICallback;
import com.gsma.mobileconnect.r2.android.clientside.interfaces.OnBackPressedListener;
import com.gsma.mobileconnect.r2.android.clientside.utils.HttpUtils;
import com.gsma.mobileconnect.r2.android.clientside.utils.IpUtils;
import com.gsma.mobileconnect.r2.android.clientside.utils.NetworkUtils;
import com.gsma.mobileconnect.r2.android.clientside.utils.StringUtils;
import com.gsma.mobileconnect.r2.android.clientside.view.MobileConnectView;

import org.apache.http.NameValuePair;

import java.util.List;


public class ClientSideAppFragment extends Fragment implements OnBackPressedListener, ICallback {

    private static final String TAG = ClientSideAppFragment.class.getSimpleName();

    private Button btnMobileConnect;
    private LinearLayout layoutMsisdn;
    private LinearLayout layoutMcc;
    private LinearLayout layoutMnc;
    private LinearLayout layoutIpAddress;
    private RadioButton rbMsisdn;
    private RadioButton rbMccMnc;
    private RadioButton rbNone;
    private EditText tvMsisdn;
    private EditText tvMcc;
    private EditText tvMnc;
    private EditText tvIpAddress;
    private CheckBox cbIp;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_client_side_app, container, false);
        init(view);

        cbIp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (cbIp.isChecked()) {
                    layoutIpAddress.setVisibility(View.VISIBLE);
                } else {
                    layoutIpAddress.setVisibility(View.GONE);
                }
            }
        });

        tvMcc.setText(NetworkUtils.getMCC(getContext()));
        tvMnc.setText(NetworkUtils.getMNC(getContext()));
        tvMsisdn.setText(getResources().getString(R.string.msisdn));
        tvIpAddress.setText(IpUtils.getIPAddress(true));
        rbMsisdn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkRadioButtonsAndSetView(view);
            }
        });


        rbMccMnc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkRadioButtonsAndSetView(view);
            }
        });

        rbNone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkRadioButtonsAndSetView(view);
            }
        });

        tvIpAddress.setFilters(InputFilters.getIpFilter());

        btnMobileConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msisdn = null;
                String mcc = null;
                String mnc = null;
                String ipAddress = null;
                if (cbIp.isChecked()) {
                    if (!StringUtils.isNullOrEmpty(tvIpAddress.getText().toString())) {
                        ipAddress = tvIpAddress.getText().toString();
                    } else {
                        Toast.makeText(getContext(), getString(R.string.type_your_ip_address), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (rbMsisdn.isChecked()) {
                    if (!StringUtils.isNullOrEmpty(tvMsisdn.getText().toString())) {
                        msisdn = tvMsisdn.getText().toString();
                    } else {
                        Toast.makeText(getContext(), getString(R.string.type_your_msisdn), Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else if (rbMccMnc.isChecked()) {
                    if (!StringUtils.isNullOrEmpty(tvMcc.getText().toString()) && !StringUtils.isNullOrEmpty(tvMnc.getText().toString())) {
                        mcc = tvMcc.getText().toString();
                        mnc = tvMnc.getText().toString();
                    } else if (StringUtils.isNullOrEmpty(tvMnc.getText().toString())) {
                        Toast.makeText(getContext(), getString(R.string.type_your_mnc), Toast.LENGTH_SHORT).show();
                        return;
                    } else if (StringUtils.isNullOrEmpty(tvMcc.getText().toString())) {
                        Toast.makeText(getContext(), getString(R.string.type_your_mcc), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                sendDiscoveryRequest(msisdn, mcc, mnc, ipAddress);
            }
        });
        return view;
    }

    /**
     * Initialization of UI elements at the current fragment.
     * @param view
     */
    private void init(View view) {
        cbIp = (CheckBox)view.findViewById(R.id.cbIp);
        layoutMsisdn = (LinearLayout)view.findViewById(R.id.layoutMsisdn);
        layoutMcc = (LinearLayout)view.findViewById(R.id.layoutMcc);
        layoutMnc = (LinearLayout)view.findViewById(R.id.layoutMnc);
        layoutIpAddress = (LinearLayout)view.findViewById(R.id.layoutIpAddress);
        rbMsisdn = (RadioButton)view.findViewById(R.id.rbMsisdn);
        rbMccMnc = (RadioButton)view.findViewById(R.id.rbMccMnc);
        rbNone = (RadioButton)view.findViewById(R.id.rbNone);
        tvMsisdn = (EditText) view.findViewById(R.id.txbMsisdn);
        tvMcc = (EditText)view.findViewById(R.id.txbMcc);
        tvMnc = (EditText)view.findViewById(R.id.txbMnc);
        tvIpAddress = (EditText)view.findViewById(R.id.txbIpAddress);
        btnMobileConnect = (Button)view.findViewById(R.id.btnMCDemo);
    }

    /**
     * Describes actions when user clicks 'Back' button.
     */
    @Override
    public void onBackPressed() {
       getActivity().onBackPressed();
    }


    /**
     * Checks wich radio button is selected and opens configuration view for it.
     * @param view
     */
    private void checkRadioButtonsAndSetView (View view) {
        if (rbMsisdn.isChecked()) {
            layoutMsisdn.setVisibility(View.VISIBLE);
            layoutMcc.setVisibility(View.GONE);
            layoutMnc.setVisibility(View.GONE);
        } else if (rbMccMnc.isChecked()){
            layoutMsisdn.setVisibility(View.GONE);
            layoutMcc.setVisibility(View.VISIBLE);
            layoutMnc.setVisibility(View.VISIBLE);
        } else {
            layoutMsisdn.setVisibility(View.GONE);
            layoutMcc.setVisibility(View.GONE);
            layoutMnc.setVisibility(View.GONE);
            layoutIpAddress.setVisibility(View.GONE);
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void sendDiscoveryRequest(String msisdn, String mcc, String mnc, String ipAddress) {
        List<NameValuePair> params = HttpUtils.prepareParameters(msisdn, mcc, mnc, ipAddress);
        final String requestUrl = HttpUtils.createGetWithParams(getString(R.string.server_endpoint), params);
        MobileConnectView mobileConnectView = new MobileConnectView();
        mobileConnectView.startAuthentication(getContext(), requestUrl);
    }

    /**
     * Waits for callback with the result and starts {@link ResultsActivity} putting in it the results.
     * @param result - response from server side application in JSON format.
     */
    @Override
    public void onComplete(String result) {
        final Intent intent = new Intent(getActivity(), ResultsActivity.class);
        intent.putExtra(getString(R.string.results_key), result);
        startActivity(intent);
    }
}
