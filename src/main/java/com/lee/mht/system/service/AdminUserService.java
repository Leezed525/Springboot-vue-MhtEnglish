package com.lee.mht.system.service;


import com.lee.mht.system.common.ResultObj;

public interface AdminUserService {
    ResultObj login(String username, String password);
}
