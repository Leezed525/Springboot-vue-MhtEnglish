package com.lee.mht.system.service.impl;

import com.lee.mht.system.dao.AdminUserDao;
import com.lee.mht.system.entity.AdminUser;
import com.lee.mht.system.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author FucXing
 * @date 2021/12/22 22:53
 **/

@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired(required = false)
    private AdminUserDao adminUserDao;


    @Override
    public AdminUser getAdminUserInfoByUsername(String username) {
        return adminUserDao.getAdminUserByUsername(username);
    }
}
