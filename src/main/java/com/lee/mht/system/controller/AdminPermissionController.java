package com.lee.mht.system.controller;

import com.lee.mht.system.common.ResultObj;
import com.lee.mht.system.entity.AdminPermission;
import com.lee.mht.system.entity.AdminPermission;
import com.lee.mht.system.entity.AdminPermission;
import com.lee.mht.system.service.AdminPermissionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * @author FucXing
 * @date 2022/01/01 00:52
 **/

@Slf4j
@RestController
@RequestMapping("/admin/permission")
public class AdminPermissionController {

    @Autowired
    private AdminPermissionService adminPermissionService;

    //查询权限列表
    @RequestMapping("/getAllAdminPermission")
    public ResultObj getAllAdminPermission(@RequestParam(required=false,defaultValue="",name = "title") String title,
                                     @RequestParam(required=false,defaultValue="",name ="percode") String percode,
                                     @RequestParam(required=false,defaultValue="",name ="pid")String pid,
                                     @RequestParam(required=false,defaultValue="5",name ="limit")String pageSize,
                                     @RequestParam(required=false,defaultValue="1",name ="page")String pageNum){
        Integer pId;
        if(StringUtils.hasLength(pid)){
            pId = Integer.parseInt(pid);
        }
        else{
            pId = null;
        }
        return adminPermissionService.getAllAdminPermission(title, percode, pId,Integer.parseInt(pageSize), Integer.parseInt(pageNum));
    }

    //获取菜单列表
    @RequestMapping("/getAllMenu")
    public ResultObj getAllMenu(){
        return adminPermissionService.getAllMenu();
    }

    //改
    @RequestMapping("/updateAdminPermission")
    public ResultObj updateAdminPermission(@RequestBody AdminPermission permission){
        return adminPermissionService.updateAdminPermission(permission);
    }

    //增
    @RequestMapping("/addAdminPermission")
    public ResultObj addAdminPermission(@RequestBody AdminPermission permission){
        return adminPermissionService.addAdminPermission(permission);
    }

    //检查权限是否唯一
    @PostMapping("checkTitleUnique")
    public ResultObj checkTitleUnique(@RequestParam("title")String title){
        return adminPermissionService.checkPermissionnameUnique(title);
    }

    //删
    @RequestMapping("/deleteAdminPermissionByIds")
    public ResultObj deleteAdminPermissionByIds(@RequestBody ArrayList<Integer> ids){
        log.info(ids.toString());
        return adminPermissionService.deleteAdminPermissionByIds(ids);
    }


    //获取权限树
    @RequestMapping("getPermissionTree")
    public ResultObj getPermissionTree(){
        return adminPermissionService.getPermissionTree();
    }

    //获取某个角色拥有的所有权限
    @RequestMapping("getPermissionByRoleId")
    public ResultObj getPermissionByRoleId(@RequestParam("RoleId")Integer RoleId){
        return adminPermissionService.getPermissionByRoleId(RoleId);
    }


}
