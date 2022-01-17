package com.lee.mht.system.controller;

import com.lee.mht.system.common.Constant;
import com.lee.mht.system.common.ResultObj;
import com.lee.mht.system.service.SystemService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
    public ResultObj login(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletRequest request) {
        return systemService.login(username, password, request);
    }

    @GetMapping("/toRole")
    @RequiresPermissions("adminRole:query")
    public ResultObj toRole() {
        return new ResultObj(Constant.OK, Constant.ALLOW_ACCESS);
    }

    @GetMapping("/toUser")
    @RequiresPermissions("adminUser:query")
    public ResultObj toUser() {
        return new ResultObj(Constant.OK, Constant.ALLOW_ACCESS);
    }

    @GetMapping("/toPermission")
    @RequiresPermissions("adminPermission:query")
    public ResultObj toPermission() {
        return new ResultObj(Constant.OK, Constant.ALLOW_ACCESS);
    }

    @GetMapping("/toLog")
    @RequiresPermissions("adminLog:query")
    public ResultObj toLog() {
        return new ResultObj(Constant.OK, Constant.ALLOW_ACCESS);
    }


    @GetMapping("/toWord")
    @RequiresPermissions("word:query")
    public ResultObj toWord() {
        return new ResultObj(Constant.OK, Constant.ALLOW_ACCESS);
    }


}
