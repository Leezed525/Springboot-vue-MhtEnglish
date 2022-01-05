package com.lee.mht.system.dao;

import com.lee.mht.system.config.MybatisRedisCache;
import com.lee.mht.system.entity.AdminPermission;
import com.lee.mht.system.utils.TreeNode;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

//@CacheNamespace(implementation = MybatisRedisCache.class)
public interface AdminPermissionDao {
    List<AdminPermission> getAllPermissionsByRoleId(@Param("id")Integer id);

    List<AdminPermission> getAllAdminPermission(@Param("title") String title,@Param("percode") String percode,@Param("pid") Integer pId);

    List<AdminPermission> getAllMenu();

    boolean updateAdminPermission(@Param("permission") AdminPermission permission);

    boolean addAdminPermission(@Param("permission")AdminPermission permission);

    int checkPermissionnameUnique(@Param("title")String title);

    boolean deleteAdminPermissionByIds(@Param("ids") ArrayList<Integer> ids);

    List<TreeNode> getPids();

    void deleteAllPermissionByRoleId(@Param("roleId") Integer roleId);

    void addPermissionByRoleId(@Param("pIds") ArrayList<Integer> pIds,@Param("roleId") Integer roleId);
}
