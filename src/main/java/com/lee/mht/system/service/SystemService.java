package com.lee.mht.system.service;

import com.lee.mht.system.common.ResultObj;

import java.util.List;

public interface SystemService {
    ResultObj login(String username, String password);
}
