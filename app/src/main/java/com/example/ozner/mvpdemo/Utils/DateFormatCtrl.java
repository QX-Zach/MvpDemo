package com.example.ozner.mvpdemo.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ozner_67 on 2016/3/23.
 */
public class DateFormatCtrl {
    private static SimpleDateFormat shortFomat = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static String format(Date date) {
        return dateFormat.format(date);
    }

    public static String formatShortDate(Date date) {
        return shortFomat.format(date);
    }

    public static String getWeekXQ(Calendar calendar) {
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        StringBuffer stringBuffer = new StringBuffer();
        switch (dayOfWeek) {
            case 1:
                stringBuffer.append("星期日");
                break;
            case 2:
                stringBuffer.append("星期一");
                break;
            case 3:
                stringBuffer.append("星期二");
                break;
            case 4:
                stringBuffer.append("星期三");
                break;
            case 5:
                stringBuffer.append("星期四");
                break;
            case 6:
                stringBuffer.append("星期五");
                break;
            case 7:
                stringBuffer.append("星期六");
                break;
        }
        return stringBuffer.toString();
    }

    /*
    *订单列表倒计时使用，将倒计时时间转换成字符串,格式：HH:MM:SS
     */
    public static String getTimeStr(long seconds) {
        StringBuilder stringBuilder = new StringBuilder();
        long hour;
        long min;
        long sec;

        hour = seconds / 3600;
        min = (seconds % 3600) / 60;
        sec = (seconds % 3600) % 60;

        if (hour > 0) {
            stringBuilder.append(String.valueOf(hour));
            stringBuilder.append(":");
        }
        if (min < 10) {
            stringBuilder.append("0");
        }
        stringBuilder.append(min);
        stringBuilder.append(":");
        if (sec < 10) {
            stringBuilder.append("0");
        }
        stringBuilder.append(sec);


        return stringBuilder.toString();
    }
}
