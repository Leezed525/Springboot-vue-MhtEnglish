package com.lee.mht.business.service;

import com.lee.mht.business.entity.Signin;

import java.util.List;

public interface SigninService {
    boolean isSigninToday(int userId);

    void signinToday(int userId);

    int getSigninDays(int userId);

    List<Signin> getSigninList(int userId);
}
