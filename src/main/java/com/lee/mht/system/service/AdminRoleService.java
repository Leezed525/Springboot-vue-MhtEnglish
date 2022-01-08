package com.lee.mht.system.service;

import com.github.pagehelper.PageInfo;
import com.lee.mht.system.common.ResultObj;
import com.lee.mht.system.entity.AdminRole;

import java.util.ArrayList;
import java.util.List;

public interface AdminRoleService {
    //获取所有权限
    List<AdminRole> getAllRoles();

    //根据用户id获取该用户的所有角色
    List<AdminRole> getAllRolesByUserId(Integer userId);

    //查询角色
    PageInfo<AdminRole> getAllAdminRole(String roleName, String comment, int pageSize, int pageNum);

    //更新角色
    boolean updateAdminRole(AdminRole role);

    //检查角色名是否重名
    int checkRolenameUnique(String rolename);

    //增加角色
    boolean addAdminRole(AdminRole role);

    //删除角色
    boolean deleteAdminRoleByIds(ArrayList<Integer> ids);

    //给角色重新分配权限
    boolean reassignPermissions(ArrayList<Integer> pIds, Integer roleId);
}
