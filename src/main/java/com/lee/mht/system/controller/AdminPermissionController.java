package com.lee.mht.system.controller;

import com.github.pagehelper.PageInfo;
import com.lee.mht.system.common.Constant;
import com.lee.mht.system.common.ResultObj;
import com.lee.mht.system.entity.AdminPermission;
import com.lee.mht.system.service.AdminPermissionService;
import com.lee.mht.system.utils.TreeNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
    public ResultObj getAllAdminPermission(@RequestParam(required = false, defaultValue = "", name = "title") String title,
                                           @RequestParam(required = false, defaultValue = "", name = "percode") String percode,
                                           @RequestParam(required = false, defaultValue = "", name = "pid") String pid,
                                           @RequestParam(required = false, defaultValue = "5", name = "limit") String pageSize,
                                           @RequestParam(required = false, defaultValue = "1", name = "page") String pageNum) {
        Integer pId;
        if (StringUtils.hasLength(pid)) {
            pId = Integer.parseInt(pid);
        } else {
            pId = null;
        }
        PageInfo<AdminPermission> pageInfo = adminPermissionService.getAllAdminPermission(title, percode, pId, Integer.parseInt(pageSize), Integer.parseInt(pageNum));
        if (pageInfo != null) {
            return new ResultObj(Constant.OK, Constant.QUERY_SUCCESS, pageInfo);
        } else {
            return new ResultObj(Constant.ERROR, Constant.QUERY_ERROR, null);
        }

    }

    //获取菜单列表
    @RequestMapping("/getAllMenu")
    public ResultObj getAllMenu() {
        List<AdminPermission> list = adminPermissionService.getAllMenu();
        if (list != null) {
            return new ResultObj(Constant.OK, Constant.QUERY_SUCCESS, list);
        } else {
            return new ResultObj(Constant.ERROR, Constant.QUERY_ERROR, null);
        }
    }

    //改
    @RequestMapping("/updateAdminPermission")
    public ResultObj updateAdminPermission(@RequestBody AdminPermission permission) {
        boolean flag = adminPermissionService.updateAdminPermission(permission);
        if (flag) {
            return new ResultObj(Constant.OK, Constant.UPDATE_SUCCESS, null);
        } else {
            return new ResultObj(Constant.ERROR, Constant.UPDATE_ERROR, null);
        }
    }

    //增
    @RequestMapping("/addAdminPermission")
    public ResultObj addAdminPermission(@RequestBody AdminPermission permission) {
        boolean flag = adminPermissionService.addAdminPermission(permission);
        if (flag) {
            return new ResultObj(Constant.OK, Constant.ADD_SUCCESS, null);
        } else {
            return new ResultObj(Constant.ERROR, Constant.ADD_ERROR, null);
        }
    }

    //检查权限是否唯一
    @PostMapping("checkTitleUnique")
    public ResultObj checkTitleUnique(@RequestParam("title") String title) {
        int count = adminPermissionService.checkPermissionnameUnique(title);
        if (count == 0) {
            return new ResultObj(Constant.OK, Constant.USERNAME_UNIQUE, null);
        } else {
            return new ResultObj(Constant.ERROR, Constant.USERNAME_NOT_UNIQUE, null);

        }
    }

    //删
    @RequestMapping("/deleteAdminPermissionByIds")
    public ResultObj deleteAdminPermissionByIds(@RequestBody ArrayList<Integer> ids) {
        boolean flag = adminPermissionService.deleteAdminPermissionByIds(ids);
        if (flag) {
            return new ResultObj(Constant.OK, Constant.DELETE_SUCCESS, null);
        } else {
            return new ResultObj(Constant.ERROR, Constant.DELETE_ERROR, null);
        }
    }


    //获取权限树
    @RequestMapping("getPermissionTree")
    public ResultObj getPermissionTree() {
        List<TreeNode> pids = adminPermissionService.getPermissionTree();
        if (pids != null) {
            return new ResultObj(Constant.OK, Constant.QUERY_SUCCESS, pids);
        } else {
            return new ResultObj(Constant.ERROR, Constant.QUERY_ERROR, null);

        }
    }

    //获取某个角色拥有的所有权限
    @RequestMapping("getPermissionByRoleId")
    public ResultObj getPermissionByRoleId(@RequestParam("RoleId") Integer RoleId) {
        List<AdminPermission> permissions = adminPermissionService.getPermissionByRoleId(RoleId);
        if (permissions != null) {
            return new ResultObj(Constant.OK, Constant.QUERY_SUCCESS, permissions);
        } else {
            return new ResultObj(Constant.ERROR, Constant.QUERY_ERROR, null);
        }
    }


}
