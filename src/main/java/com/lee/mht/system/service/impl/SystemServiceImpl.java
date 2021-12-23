package com.lee.mht.system.service.impl;

import com.lee.mht.system.common.Constants;
import com.lee.mht.system.common.ResultObj;
import com.lee.mht.system.service.SystemService;
import com.lee.mht.system.utils.TokenUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

/**
 * @author FucXing
 * @date 2021/12/23 19:55
 **/
@Service
public class SystemServiceImpl implements SystemService {


    @Override
    public ResultObj login(@Param("username") String username, @Param("password") String password) {
        try {
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken uptoken = new UsernamePasswordToken(username,password);
            subject.login(uptoken);
            //查询权限，现在还没写，先留个空


            //获取token
            String token = TokenUtils.getToken(username, password);
            //返回登陆成功
            return new ResultObj(Constants.OK,"登陆成功",token);
        }catch (Exception e) {
            e.printStackTrace();
            return new ResultObj(Constants.ERROR,"登陆失败",null);
        }
    }
}
