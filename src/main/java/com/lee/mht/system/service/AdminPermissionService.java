package com.lee.mht.system.service;

import com.github.pagehelper.PageInfo;
import com.lee.mht.system.common.ResultObj;
import com.lee.mht.system.entity.AdminPermission;
import com.lee.mht.system.utils.TreeNode;

import java.util.ArrayList;
import java.util.List;

public interface AdminPermissionService {
    PageInfo<AdminPermission> getAllAdminPermission(String title, String percode, Integer pId, int pageSize, int pageNum);

    List<AdminPermission> getAllMenu();

    boolean updateAdminPermission(AdminPermission permission);

    boolean addAdminPermission(AdminPermission permission);

    int checkPermissionnameUnique(String title);

    boolean deleteAdminPermissionByIds(ArrayList<Integer> ids);

    List<TreeNode> getPermissionTree();

    List<AdminPermission> getPermissionByRoleId(Integer roleId);
}
