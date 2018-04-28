package com.gsma.mobileconnect.r2.android.clientside.utils;


public class StringUtils {

    /**
     * Inspect a String for content.  Note that this method considers white space as content and
     * therefore a non-empty String.
     *
     * @param str to inspect.
     * @return true if the String is not null and has zero length.
     */
    public static boolean isNullOrEmpty(final String str) {
        return str == null || "".equals(str);
    }

}
