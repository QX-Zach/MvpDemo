package com.example.ozner.mvpdemo.Login.bean;

/**
 * Created by ozner_67 on 2016/4/18.
 */
public interface OnLoginListener {
    void loginSuccess(User user);
    void loginFailed();
}
