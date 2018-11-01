package com.gsma.mobileconnect.r2.android.clientside.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gsma.mobileconnect.r2.android.clientside.R;
import com.gsma.mobileconnect.r2.android.clientside.utils.HttpUtils;
import com.gsma.mobileconnect.r2.android.clientside.utils.StringUtils;
import com.gsma.mobileconnect.r2.android.clientside.view.MobileConnectView;

import org.apache.http.NameValuePair;

import java.util.List;

public class WithoutDiscoveryAppFragment extends Fragment {

    private Button btnMobileConnect;
    private LinearLayout layoutMsisdn;
    private EditText tvMsisdn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_without_discovery_app, container, false);
        init(view);
        btnMobileConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msisdn = tvMsisdn.getText().toString();
                if (!StringUtils.isNullOrEmpty(msisdn)) {
                    sendWithoutDiscoveryRequest(msisdn);
                } else {
                    Toast.makeText(getContext(), "MSISDN is empty", Toast.LENGTH_SHORT).show();
                    sendWithoutDiscoveryRequest("");
                }
            }
        });
        return view;
    }

    private void sendWithoutDiscoveryRequest(String msisdn) {
        List<NameValuePair> params = HttpUtils.prepareParameters(msisdn);
        final String requestUrl = HttpUtils.createGetWithParams(getString(R.string.server_endpoint_without_discovery_endpoint), params);
        MobileConnectView mobileConnectView = new MobileConnectView();
        mobileConnectView.startAuthentication(getContext(), requestUrl);
    }

    private void init(View view) {
        layoutMsisdn = view.findViewById(R.id.layoutMsisdnWD);
        tvMsisdn = view.findViewById(R.id.txbMsisdnIdWD);
        btnMobileConnect = view.findViewById(R.id.btnMCDemoWD);
    }
}
