package com.lee.mht.system.service;

import com.github.pagehelper.PageInfo;
import com.lee.mht.system.common.ResultObj;
import com.lee.mht.system.entity.AdminRole;

import java.util.ArrayList;
import java.util.List;

public interface AdminRoleService {
    List<AdminRole> getAllRoles();

    List<AdminRole> getAllRolesByUserId(Integer userId);

    PageInfo<AdminRole> getAllAdminRole(String roleName, String comment, int pageSize, int pageNum);

    boolean updateAdminRole(AdminRole role);

    int checkRolenameUnique(String rolename);

    boolean addAdminRole(AdminRole role);

    boolean deleteAdminRoleByIds(ArrayList<Integer> ids);

    boolean reassignPermissions(ArrayList<Integer> pIds, Integer roleId);
}
