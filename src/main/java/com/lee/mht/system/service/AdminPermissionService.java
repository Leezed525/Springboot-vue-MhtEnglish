package com.lee.mht.system.service;

import com.github.pagehelper.PageInfo;
import com.lee.mht.system.common.ResultObj;
import com.lee.mht.system.entity.AdminPermission;
import com.lee.mht.system.utils.TreeNode;

import java.util.ArrayList;
import java.util.List;

public interface AdminPermissionService {
    //查询权限
    PageInfo<AdminPermission> getAllAdminPermission(String title, String percode, Integer pId, int pageSize, int pageNum);

    //获取菜单列表
    List<AdminPermission> getAllMenu();

    //更新权限
    boolean updateAdminPermission(AdminPermission permission);

    //增加权限
    boolean addAdminPermission(AdminPermission permission);

    //检查权限名是否重名
    int checkPermissionnameUnique(String title);

    //删除权限
    boolean deleteAdminPermissionByIds(ArrayList<Integer> ids);

    //获取权限树
    List<TreeNode> getPermissionTree();

    //根据角色id返回该用户所拥有的所有权限
    List<AdminPermission> getPermissionByRoleId(Integer roleId);

    //根据用户id返回这个用户的所有权限
    List<String> getAllPermissionByUserId(int user_id);

}
