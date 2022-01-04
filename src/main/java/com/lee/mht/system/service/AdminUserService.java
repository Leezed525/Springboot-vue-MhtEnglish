package com.lee.mht.system.service;


import com.github.pagehelper.PageInfo;
import com.lee.mht.system.common.ResultObj;
import com.lee.mht.system.entity.AdminUser;

import java.util.List;

public interface AdminUserService {

    AdminUser getAdminUserInfoByUsername(String username);

    PageInfo<AdminUser> getAllAdminUser(String username, String nickname, Integer role_id, Integer pageSize, Integer pageNum);

    boolean updateAdminUser(AdminUser user);

    boolean deleteAdminUserByIds(List<Integer> ids);

    boolean addAdminUser(AdminUser user);

    boolean restPassword(Integer id);

    boolean reassignRoles(List<Integer> rIds, Integer userId);

    int checkUsernameUnique(String username);
}
