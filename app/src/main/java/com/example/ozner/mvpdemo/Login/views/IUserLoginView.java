package com.example.ozner.mvpdemo.Login.views;

import com.example.ozner.mvpdemo.Login.bean.User;

/**
 * Created by ozner_67 on 2016/4/18.
 */
public interface IUserLoginView {
    String getUserName();

    String getPassword();

    void clearUserName();

    void clearPassword();

    void showLoading();

    void hideLoading();

    void toActivity(User user);

    void shoFailedError();
}
