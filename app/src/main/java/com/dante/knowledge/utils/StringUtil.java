package com.dante.knowledge.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * deals with string work like copy, parse.
 */
public class StringUtil {

    public static final String LAST_DATE= "lastDate";

    public static String parseDate(String time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        formatter.setLenient(false);
        Date date = null;
        try {
            date = formatter.parse(time);
            return formatDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
    public static String formatDate(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return formatter.format(date);
    }
}
