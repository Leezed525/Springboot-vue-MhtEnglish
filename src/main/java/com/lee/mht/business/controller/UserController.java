package com.lee.mht.business.controller;

import com.lee.mht.business.entity.User;
import com.lee.mht.business.service.UserService;
import com.lee.mht.system.common.Constant;
import com.lee.mht.system.common.ResultObj;
import com.lee.mht.system.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author FucXing
 * @date 2022/02/02 22:35
 **/
@RequestMapping("/user")
@RestController
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/getUserInfo")
    public ResultObj getUserInfo(HttpServletRequest request) {
        try {
            //获取token
            String token = request.getHeader(Constant.HEADER_TOKEN_KEY);
            int id = Integer.parseInt(JwtUtils.getId(token));
            User user = userService.getUserInfoById(id);
            return new ResultObj(Constant.OK, Constant.QUERY_SUCCESS, user);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResultObj(Constant.SERVER_ERROR_CODE, Constant.QUERY_ERROR);
        }
    }

    @RequestMapping("/updateUserInfo")
    public ResultObj updateUserInfo(@RequestBody User user, HttpServletRequest request) {
        try {
            user.setId(JwtUtils.getId(request));
            userService.updateUserInfo(user);
            return new ResultObj(Constant.OK, Constant.UPDATE_SUCCESS);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResultObj(Constant.SERVER_ERROR_CODE, Constant.UPDATE_ERROR);
        }
    }

}
