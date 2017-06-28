package com.wincom.dcim.agentd.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by master on 6/21/17.
 */
public class DateFormat {
    private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    private static final SimpleDateFormat format = new SimpleDateFormat(TIMESTAMP_FORMAT, Locale.ENGLISH);

    public static Date parseTimestamp(String s) {
        try {
            return format.parse(s);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static String formatTimestamp(Date value) {
        return format.format(value);
    }
}
