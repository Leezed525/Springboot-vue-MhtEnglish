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
import com.lee.mht.system.utils.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public ResultObj login(String username, String password) {

        AdminUser user = adminUserDao.login(username);
        int user_id = user.getId();
        String user_password = user.getPassword();
        //检查密码是否正确
        boolean passwordFlag = PasswordUtils.matches(user.getSalt(), password, user_password);
        if (user_id == 0 || !passwordFlag) {
            return new ResultObj(Constant.ERROR, Constant.USERNAME_PASSWORD_ERROR, null);
        }
        if (!user.getAvailable()) {
            return new ResultObj(Constant.ERROR, Constant.ADMINUSER_NOT_AVAILABLE, null);
        }
        //到这里就算登录通过，生成accessToken
        Map<String, Object> claims = new HashMap<>();

        //这里要加权限信息 加载claim中
        //留空
        //获取该用户所有角色
        List<AdminRole> roles = adminRoleDao.getAllRolesByUserId(user_id);
        //用Set来去重用户所有权限
        Set<String> permissions = new HashSet<>();

        for(AdminRole role : roles){
            //查询用户所拥有的每个角色所拥有的权限
            List<AdminPermission> permissionList = adminPermissionDao.getAllPermissionsByRoleId(role.getId());
            for (AdminPermission permission : permissionList){
                permissions.add(permission.getPercode());
            }
        }
        //将set转成string
        List<String> permissionsToClaims = new ArrayList<String>(permissions);

        claims.put(Constant.JWT_PERMISSIONS_KEY, permissionsToClaims);
        claims.put(Constant.JWT_USER_NAME, user.getUsername());

        //accessToken 中传入adminuser的id和claims
        String accessToken = JwtUtils.getAccessToken(String.valueOf(user_id), claims);

        //创建 refreshToken 存入redis
        //具体逻辑尚未实现
        String refreshToken = JwtUtils.getRefreshToken(String.valueOf(user_id),claims);

        //输出登录ip(为功能升级做准备)
        String ip = WebUtils.getRequest().getRemoteAddr();
        log.info("登录人" + username + "的IP为" + ip);
        return new ResultObj(Constant.OK, Constant.LOGIN_SUCCESS, accessToken);
    }
}
