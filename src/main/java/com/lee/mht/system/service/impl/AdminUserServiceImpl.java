package com.lee.mht.system.service.impl;

import com.lee.mht.system.common.Constants;
import com.lee.mht.system.common.ResultObj;
import com.lee.mht.system.dao.AdminUserDao;
import com.lee.mht.system.service.AdminUserService;
import com.lee.mht.system.utils.TokenUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
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


}
