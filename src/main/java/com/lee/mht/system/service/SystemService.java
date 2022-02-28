package com.lee.mht.system.service;

import com.lee.mht.system.common.ResultObj;

import javax.servlet.http.HttpServletRequest;

public interface SystemService {
    ResultObj login(String username, String password, HttpServletRequest request);

    int getWordCount();
}
