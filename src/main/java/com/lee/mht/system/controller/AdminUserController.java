package com.lee.mht.system.controller;

import com.lee.mht.system.common.Constants;
import com.lee.mht.system.common.ResultObj;
import com.lee.mht.system.entity.AdminUser;
import com.lee.mht.system.service.AdminUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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


    @PostMapping("/login")
    public ResultObj login(@RequestParam("username")String username,@RequestParam("password")String password){
        return adminUserService.login(username,password);
    }

}
