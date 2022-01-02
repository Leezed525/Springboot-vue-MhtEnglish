package com.lee.mht.system.dao;

import com.lee.mht.system.entity.AdminPermission;
import com.lee.mht.system.utils.TreeNode;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

public interface AdminPermissionDao {
    List<AdminPermission> getAllPermissionsByRoleId(@Param("id")Integer id);

    List<AdminPermission> getAllAdminPermission(@Param("title") String title,@Param("percode") String percode,@Param("pid") Integer pId);

    List<AdminPermission> getAllMenu();

    boolean updateAdminPermission(@Param("permission") AdminPermission permission);

    boolean addAdminPermission(@Param("permission")AdminPermission permission);

    int checkPermissionnameUnique(@Param("title")String title);

    boolean deleteAdminPermissionByIds(@Param("ids") ArrayList<Integer> ids);

    List<TreeNode> getPids();
}
