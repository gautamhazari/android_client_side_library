package com.gsma.mobileconnect.r2.android.application.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gsma.mobileconnect.r2.android.application.R;
import com.gsma.mobileconnect.r2.android.clientsidelibrary.handlers.SessionHandler;
import com.gsma.mobileconnect.r2.android.clientsidelibrary.utils.StringUtils;

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
                SessionHandler sessionHandler = new SessionHandler();
                if (StringUtils.isNullOrEmpty(msisdn)) {
                    Toast.makeText(getContext(), "MSISDN is empty", Toast.LENGTH_SHORT).show();
                    msisdn = getString(R.string.empty_string);
                }
                sessionHandler.startWithoutDiscoverySession(getContext(), msisdn, getString(R.string.server_endpoint_without_discovery_endpoint));

            }
        });
        return view;
    }


    private void init(View view) {
        layoutMsisdn = view.findViewById(R.id.layoutMsisdnWD);
        tvMsisdn = view.findViewById(R.id.txbMsisdnIdWD);
        btnMobileConnect = view.findViewById(R.id.btnMCDemoWD);
    }
}
