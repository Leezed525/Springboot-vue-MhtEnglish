package com.lee.mht.business.service;

public interface SigninService {
    boolean isSigninToday(int userId);

    void signinToday(int userId);

    int getSigninDays(int userId);

}
