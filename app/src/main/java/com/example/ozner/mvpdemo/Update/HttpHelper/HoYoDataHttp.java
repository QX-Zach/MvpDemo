package com.example.ozner.mvpdemo.Update.HttpHelper;

import android.content.Context;
import android.util.Log;

import com.example.ozner.mvpdemo.Update.HttpHelper.bean.NetJsonObject;
import com.example.ozner.mvpdemo.Utils.LogUtilsLC;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;

/**
 * Created by xinde on 2016/2/26.
 * 获取网络数据对象
 */
public class HoYoDataHttp {
    /*
    *对应服务器接口 获取JSONObject对象
     */
    public static NetJsonObject HoYoWebServer(Context context, String url, List<NameValuePair> params) {
        NetJsonObject result = new NetJsonObject();
        result.state = -1;
        String response = CsirHttp.postGetString(url, params, 15000);
        if (LogUtilsLC.APP_DBG)
            Log.e("hoyo", "HoYoWebServer:" + response);
        if (response != null) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                result.state = Integer.parseInt(jsonObject.get("state").toString());
                result.value = response;
//                if (result.state == -10015) {
//                    HoYoPreference.setUserToken(context, "");
//                    HoYoPreference.SetValue(context, HoYoPreference.UserID, "0");
//                    Intent broadIntent = new Intent(BroadCastAction.NeedReLogin);
//                    ((Activity) context).sendBroadcast(broadIntent);
//                    ((Activity) context).startActivity(new Intent(context, LoginActivity.class));
//                    ((Activity) context).finish();
//                    return null;
//                }
//                return result;
            } catch (Exception ex) {
//                result.state = -1;
                ex.printStackTrace();
//                return result;
            } finally {
                return result;
            }
        }
        return result;
    }
}
