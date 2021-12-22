package com.lee.mht.system.dao;

import com.lee.mht.system.entity.AdminUser;
import org.springframework.stereotype.Repository;


public interface AdminUserDao {
    AdminUser getAdminUserbyUsername(String username);
}
