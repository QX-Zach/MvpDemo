package com.example.ozner.mvpdemo.Login.Presenter;

import android.os.Handler;

import com.example.ozner.mvpdemo.Login.bean.IUserBiz;
import com.example.ozner.mvpdemo.Login.bean.OnLoginListener;
import com.example.ozner.mvpdemo.Login.bean.User;
import com.example.ozner.mvpdemo.Login.bean.UserBiz;
import com.example.ozner.mvpdemo.Login.views.IUserLoginView;

/**
 * Created by ozner_67 on 2016/4/18.
 */
public class UserLoginPresenter {
    private IUserBiz userBiz;
    private IUserLoginView userLoginView;
    private Handler mHander = new Handler();

    public UserLoginPresenter(IUserLoginView userLoginView) {
        this.userLoginView = userLoginView;
        this.userBiz = new UserBiz();
    }

    public void login() {
        userLoginView.showLoading();
        userBiz.login(userLoginView.getUserName(), userLoginView.getPassword(), new OnLoginListener() {
            @Override
            public void loginSuccess(final User user) {
                mHander.post(new Runnable() {
                    @Override
                    public void run() {
                        userLoginView.toActivity(user);
                        userLoginView.hideLoading();
                    }
                });
            }

            @Override
            public void loginFailed() {
                //需要在UI线程执行
                mHander.post(new Runnable() {
                    @Override
                    public void run() {
                        userLoginView.shoFailedError();
                        userLoginView.hideLoading();
                    }
                });
            }
        });
    }

    public void clear() {
        userLoginView.clearUserName();
        userLoginView.clearPassword();
    }
}
