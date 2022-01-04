package com.lee.mht.system.service.impl;

import com.lee.mht.system.common.Constant;
import com.lee.mht.system.common.ResultObj;
import com.lee.mht.system.dao.AdminPermissionDao;
import com.lee.mht.system.dao.AdminRoleDao;
import com.lee.mht.system.dao.AdminUserDao;
import com.lee.mht.system.entity.AdminPermission;
import com.lee.mht.system.entity.AdminRole;
import com.lee.mht.system.entity.AdminUser;
import com.lee.mht.system.service.SystemService;
import com.lee.mht.system.utils.JwtUtils;
import com.lee.mht.system.utils.PasswordUtils;
import com.lee.mht.system.utils.RedisUtils;
import com.lee.mht.system.utils.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired(required = false)
    AdminPermissionDao adminPermissionDao;

    @Autowired
    RedisUtils redisUtils;

    @Override
    public ResultObj login(String username, String password) {

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

        //查询用户的所有权限
        List<String> permissionsToClaims = getAllPermissionByUserId(user_id);
        //放入
        claims.put(Constant.JWT_PERMISSIONS_KEY, permissionsToClaims);
        claims.put(Constant.JWT_USER_NAME, user.getUsername());
        claims.put(Constant.JWT_ISSUANCE_TIME,nowTimestamp);
        //accessToken 中传入adminuser的id和claims
        String accessToken = JwtUtils.getAccessToken(String.valueOf(user_id), claims);

        //将accessToken 存入redis
        redisUtils.set(Constant.REDIS_TOKEN_KEY + user_id,accessToken,Constant.TOKEN_EXPIRE_TIME);

        //输出登录ip(为功能升级做准备)
        String ip = WebUtils.getRequest().getRemoteAddr();
        log.info("登录人" + username + "的IP为" + ip);
        return new ResultObj(Constant.OK, Constant.LOGIN_SUCCESS, accessToken);
    }

    private List<String> getAllPermissionByUserId(int user_id){
        //获取该用户所有角色
        List<AdminRole> roles = adminRoleDao.getAllRolesByUserId(user_id);
        //用Set来去重用户所有权限
        Set<String> permissions = new HashSet<>();
        //查询用户所拥有的每个角色所拥有的权限
        for(AdminRole role : roles){
            List<AdminPermission> permissionList = adminPermissionDao.getAllPermissionsByRoleId(role.getId());
            for (AdminPermission permission : permissionList){
                permissions.add(permission.getPercode());
            }
        }
        //将set转成List<string>
        return new ArrayList<String>(permissions);
    }
}
