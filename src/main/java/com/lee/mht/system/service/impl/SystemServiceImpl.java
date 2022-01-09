package com.lee.mht.system.service.impl;

import com.lee.mht.system.annotation.CostTime;
import com.lee.mht.system.common.Constant;
import com.lee.mht.system.common.ResultObj;
import com.lee.mht.system.dao.AdminRoleDao;
import com.lee.mht.system.dao.AdminUserDao;
import com.lee.mht.system.entity.AdminUser;
import com.lee.mht.system.service.AdminPermissionService;
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
    RedisUtils redisUtils;

    @Override
    @CostTime
    public ResultObj login(String username, String password, HttpServletRequest request) {

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


        //放入
        claims.put(Constant.JWT_USER_NAME, user.getUsername());
        claims.put(Constant.JWT_ISSUANCE_TIME,nowTimestamp);
        //accessToken 中传入adminuser的id和claims
        String accessToken = JwtUtils.getAccessToken(String.valueOf(user_id), claims);

        //将accessToken 存入redis
        redisUtils.set(Constant.REDIS_TOKEN_KEY + user_id,accessToken,Constant.TOKEN_EXPIRE_TIME);

        log.info("登录人" + username + "的IP为" + IpUtils.getClientIpAddress(request));

        return new ResultObj(Constant.OK, Constant.LOGIN_SUCCESS, accessToken);
    }

}
