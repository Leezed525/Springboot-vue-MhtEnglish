package com.lee.mht.system.dao;

import com.lee.mht.system.entity.AdminUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


public interface AdminUserDao {
    AdminUser getAdminUserbyUsername(String username);

    Boolean login(@Param("username")String username,@Param("password") String password);

    String getpasswordByUsername(@Param("username") String username);
}
