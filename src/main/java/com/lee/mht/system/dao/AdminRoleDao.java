package com.lee.mht.system.dao;

import com.lee.mht.system.entity.AdminRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author FucXing
 * @date 2021/12/28 17:42
 **/
public interface AdminRoleDao {

    List<AdminRole> getAllRolesByUserId(@Param("id")int userId);

}
