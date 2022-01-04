package com.lee.mht.system.controller;

import com.github.pagehelper.PageInfo;
import com.lee.mht.system.common.Constant;
import com.lee.mht.system.common.ResultObj;
import com.lee.mht.system.entity.AdminRole;
import com.lee.mht.system.entity.AdminUser;
import com.lee.mht.system.service.AdminRoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    //获取所有角色用以放入selection
    @RequestMapping(value = "/getAllRoles")
    public ResultObj getAllRoles() {
        List<AdminRole> adminRoles = adminRoleService.getAllRoles();
        if (adminRoles != null) {
            return new ResultObj(Constant.OK, Constant.QUERY_SUCCESS, adminRoles);
        } else {
            return new ResultObj(Constant.ERROR, Constant.QUERY_ERROR, null);
        }
    }

    //获取用户的角色
    @RequestMapping("/getAllRolesByUserId")
    public ResultObj getAllRolesByUserId(@RequestParam("userId") Integer userId) {
        List<AdminRole> adminRoles = adminRoleService.getAllRolesByUserId(userId);
        if (adminRoles != null){
            return new ResultObj(Constant.OK, Constant.QUERY_SUCCESS, adminRoles);
        }
        else{
            return new ResultObj(Constant.ERROR, Constant.QUERY_ERROR, null);
        }

    }

    //查（带条件）
    //通过查询条件获取所有用户
    @RequestMapping("/getAllAdminRole")
    public ResultObj getAllAdminRole(@RequestParam(required = false, defaultValue = "", name = "roleName") String roleName,
                                     @RequestParam(required = false, defaultValue = "", name = "comment") String comment,
                                     @RequestParam(required = false, defaultValue = "5", name = "limit") String pageSize,
                                     @RequestParam(required = false, defaultValue = "1", name = "page") String pageNum) {
        PageInfo<AdminRole> pageInfo =  adminRoleService.getAllAdminRole(roleName, comment, Integer.parseInt(pageSize), Integer.parseInt(pageNum));
        if (pageInfo != null) {
            return new ResultObj(Constant.OK, Constant.QUERY_SUCCESS, pageInfo);
        }else{
            return new ResultObj(Constant.ERROR, Constant.QUERY_ERROR, null);
        }
    }

    @RequestMapping("/updateAdminRole")
    public ResultObj updateAdminRole(@RequestBody AdminRole role) {
        boolean flag = adminRoleService.updateAdminRole(role);
        if(flag){
            return new ResultObj(Constant.OK, Constant.UPDATE_SUCCESS, null);
        }
        else{
            return new ResultObj(Constant.ERROR, Constant.UPDATE_ERROR, null);
        }
    }

    @PostMapping("checkRolenameUnique")
    public ResultObj checkRolenameUnique(@RequestParam("roleName") String rolename) {
        int count = adminRoleService.checkRolenameUnique(rolename);
        if(count == 0){
            return new ResultObj(Constant.OK, Constant.ROLENAME_UNIQUE,null);
        }else{
            return new ResultObj(Constant.ERROR, Constant.ROLENAME_NOT_UNIQUE,null);
        }
    }

    @RequestMapping("/addAdminRole")
    public ResultObj addAdminRole(@RequestBody AdminRole role) {
        boolean flag = adminRoleService.addAdminRole(role);
        if (flag) {
            return new ResultObj(Constant.OK, Constant.ADD_SUCCESS, null);
        } else {
            return new ResultObj(Constant.ERROR, Constant.ADD_ERROR, null);
        }
    }

    @RequestMapping("/deleteAdminRoleByIds")
    public ResultObj deleteAdminRoleByIds(@RequestBody ArrayList<Integer> ids) {
        boolean flag =  adminRoleService.deleteAdminRoleByIds(ids);
        if (flag) {
            return new ResultObj(Constant.OK, Constant.DELETE_SUCCESS, null);
        } else {
            return new ResultObj(Constant.ERROR, Constant.DELETE_ERROR, null);
        }
    }

    //给角色分配权限
    @RequestMapping("/reassignPermission")
    public ResultObj reassignRoles(@RequestParam("pIds") ArrayList<Integer> pIds, @RequestParam("rId") Integer roleId) {
        boolean flag =  adminRoleService.reassignPermissions(pIds, roleId);
        if (flag) {
            return new ResultObj(Constant.OK, Constant.UPDATE_SUCCESS, null);
        } else {
            return new ResultObj(Constant.ERROR, Constant.UPDATE_ERROR, null);
        }
    }
}
