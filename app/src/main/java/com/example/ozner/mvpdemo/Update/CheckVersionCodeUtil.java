package com.example.ozner.mvpdemo.Update;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.ozner.mvpdemo.Update.HttpHelper.HoYoDataHttp;
import com.example.ozner.mvpdemo.Update.HttpHelper.bean.NetJsonObject;
import com.example.ozner.mvpdemo.Utils.LogUtilsLC;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by ozner_67 on 2016/7/8.
 */
public class CheckVersionCodeUtil {

    /**
     * 获取软件版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
            if (LogUtilsLC.APP_DBG)
                Log.e("tag", "updateManage:curVersion: " + versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /*
    *检查网络版本
     */
    public static void CheckNetVersion(final Context context, final Handler mhandler, final int msgWhat) {
        final String checkVerUrl = HoYoPreference.ServerAddress(context) + "/Command/NewVersion";
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<NameValuePair> pars = new ArrayList<NameValuePair>();
                pars.add(new BasicNameValuePair("code", String.valueOf(getVersionCode(context))));
                pars.add(new BasicNameValuePair("os", "android"));
                pars.add(new BasicNameValuePair("appname", "com.hoyo.ozner.hoyoproject"));
                NetJsonObject result = HoYoDataHttp.HoYoWebServer(context, checkVerUrl, pars);
                Message message = new Message();
                message.what = msgWhat;
                message.obj = result;
                mhandler.sendMessage(message);
            }
        }).start();
    }

    /*
    *进行更新
     */
    public static void doUpdate(Context context, Class<?> cls, String downloadURL) {
        Intent intent = new Intent(context, cls);
        intent.putExtra("downloadURL", downloadURL);
        context.startActivity(intent);
    }
}
