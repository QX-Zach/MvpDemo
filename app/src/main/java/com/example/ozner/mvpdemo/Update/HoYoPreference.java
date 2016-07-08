package com.example.ozner.mvpdemo.Update;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by xinde on 2016/2/26.
 */
public class HoYoPreference {
    public final static String HoYo = "hoyo";
    public final static String ServerAddress = "serveraddress";
    public final static String UserToken = "usertoken";
    public final static String UserID = "userid";

    private static SharedPreferences Init(Context context) {
        if (context != null)
            return context.getSharedPreferences(HoYo, Context.MODE_PRIVATE);
        else return null;
    }

    private static SharedPreferences.Editor InitEditor(Context context) {
        if (context != null)
            return context.getSharedPreferences(HoYo, Context.MODE_PRIVATE).edit();
        else
            return null;
    }

    public static void setUserToken(Context mContext, String usertoken) {
        SharedPreferences.Editor hoyoet = InitEditor(mContext);
        hoyoet.putString(UserToken, usertoken);
        hoyoet.commit();
    }

    public static String getUserToken(Context mContext) {
        if (mContext != null) {
            SharedPreferences hoyo = Init(mContext);
            return hoyo.getString(UserToken, null);
        } else {
            return null;
        }
    }

    public static void SetValue(Context mycontex, String key, String value) {
        SharedPreferences.Editor hoyoet = InitEditor(mycontex);
        hoyoet.putString(key, value);
        hoyoet.commit();
    }

    public static String GetValue(Context mycontext, String key, String defaultValue) {
        SharedPreferences hoyo = Init(mycontext);
        return hoyo.getString(key, defaultValue);
    }


    public static String ServerAddress(Context mycontext) {
        SharedPreferences hoyo = Init(mycontext);
        if (hoyo != null) {
            String server = hoyo.getString(ServerAddress, null);
            if (server == null) {
                SharedPreferences.Editor ethoyo = InitEditor(mycontext);
                ethoyo.putString(ServerAddress, "http://wechat.hoyofuwu.com/");
                return "http://wechat.hoyofuwu.com/";
            } else
                return server;
        }
        return "http://wechat.hoyofuwu.com/";
    }
}
