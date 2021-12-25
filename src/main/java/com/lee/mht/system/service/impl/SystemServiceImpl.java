package com.lee.mht.system.service.impl;

import com.lee.mht.system.common.Constant;
import com.lee.mht.system.common.ResultObj;
import com.lee.mht.system.dao.AdminUserDao;
import com.lee.mht.system.entity.AdminUser;
import com.lee.mht.system.service.SystemService;
import com.lee.mht.system.shiro.LeeToken;
import com.lee.mht.system.utils.JwtUtils;
import com.lee.mht.system.utils.PasswordUtils;
import com.lee.mht.system.utils.TokenUtils;
import com.lee.mht.system.utils.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author FucXing
 * @date 2021/12/23 19:55
 **/
@Slf4j
@Service
public class SystemServiceImpl implements SystemService {

    @Autowired(required = false)
    AdminUserDao adminUserDao;

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
        claims.put(Constant.JWT_USER_NAME, user.getUsername());

        //accessToken 中传入adminuser的id和claims
        String accessToken = JwtUtils.getAccessToken(String.valueOf(user_id), claims);

        //创建 refreshToken 存入redis
        //具体逻辑尚未实现
        String refreshToken = JwtUtils.getRefreshToken(String.valueOf(user_id),claims);

        //输出登录ip(为功能升级做准备)
        String ip = WebUtils.getRequest().getRemoteAddr();
        log.info("登录人" + username + "的IP为" + ip);
        return new ResultObj(Constant.OK, "登陆成功", accessToken);
    }
}
