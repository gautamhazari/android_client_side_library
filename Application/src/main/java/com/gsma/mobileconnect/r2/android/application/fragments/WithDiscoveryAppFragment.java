package com.gsma.mobileconnect.r2.android.application.fragments;


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

import com.gsma.mobileconnect.r2.android.application.R;
import com.gsma.mobileconnect.r2.android.application.activity.ResultsActivity;
import com.gsma.mobileconnect.r2.android.application.interfaces.OnBackPressedListener;
import com.gsma.mobileconnect.r2.android.clientsidelibrary.filters.InputFilters;
import com.gsma.mobileconnect.r2.android.clientsidelibrary.handlers.SessionHandler;
import com.gsma.mobileconnect.r2.android.clientsidelibrary.interfaces.ICallback;
import com.gsma.mobileconnect.r2.android.clientsidelibrary.utils.IpUtils;
import com.gsma.mobileconnect.r2.android.clientsidelibrary.utils.StringUtils;


public class WithDiscoveryAppFragment extends Fragment implements OnBackPressedListener {

    private static final String TAG = WithDiscoveryAppFragment.class.getSimpleName();

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

        final View view = inflater.inflate(R.layout.fragment_with_discovery_app, container, false);
        init(view);

        System.out.println("on create view");

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

        tvMcc.setText(R.string.mcc_value);
        tvMnc.setText(R.string.mnc_value);
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
                SessionHandler sessionHandler = new SessionHandler();
                sessionHandler.startDiscoverySession(getContext(), msisdn, mcc, mnc, ipAddress, getString(R.string.server_endpoint_with_discovery_endpoint));
            }
        });
//        onComplete("{\"status\":\"failure\",\"action\":\"error\",\"clientName\":null,\"url\":null,\"sdkSession\":null,\"state\":null,\"nonce\":null,\"subscriberId\":null,\"token\":{\"timeReceived\":\"2020-01-21T10:52:11.288+0000\",\"accessToken\":\"5e52c906-6c65-496c-b719-6bcdbfbd4c65\",\"tokenType\":\"Bearer\",\"idToken\":\"eyJhbGciOiJSUzI1NiIsImtpZCI6Im9wZXJhdG9yLWIiLCJ0eXAiOiJKV1QifQ.eyJhY3IiOiIyIiwiYW1yIjpbIlNJTV9PSyJdLCJhdF9oYXNoIjoiUEotYTQtTXM3UmpNYXJWdTltV05sQSIsImF1ZCI6WyJObUkxWkRnM01HTXROakkwWlMwME5qUmlMV0k0TWpZdFltUXlNV0V4TXpVM056QXpPbTl3WlhKaGRHOXlMV0k9Il0sImF1dGhfdGltZSI6MTU3OTYwMzkzMC4wLCJhenAiOiJObUkxWkRnM01HTXROakkwWlMwME5qUmlMV0k0TWpZdFltUXlNV0V4TXpVM056QXpPbTl3WlhKaGRHOXlMV0k9IiwiZGlzcGxheWVkX2RhdGEiOiJkZW1vQXBwU0RLLWRlbW8gYmluZGluZy1kZW1vIGNvbnRleHQiLCJleHAiOjE1Nzk2MDM5OTAsImhhc2hlZF9sb2dpbl9oaW50IjoiMmNiYTIyMzMwNDE0OWJhYWI1ZWZjZTY1YjQyODBhMjI2NTJkZTVjYzYyOWI0NjQ0ODNiMTBjNmU2YTJmNmY2MSIsImlhdCI6MTU3OTYwMzkzMCwiaXNzIjoiaHR0cHM6Ly9vcGVyYXRvci1iLnNhbmRib3gubW9iaWxlY29ubmVjdC5pbyIsIm5vbmNlIjoiZmU2M2M4ZjUtYWQzNC00YzYyLTk4NmItNzc4OTk1OGU3NzllIiwic3ViIjoiMThjYzNjM2ItMGMwNy00NDJhLWI0NWMtNDI4ZmUwNDUzM2UwIn0.PZtiwnKmwnnK04wTzY7Q3euM5VJwSP4-Z7tCl42SEopLohbN9ciluiXpv1Utorsr_2RXUZ7h_G1Cmar1OliPdHYYjbh_WJOC4UtXA-tYe0tRRNHATM8lrQCkc1WNktEBY3KFvJ2d3-HJbMFj2Vv25Mow8QuKIYk5Z85aQFQYEh3J6R9AirFTM5zLXs2q5HHf8o9PTYTZTuZQKVuWIefs_O13X3eZMIbNMGKkXyqFS1Q4oSAnu32QaiRwKkiP8zDuL_owthOrQTJviRmakCd9cxM9X0BIPrIVj7oCGjrbFh8wpMtEuIt1On4zfThpiR054ywF1fWa-b89smbAU-uIkg\",\"refreshToken\":null,\"expiry\":\"2020-01-21T10:53:11.288+0000\",\"expiresIn\":60,\"correlationId\":null},\"tokenValidated\":false,\"identity\":{\"sub\": \"18cc3c3b-0c07-442a-b45c-428fe04533e0\", \"phone_number\": \"+447700900911\", \"updated_at\": \"2020-01-21T10:52:11.850687\"},\"error\":\"Invalid Id Token\",\"description\":\"Token validation failed\",\"outcome\":null}");
        return view;
    }

    /**
     * Initialization of UI elements at the current fragment.
     *
     * @param view
     */
    private void init(View view) {
        cbIp = view.findViewById(R.id.cbIp);
        layoutMsisdn = view.findViewById(R.id.layoutMsisdn);
        layoutMcc = view.findViewById(R.id.layoutMcc);
        layoutMnc = view.findViewById(R.id.layoutMnc);
        layoutIpAddress = view.findViewById(R.id.layoutIpAddress);
        rbMsisdn = view.findViewById(R.id.rbMsisdn);
        rbMccMnc = view.findViewById(R.id.rbMccMnc);
        rbNone = view.findViewById(R.id.rbNone);
        tvMsisdn = view.findViewById(R.id.txbMsisdn);
        tvMcc = view.findViewById(R.id.txbMcc);
        tvMnc = view.findViewById(R.id.txbMnc);
        tvIpAddress = view.findViewById(R.id.txbIpAddress);
        btnMobileConnect = view.findViewById(R.id.btnMCDemo);
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
     *
     * @param view
     */
    private void checkRadioButtonsAndSetView(View view) {
        if (rbMsisdn.isChecked()) {
            layoutMsisdn.setVisibility(View.VISIBLE);
            layoutMcc.setVisibility(View.GONE);
            layoutMnc.setVisibility(View.GONE);
        } else if (rbMccMnc.isChecked()) {
            layoutMsisdn.setVisibility(View.GONE);
            layoutMcc.setVisibility(View.VISIBLE);
            layoutMnc.setVisibility(View.VISIBLE);
        } else {
            layoutMsisdn.setVisibility(View.GONE);
            layoutMcc.setVisibility(View.GONE);
            layoutMnc.setVisibility(View.GONE);
            layoutIpAddress.setVisibility(View.GONE);
            hideInput(view);
        }
    }





    private void hideInput(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
