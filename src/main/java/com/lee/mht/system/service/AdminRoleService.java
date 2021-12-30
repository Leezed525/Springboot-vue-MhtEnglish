package com.lee.mht.system.service;

import com.lee.mht.system.common.ResultObj;
import com.lee.mht.system.entity.AdminRole;

import java.util.ArrayList;

public interface AdminRoleService {
    ResultObj getAllRoles();

    ResultObj getAllRolesByUserId(Integer userId);

    ResultObj getAllAdminRole(String roleName, String comment, int pageSize, int pageNum);

    ResultObj updateAdminRole(AdminRole role);

    ResultObj checkRolenameUnique(String rolename);

    ResultObj addAdminRole(AdminRole role);

    ResultObj deleteAdminRoleByIds(ArrayList<Integer> ids);
}
