package com.lee.mht.system.dao;

import com.lee.mht.system.entity.AdminPermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdminPermissionDao {
    List<AdminPermission> getAllPermissionsByRoleId(@Param("id")Integer id);
}
