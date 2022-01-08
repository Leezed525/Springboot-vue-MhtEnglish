package com.lee.mht.system.service;


import com.github.pagehelper.PageInfo;
import com.lee.mht.system.common.ResultObj;
import com.lee.mht.system.entity.AdminUser;

import java.util.List;

public interface AdminUserService {

    //查询单个系统系统用户信息
    AdminUser getAdminUserInfoByUsername(String username);

    //查询系统用户列表信息
    PageInfo<AdminUser> getAllAdminUser(String username, String nickname, Integer role_id, Integer pageSize, Integer pageNum);

    //更新系统用户
    boolean updateAdminUser(AdminUser user);

    //删除系统用户
    boolean deleteAdminUserByIds(List<Integer> ids);

    //增加系统用户
    boolean addAdminUser(AdminUser user);

    //重置密码
    boolean restPassword(Integer id);

    //给系统用户重新分配角色
    boolean reassignRoles(List<Integer> rIds, Integer userId);

    //检查系统用户名重名
    int checkUsernameUnique(String username);
}
