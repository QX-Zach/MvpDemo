package com.example.ozner.mvpdemo.DateFormatTest;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by ozner_67 on 2016/5/4.
 */
public class ChatDateUtils {
    private static Calendar calendar = Calendar.getInstance();
    private static String timeFormat = "HH:MM";

    public static String getChatTime(long timeInMillis) {
        StringBuilder sb = new StringBuilder();
        calendar.setTimeInMillis(timeInMillis);
        SimpleDateFormat sf = new SimpleDateFormat(timeFormat);
        sb.append(sf.format(calendar.getTime()));

        return sb.toString();
    }
}
