package com.dante.knowledge.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Dante on 2016/1/31.
 */
public class StringUtil {
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
