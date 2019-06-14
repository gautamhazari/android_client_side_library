package com.gsma.mobileconnect.r2.android.application.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
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

import static com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants.ADDRESS;
import static com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants.AUTHN;
import static com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants.AUTHZ;
import static com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants.COMMON_DISCOVERY;
import static com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants.DUBLIN_DISCOVERY;
import static com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants.EMAIL;
import static com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants.IDENTITY_NATIONALID;
import static com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants.IDENTITY_PHONE;
import static com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants.IDENTITY_SIGNUP;
import static com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants.MC_DI_R2_V2_3;
import static com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants.MC_V1_1;
import static com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants.MC_V2_0;
import static com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants.OFFLINE_ACCESS;
import static com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants.OPENID;
import static com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants.PHONE;
import static com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants.PROFILE;
import static com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants.SINGAPORE_DISCOVERY;

public abstract class BaseFragment extends Fragment implements OnBackPressedListener, ICallback {
    private static final String TAG = CommonFragment.class.getSimpleName();

    protected Button btnMobileConnect;
    protected LinearLayout layoutMsisdn;
    protected LinearLayout layoutMcc;
    protected LinearLayout layoutMnc;
    protected LinearLayout layoutIpAddress;
    protected RadioButton rbMsisdn;
    protected RadioButton rbMccMnc;
    protected RadioButton rbNone;
    protected EditText tvMsisdn;
    protected EditText tvMcc;
    protected EditText tvMnc;
    protected EditText tvIpAddress;
    protected CheckBox cbIp;
    protected CheckBox ignoreAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_with_discovery_app, container, false);
        init(view);

        return view;
    }

    protected void onCreateSetter(final View view) {
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
    }

    protected void onClickMobileConnectButton() {
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
                startDiscoverySession(msisdn, mcc, mnc, ipAddress);
        }
        });
    }

    protected abstract void startDiscoverySession(String msisdn, String mcc, String mnc, String ip);

    /**
     * Initialization of UI elements at the current fragment.
     *
     * @param view
     */
    protected void init(View view) {
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
    protected void checkRadioButtonsAndSetView(View view) {
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

    protected void hideInput(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
