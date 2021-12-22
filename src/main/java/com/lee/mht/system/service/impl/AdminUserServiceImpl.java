package com.lee.mht.system.service.impl;

import com.lee.mht.system.common.ResultObj;
import com.lee.mht.system.dao.AdminUserDao;
import com.lee.mht.system.service.AdminUserService;
import org.apache.ibatis.annotations.Param;
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
    public Boolean login(@Param("username") String username,@Param("password") String password) {
        return adminUserDao.login(username,password);
    }
}
