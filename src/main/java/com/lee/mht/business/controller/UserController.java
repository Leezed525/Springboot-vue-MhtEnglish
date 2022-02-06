package com.lee.mht.business.controller;

import com.lee.mht.business.entity.User;
import com.lee.mht.business.service.BusinessService;
import com.lee.mht.business.service.UserService;
import com.lee.mht.system.common.Constant;
import com.lee.mht.system.common.ResultObj;
import com.lee.mht.system.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author FucXing
 * @date 2022/02/02 22:35
 **/
@RequestMapping("/user")
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/getUserInfo")
    public ResultObj getUserInfo(HttpServletRequest request){

        try{
            //获取token
            String token = request.getHeader(Constant.HEADER_TOKEN_KEY);
            int id = Integer.parseInt(JwtUtils.getId(token));
            User user = userService.getUserInfoById(id);
            return new ResultObj(Constant.OK,Constant.QUERY_SUCCESS,user);
        }catch (Exception e) {
            return new ResultObj(Constant.SERVER_ERROR,Constant.QUERY_ERROR);
        }
    }
}
