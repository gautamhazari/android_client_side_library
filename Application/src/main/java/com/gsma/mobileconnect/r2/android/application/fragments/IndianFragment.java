package com.gsma.mobileconnect.r2.android.application.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.gsma.mobileconnect.r2.android.application.R;
import com.gsma.mobileconnect.r2.android.clientsidelibrary.handlers.SessionHandler;

import static com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants.INDIAN_DISCOVERY;
import static com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants.MC_ATTR_VM_SHARE;
import static com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants.MC_ATTR_VM_SHARE_HASH;
import static com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants.MC_INDIA_TC;
import static com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants.MC_MNV_VALIDATE;
import static com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants.MC_MNV_VALIDATE_PLUS;
import static com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants.MC_V1_1;
import static com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants.OPENID;

public class IndianFragment extends BaseFragment {




    private Spinner scopeSpinner;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_indian, container, false);
        init(view);

        createSpinner();

        onCreateSetter(view);

        onClickMobileConnectButton();
        return view;
    }

    @Override
    protected void startDiscoverySession(String msisdn, String mcc, String mnc, String ip) {
        SessionHandler sessionHandler = new SessionHandler();
        sessionHandler.startDiscoverySession(getContext(), msisdn, mcc, mnc, ip,
                getString(R.string.server_endpoint_with_discovery_endpoint),
                INDIAN_DISCOVERY, scopeSpinner.getSelectedItem().toString(),
                MC_V1_1, ignoreAuth.isChecked());
    }

    private void createSpinner() {
        String[] data = {OPENID, MC_INDIA_TC, MC_ATTR_VM_SHARE, MC_ATTR_VM_SHARE_HASH, MC_MNV_VALIDATE, MC_MNV_VALIDATE_PLUS};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, data);
        scopeSpinner.setAdapter(adapter);
        scopeSpinner.setSelection(0);
        scopeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        scopeSpinner = view.findViewById(R.id.spinnerScope);
    }

}
