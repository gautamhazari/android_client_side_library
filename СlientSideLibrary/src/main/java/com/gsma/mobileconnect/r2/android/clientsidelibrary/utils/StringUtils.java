package com.gsma.mobileconnect.r2.android.clientsidelibrary.utils;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static String removeUTFCharacters(String data){
        Pattern p = Pattern.compile("\\\\u(\\p{XDigit}{4})");
        Matcher m = p.matcher(data);
        StringBuffer buf = new StringBuffer(data.length());
        while (m.find()) {
            String ch = String.valueOf((char) Integer.parseInt(m.group(1), 16));
            m.appendReplacement(buf, Matcher.quoteReplacement(ch));
        }
        m.appendTail(buf);
        return buf.toString().replaceAll("\\\\", "");
    }

}
