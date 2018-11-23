package com.gsma.mobileconnect.r2.android.clientsidelibrary.filters;


import android.text.InputFilter;

import com.gsma.mobileconnect.r2.android.clientsidelibrary.constants.Constants;

/*
 Contains methods to for validating input text and preventing errors.
 */
public class InputFilters {

    //Regular expression to validate the IP address
    private final static String IP_EXPRESSION = "^\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?";
    private final static String IP_SEPARATOR = "\\.";
    private final static int IP_MAX_VALUE = 255;

    /**
     * Contains regulations of IP address.
     * @return filter for IP address.
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
                        String[] splits = resultingTxt.split(IP_SEPARATOR);
                        for (String split : splits) {
                            if (Integer.valueOf(split) > IP_MAX_VALUE) {
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
