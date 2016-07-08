package com.example.ozner.mvpdemo.Update.HttpHelper.bean;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by xinde on 2016/2/26.
 */
public class NetJsonObject implements Serializable {
    public NetJsonObject() {
    }

    public int state = -1;
    public String value;

    public JSONObject getJSONObject() {
        try {
            return new JSONObject(value);
        } catch (Exception ex) {
            return null;
        }
    }
}