package com.lee.mht.system.controller;

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
    public ResultObj getAllRoles(){
        return adminRoleService.getAllRoles();
    }

    //获取用户的角色
    @RequestMapping("/getAllRolesByUserId")
    public ResultObj getAllRolesByUserId(@RequestParam("userId")Integer userId){
        return adminRoleService.getAllRolesByUserId(userId);
    }

    //查（带条件）
    //通过查询条件获取所有用户
    @RequestMapping("/getAllAdminRole")
    public ResultObj getAllAdminRole(@RequestParam(required=false,defaultValue="",name = "roleName") String roleName,
                                     @RequestParam(required=false,defaultValue="",name ="comment") String comment,
                                     @RequestParam(required=false,defaultValue="5",name ="limit")String pageSize,
                                     @RequestParam(required=false,defaultValue="1",name ="page")String pageNum){
        return adminRoleService.getAllAdminRole(roleName, comment,Integer.parseInt(pageSize), Integer.parseInt(pageNum));
    }

    @RequestMapping("/updateAdminRole")
    public ResultObj updateAdminRole(@RequestBody AdminRole role){
        return adminRoleService.updateAdminRole(role);
    }

    @PostMapping("checkRolenameUnique")
    public ResultObj checkRolenameUnique(@RequestParam("roleName")String rolename){
        return adminRoleService.checkRolenameUnique(rolename);
    }

    @RequestMapping("/addAdminRole")
    public ResultObj addAdminRole(@RequestBody AdminRole role){
        return adminRoleService.addAdminRole(role);
    }

    @RequestMapping("/deleteAdminRoleByIds")
    public ResultObj deleteAdminRoleByIds(@RequestBody ArrayList<Integer> ids){
        return adminRoleService.deleteAdminRoleByIds(ids);
    }

    //给角色分配权限
    @RequestMapping("/reassignPermission")
    public ResultObj reassignRoles(@RequestParam("pIds") ArrayList<Integer> pIds,@RequestParam("rId")Integer roleId){
        return adminRoleService.reassignPermissions(pIds, roleId);
    }
}
