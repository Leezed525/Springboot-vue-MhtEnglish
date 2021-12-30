package com.lee.mht.system.service;


import com.lee.mht.system.common.ResultObj;
import com.lee.mht.system.entity.AdminUser;

import java.util.List;

public interface AdminUserService {

    AdminUser getAdminUserInfoByUsername(String username);

    ResultObj  getAllAdminUser(String username, String nickname, Integer role_id,Integer pageSize,Integer pageNum);

    ResultObj updateAdminUser(AdminUser user);

    ResultObj deleteAdminUserByIds(List<Integer> ids);

    ResultObj addAdminUser(AdminUser user);

    ResultObj restPassword(Integer id);

    ResultObj reassignRoles(List<Integer> rIds, Integer userId);

    ResultObj checkUsernameUnique(String username);
}
