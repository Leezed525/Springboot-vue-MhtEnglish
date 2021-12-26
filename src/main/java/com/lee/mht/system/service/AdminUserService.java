package com.lee.mht.system.service;


import com.lee.mht.system.common.ResultObj;
import com.lee.mht.system.entity.AdminUser;

import java.util.List;

public interface AdminUserService {

    AdminUser getAdminUserInfoByUsername(String username);

    ResultObj  getAllAdminUser(String username, String nickname, Integer role_id,Integer pageSize,Integer pageNum);
}
