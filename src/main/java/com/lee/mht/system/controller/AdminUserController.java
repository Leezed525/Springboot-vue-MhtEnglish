package com.lee.mht.system.controller;

import com.lee.mht.system.common.ResultObj;
import com.lee.mht.system.entity.AdminUser;
import com.lee.mht.system.service.AdminUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    //通过查询条件获取所有用户
    @RequestMapping("/getAllAdminUser")
    public ResultObj getAllAdminUser(@RequestParam(required=false,defaultValue="",name = "username") String username,
                                           @RequestParam(required=false,defaultValue="",name ="nickname") String nickname,
                                           @RequestParam(required=false,defaultValue="",name ="roleId")String roleId,
                                           @RequestParam(required=false,defaultValue="5",name ="limit")String pageSize,
                                           @RequestParam(required=false,defaultValue="1",name ="page")String pageNum){
        Integer role_id;
        if(StringUtils.hasLength(roleId)){
            role_id = Integer.parseInt(roleId);
        }
        else{
            role_id = null;
        }
        return adminUserService.getAllAdminUser(username, nickname, role_id,Integer.parseInt(pageSize), Integer.parseInt(pageNum));
    }

    //改
    @RequestMapping("/updateAdminUser")
    public ResultObj updateAdminUser(@RequestBody AdminUser user){
        return adminUserService.updateAdminUser(user);
    }

    //删
    @RequestMapping("/deleteAdminUserByIds")
    public ResultObj deleteAdminUserByIds(@RequestBody ArrayList<Integer> ids){
        return adminUserService.deleteAdminUserByIds(ids);
    }

    //增
    @RequestMapping("/addAdminUser")
    public ResultObj addAdminUser(@RequestBody AdminUser user){
        return adminUserService.addAdminUser(user);
    }

    //重置密码
    @RequestMapping("/restPassword")
    public ResultObj restPassword(Integer id){
        log.info(String.valueOf(id));
        return adminUserService.restPassword(id);
    }
}
