package com.lee.mht.system.dao;

import com.lee.mht.system.entity.AdminUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface AdminUserDao {
    AdminUser getAdminUserByUsername(@Param("username")String username);

    AdminUser login(@Param("username")String username);

    String getpasswordByUsername(@Param("username") String username);

    List<AdminUser> getAllAdminUser(@Param("username")String username,
                                    @Param("nickname")String nickname,
                                    @Param("role_id") Integer role_id);
}
