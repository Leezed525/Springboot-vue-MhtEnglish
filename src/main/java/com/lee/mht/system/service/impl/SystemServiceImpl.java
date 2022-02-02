package com.lee.mht.system.service.impl;

import com.lee.mht.system.annotation.CostTime;
import com.lee.mht.system.common.Constant;
import com.lee.mht.system.common.ResultObj;
import com.lee.mht.system.dao.AdminRoleDao;
import com.lee.mht.system.dao.AdminUserDao;
import com.lee.mht.system.entity.AdminLog;
import com.lee.mht.system.entity.AdminUser;
import com.lee.mht.system.service.AdminPermissionService;
import com.lee.mht.system.service.RedisService;
import com.lee.mht.system.service.SystemService;
import com.lee.mht.system.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author FucXing
 * @date 2021/12/23 19:55
 **/
@Slf4j
@Service
public class SystemServiceImpl implements SystemService {

    @Autowired(required = false)
    AdminUserDao adminUserDao;

    @Autowired(required = false)
    AdminRoleDao adminRoleDao;

    @Autowired
    AdminPermissionService adminPermissionService;

    @Autowired
    private RedisService redisService;

    @Override
    @CostTime
    public ResultObj login(String username, String password, HttpServletRequest request) {

        Long beginTime = System.currentTimeMillis();
        AdminUser user = adminUserDao.login(username);
        int userId = user.getId();
        String user_password = user.getPassword();
        //检查密码是否正确
        boolean passwordFlag = PasswordUtils.matches(user.getSalt(), password, user_password);
        if (userId == 0 || !passwordFlag) {
            return new ResultObj(Constant.SERVER_ERROR, Constant.USERNAME_PASSWORD_ERROR, null);
        }
        if (!user.getAvailable()) {
            return new ResultObj(Constant.SERVER_ERROR, Constant.ADMINUSER_NOT_AVAILABLE, null);
        }
        //到这里就算登录通过，生成accessToken
        String accessToken = JwtUtils.generateMhtToken(String.valueOf(userId), user.getUsername(), Constant.JWT_USER_TYPE_ADMIN);
        //将accessToken 存入redis
        redisService.setAdminUserLoginToken(userId, accessToken,Constant.JWT_USER_TYPE_ADMIN);
        //记录登录日志
        saveLoginLog(userId,username, request, beginTime);
        return new ResultObj(Constant.OK, Constant.LOGIN_SUCCESS, accessToken);
    }

    @Async("asyncServiceExecutor")
    void saveLoginLog(int userId,String username, HttpServletRequest request, Long beginTime) {
        AdminLog adminLog = new AdminLog();
        adminLog.setType(Constant.LOG_TYPE_SYSTEM);
        adminLog.setOperator(username);
        adminLog.setOperatorId(userId);
        String ip = IpUtils.getClientIpAddress(request);
        if (ip == null) {
            ip = Constant.DEV_DEBUG_IP;
        }
        adminLog.setIp(ip);
        adminLog.setAction("系统用户登入");
        adminLog.setMethod("com.lee.mht.system.service.SystemService.login");
        adminLog.setResult(Constant.LOG_RESULT_SUCCESS);
        adminLog.setCreatetime(new Date());

        Long currentTime = System.currentTimeMillis();
        adminLog.setTimeConsuming(String.valueOf((currentTime - beginTime)));
        redisService.pushMhtLogList(adminLog);
    }

}
