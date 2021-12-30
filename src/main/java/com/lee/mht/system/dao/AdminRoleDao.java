package com.lee.mht.system.dao;

import com.lee.mht.system.entity.AdminRole;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

/**
 * @author FucXing
 * @date 2021/12/28 17:42
 **/
public interface AdminRoleDao {

    List<AdminRole> getAllRolesByUserId(@Param("id")int userId);

    List<AdminRole> getAllRoles();

    void deleteAllRolesByUserId(@Param("userId") Integer userId);

    void addRolesByUserId(@Param("rIds") List<Integer> rIds,@Param("u_id") Integer userId);

    List<AdminRole> getAllAdminRole(@Param("rolename")String roleName,@Param("comment") String comment);

    boolean updateAdminRole(@Param("adminRole") AdminRole role);

    int checkRolenameUnique(@Param("rolename")String rolename);

    boolean addAdminRole(@Param("adminRole") AdminRole role);

    boolean deleteAdminRoleByIds(@Param("ids") ArrayList<Integer> ids);
}
