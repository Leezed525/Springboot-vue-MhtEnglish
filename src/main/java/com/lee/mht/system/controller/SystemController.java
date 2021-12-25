package com.lee.mht.system.controller;

import com.lee.mht.system.common.ResultObj;
import com.lee.mht.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author FucXing
 * @date 2021/12/23 19:54
 **/
@RequestMapping("/admin/system")
@RestController
@CrossOrigin
public class SystemController {

    @Autowired
    SystemService systemService;

    @PostMapping("/login")
    public ResultObj login(@RequestParam("username")String username, @RequestParam("password")String password){
        return systemService.login(username,password);
    }
}
