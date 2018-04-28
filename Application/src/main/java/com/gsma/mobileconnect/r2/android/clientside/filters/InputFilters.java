package com.gsma.mobileconnect.r2.android.clientside.filters;


import android.text.InputFilter;

import com.gsma.mobileconnect.r2.android.clientside.constants.Constants;

/*
 Contains methods to for validating input text and preventing errors.
 */
public class InputFilters {

    //Expression to validate IP address
    private final static String IP_EXPRESSION = "^\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?";

    /**
     * Contains regulations of IP address.
     * @return filter fo IP adress.
     */
    public static InputFilter [] getIpFilter() {
        InputFilter[] inputFilter = new InputFilter[1];
        inputFilter[0] = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       android.text.Spanned dest, int dstart, int dend) {
                if (end > start) {
                    String destTxt = dest.toString();
                    String resultingTxt = destTxt.substring(0, dstart)
                            + source.subSequence(start, end)
                            + destTxt.substring(dend);
                    if (!resultingTxt
                            .matches(IP_EXPRESSION)) {
                        return Constants.EMPTY_STRING;
                    } else {
                        String[] splits = resultingTxt.split("\\.");
                        for (String split : splits) {
                            if (Integer.valueOf(split) > 255) {
                                return Constants.EMPTY_STRING;
                            }
                        }
                    }
                }
                return null;
            }

        };
        return inputFilter;
    }
}
