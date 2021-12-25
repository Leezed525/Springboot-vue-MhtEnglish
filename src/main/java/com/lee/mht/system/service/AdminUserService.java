package com.lee.mht.system.service;


import com.lee.mht.system.common.ResultObj;
import com.lee.mht.system.entity.AdminUser;

public interface AdminUserService {

    AdminUser getAdminUserInfoByUsername(String username);
}
