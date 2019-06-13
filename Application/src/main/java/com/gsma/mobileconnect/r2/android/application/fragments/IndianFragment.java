package com.gsma.mobileconnect.r2.android.application.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.gsma.mobileconnect.r2.android.application.R;
import com.gsma.mobileconnect.r2.android.application.activity.ResultsActivity;
import com.gsma.mobileconnect.r2.android.application.interfaces.OnBackPressedListener;
import com.gsma.mobileconnect.r2.android.clientsidelibrary.filters.InputFilters;
import com.gsma.mobileconnect.r2.android.clientsidelibrary.handlers.SessionHandler;
import com.gsma.mobileconnect.r2.android.clientsidelibrary.interfaces.ICallback;
import com.gsma.mobileconnect.r2.android.clientsidelibrary.utils.IpUtils;
import com.gsma.mobileconnect.r2.android.clientsidelibrary.utils.StringUtils;

import static com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants.INDIAN_DISCOVERY;
import static com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants.MC_ATTR_VM_SHARE;
import static com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants.MC_ATTR_VM_SHARE_HASH;
import static com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants.MC_INDIA_TC;
import static com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants.MC_MNV_VALIDATE;
import static com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants.MC_MNV_VALIDATE_PLUS;
import static com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants.MC_V1_1;
import static com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants.OPENID;

public class IndianFragment extends Fragment  implements OnBackPressedListener, ICallback {

    private static final String TAG = IndianFragment.class.getSimpleName();

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
    private CheckBox ignoreAuth;
    private Spinner scopeSpinner;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_indian, container, false);
        init(view);

        createSpinner();

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

        tvMcc.setText("55");
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
                sessionHandler.startDiscoverySession(getContext(), msisdn, mcc, mnc, ipAddress,
                        getString(R.string.server_endpoint_with_discovery_endpoint),
                        INDIAN_DISCOVERY,
                        scopeSpinner.getSelectedItem().toString(), MC_V1_1, ignoreAuth.isChecked());
            }
        });
        return view;
    }

    private void createSpinner() {
        String[] data = {OPENID, MC_INDIA_TC, MC_ATTR_VM_SHARE, MC_ATTR_VM_SHARE_HASH, MC_MNV_VALIDATE, MC_MNV_VALIDATE_PLUS};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, data);
        scopeSpinner.setAdapter(adapter);
//        scopeSpinner.setPrompt("Set scope");
        scopeSpinner.setSelection(0);
        scopeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getContext(), "Position = " + position, Toast.LENGTH_SHORT).show();
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
    private void init(View view) {
        cbIp = view.findViewById(R.id.cbIp);
        ignoreAuth = view.findViewById(R.id.ignoreAuth);
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
        scopeSpinner = view.findViewById(R.id.spinnerScope);
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



    /**
     * Waits for callback with the result and starts {@link ResultsActivity} putting in it the results.
     *
     * @param result - response from server side application in JSON format.
     */
    @Override
    public void onComplete(String result) {
        final Intent intent = new Intent(getActivity(), ResultsActivity.class);
        intent.putExtra(getString(R.string.results_key), result);
        startActivity(intent);
    }

    private void hideInput(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
