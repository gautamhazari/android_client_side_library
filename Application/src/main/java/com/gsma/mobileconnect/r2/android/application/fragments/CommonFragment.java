package com.gsma.mobileconnect.r2.android.application.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.gsma.mobileconnect.r2.android.application.R;
import com.gsma.mobileconnect.r2.android.clientsidelibrary.handlers.SessionHandler;
import com.gsma.mobileconnect.r2.android.clientsidelibrary.utils.StringUtils;

import static com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants.ADDRESS;
import static com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants.AUTHN;
import static com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants.AUTHZ;
import static com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants.COMMON_DISCOVERY;
import static com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants.DUBLIN_DISCOVERY;
import static com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants.EMAIL;
import static com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants.IDENTITY_NATIONALID;
import static com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants.IDENTITY_PHONE;
import static com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants.IDENTITY_SIGNUP;
import static com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants.INDIAN_DISCOVERY;
import static com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants.MC_DI_R2_V2_3;
import static com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants.MC_V1_1;
import static com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants.MC_V2_0;
import static com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants.OFFLINE_ACCESS;
import static com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants.OPENID;
import static com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants.PHONE;
import static com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants.PROFILE;
import static com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants.SINGAPORE_DISCOVERY;


public class CommonFragment extends BaseFragment {


    private Spinner discoverySpinner;
    private Spinner versionSpinner;
    private Spinner commonScopesSpinner;
    private String[] discoveryUrls = {COMMON_DISCOVERY, SINGAPORE_DISCOVERY, DUBLIN_DISCOVERY};
    private String[] versions = {MC_V1_1, MC_V2_0, MC_DI_R2_V2_3};
    private String[] scopes11 = {OPENID, PROFILE, PHONE, OFFLINE_ACCESS, EMAIL, ADDRESS};
    private String[] scopes2 = {AUTHN, AUTHZ, IDENTITY_NATIONALID, IDENTITY_PHONE, IDENTITY_SIGNUP};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_with_discovery_app, container, false);
        init(view);

        createSpinner();

        onCreateSetter(view);

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
                SessionHandler sessionHandler = new SessionHandler();
                sessionHandler.startDiscoverySession(getContext(), msisdn, mcc, mnc, ipAddress,
                        getString(R.string.server_endpoint_with_discovery_endpoint),
                        discoverySpinner.getSelectedItem().toString(),
                        commonScopesSpinner.getSelectedItem().toString(),
                        versionSpinner.getSelectedItem().toString(), ignoreAuth.isChecked());
            }
        });

        onClickMobileConnectButton();
        return view;
    }

    @Override
    protected void startDiscoverySession(String msisdn, String mcc, String mnc, String ip) {
        SessionHandler sessionHandler = new SessionHandler();
        sessionHandler.startDiscoverySession(getContext(), msisdn, mcc, mnc, ip,
                getString(R.string.server_endpoint_with_discovery_endpoint),
                discoverySpinner.getSelectedItem().toString(),
                commonScopesSpinner.getSelectedItem().toString(),
                versionSpinner.getSelectedItem().toString(), ignoreAuth.isChecked());
    }

    private void createSpinner() {
        ArrayAdapter<String> discoveryAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, discoveryUrls);
        discoverySpinner.setAdapter(discoveryAdapter);
        discoverySpinner.setSelection(0);
        discoverySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ArrayAdapter<String> versionAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, versions);
        versionSpinner.setAdapter(versionAdapter);
        versionSpinner.setSelection(0);
        versionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                createScopeSpinner(versionSpinner.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void createScopeSpinner(String version) {
        String[] scopeArray;
        if (version.equals(MC_V1_1)) {
            scopeArray = scopes11;
        } else {
            scopeArray = scopes2;
        }
        ArrayAdapter<String> scopeAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, scopeArray);
        commonScopesSpinner.setAdapter(scopeAdapter);
        commonScopesSpinner.setSelection(0);
        commonScopesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    /**
     * Initialization of UI elements at the current fragment.
     *
     * @param view
     */
    protected void init(View view) {
        super.init(view);
        discoverySpinner = view.findViewById(R.id.discoverySpinner);
        versionSpinner = view.findViewById(R.id.versionSpinner);
        commonScopesSpinner = view.findViewById(R.id.commonScopesSpinner);
    }





}
