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
        int user_id = user.getId();
        String user_password = user.getPassword();
        //检查密码是否正确
        boolean passwordFlag = PasswordUtils.matches(user.getSalt(), password, user_password);
        if (user_id == 0 || !passwordFlag) {
            return new ResultObj(Constant.SERVER_ERROR, Constant.USERNAME_PASSWORD_ERROR, null);
        }
        if (!user.getAvailable()) {
            return new ResultObj(Constant.SERVER_ERROR, Constant.ADMINUSER_NOT_AVAILABLE, null);
        }
        //获取当前时间
        long nowTimestamp = new Timestamp(System.currentTimeMillis()).getTime();
        //到这里就算登录通过，生成accessToken
        Map<String, Object> claims = new HashMap<>();
        //放入信息
        claims.put(Constant.JWT_USER_NAME, user.getUsername());
        //签发时间
        claims.put(Constant.JWT_ISSUANCE_TIME,nowTimestamp);
        //accessToken 中传入adminuser的id和claims
        String accessToken = JwtUtils.getAccessToken(String.valueOf(user_id), claims);
        //将accessToken 存入redis
        redisService.setAdminUserLoginToken(user_id,accessToken);
        //记录登录日志
        saveLoginLog(username,request,beginTime);
        return new ResultObj(Constant.OK, Constant.LOGIN_SUCCESS, accessToken);
    }

    private void saveLoginLog(String username, HttpServletRequest request, Long beginTime) {
        AdminLog adminLog = new AdminLog();
        adminLog.setType(Constant.LOG_TYPE_SYSTEM);
        adminLog.setOperator(username);
        String ip = IpUtils.getClientIpAddress(request);
        if(ip == null){
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
