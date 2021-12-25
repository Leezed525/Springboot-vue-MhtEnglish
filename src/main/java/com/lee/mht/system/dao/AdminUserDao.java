package com.lee.mht.system.dao;

import com.lee.mht.system.entity.AdminUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


public interface AdminUserDao {
    AdminUser getAdminUserByUsername(@Param("username")String username);

    AdminUser login(@Param("username")String username);

    String getpasswordByUsername(@Param("username") String username);
}
