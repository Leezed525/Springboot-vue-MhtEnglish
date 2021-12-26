package com.lee.mht.system.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lee.mht.system.common.Constant;
import com.lee.mht.system.common.ResultObj;
import com.lee.mht.system.entity.AdminUser;
import com.lee.mht.system.service.AdminUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author FucXing
 * @date 2021/12/22 20:02
 **/
@RestController
@RequestMapping("/admin/user")
@Slf4j
public class AdminUserController {

    @Autowired
    AdminUserService adminUserService;

    @RequestMapping("/getAdminUserInfoByUsername")
    public AdminUser getAdminUserInfoByUsername(@RequestParam("username") String username){
        log.info("username" + username);
        return adminUserService.getAdminUserInfoByUsername(username);
    }

    @RequestMapping("/getAllAdminUser")
    public ResultObj getAllAdminUser(@RequestParam(required=false,defaultValue="",name = "username") String username,
                                           @RequestParam(required=false,defaultValue="",name ="nickname") String nickname,
                                           @RequestParam(required=false,defaultValue="",name ="role_id")String role_id,
                                           @RequestParam(required=false,defaultValue="5",name ="limit")String pageSize,
                                           @RequestParam(required=false,defaultValue="1",name ="page")String pageNum){
        Integer roleId;
        if(StringUtils.hasLength(role_id)){
            roleId = Integer.parseInt(role_id);
        }
        else{
            roleId = null;
        }
        return adminUserService.getAllAdminUser(username, nickname, roleId,Integer.parseInt(pageSize), Integer.parseInt(pageNum));

    }

}
