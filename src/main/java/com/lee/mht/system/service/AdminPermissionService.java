package com.lee.mht.system.service;

import com.lee.mht.system.common.ResultObj;
import com.lee.mht.system.entity.AdminPermission;

import java.util.ArrayList;

public interface AdminPermissionService {
    ResultObj getAllAdminPermission(String title, String percode, Integer pId, int pageSize, int pageNum);

    ResultObj getAllMenu();

    ResultObj updateAdminPermission(AdminPermission permission);

    ResultObj addAdminPermission(AdminPermission permission);

    ResultObj checkPermissionnameUnique(String title);

    ResultObj deleteAdminPermissionByIds(ArrayList<Integer> ids);

    ResultObj getPermissionTree();

    ResultObj getPermissionByRoleId(Integer roleId);
}
