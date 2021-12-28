package com.lee.mht.system.controller;

import com.lee.mht.system.common.ResultObj;
import com.lee.mht.system.entity.AdminRole;
import com.lee.mht.system.service.AdminRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author FucXing
 * @date 2021/12/28 17:30
 **/

@RestController
@RequestMapping("/admin/role")
public class AdminRoleController {

    @Autowired
    AdminRoleService adminRoleService;

    //@RequestMapping(value = "/getAllRoles")
    //public ResultObj getAllRoles(){
    //    return adminRoleService.getAllRoles();
    //}

}
