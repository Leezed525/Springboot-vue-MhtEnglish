package com.lee.mht.system.controller;

import com.github.pagehelper.PageInfo;
import com.lee.mht.system.common.Constant;
import com.lee.mht.system.common.ResultObj;
import com.lee.mht.system.entity.AdminUser;
import com.lee.mht.system.service.AdminUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

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
    @RequiresPermissions("adminUser:query")
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
        PageInfo<AdminUser> pageInfo =adminUserService.getAllAdminUser(username, nickname, role_id,Integer.parseInt(pageSize), Integer.parseInt(pageNum));
        if(pageInfo != null){
            return new ResultObj(Constant.OK, Constant.QUERY_SUCCESS, pageInfo);
        }else{
            return new ResultObj(Constant.SERVER_ERROR, Constant.QUERY_ERROR, null);
        }
    }

    //改
    @RequiresPermissions("adminUser:update")
    @RequestMapping("/updateAdminUser")
    public ResultObj updateAdminUser(@RequestBody AdminUser user){
        boolean flag =  adminUserService.updateAdminUser(user);
        if (flag) {
            return new ResultObj(Constant.OK, Constant.UPDATE_SUCCESS, null);
        } else {
            return new ResultObj(Constant.SERVER_ERROR, Constant.UPDATE_ERROR, null);
        }
    }

    //删
    @RequiresPermissions("adminUser:delete")
    @RequestMapping("/deleteAdminUserByIds")
    public ResultObj deleteAdminUserByIds(@RequestBody ArrayList<Integer> ids){
        boolean flag =  adminUserService.deleteAdminUserByIds(ids);
        if (flag) {
            return new ResultObj(Constant.OK, Constant.DELETE_SUCCESS, null);
        } else {
            return new ResultObj(Constant.SERVER_ERROR, Constant.DELETE_ERROR, null);
        }
    }

    //增
    @RequiresPermissions("adminUser:create")
    @RequestMapping("/addAdminUser")
    public ResultObj addAdminUser(@RequestBody AdminUser user){
        boolean flag =  adminUserService.addAdminUser(user);
        if (flag) {
            return new ResultObj(Constant.OK, Constant.ADD_SUCCESS, null);
        } else {
            return new ResultObj(Constant.SERVER_ERROR, Constant.ADD_ERROR, null);
        }
    }

    //重置密码
    @RequiresPermissions("adminUser:reset")
    @RequestMapping("/restPassword")
    public ResultObj restPassword(Integer id){
        boolean flag =  adminUserService.restPassword(id);
        if (flag) {
            return new ResultObj(Constant.OK, Constant.RESET_SUCCESS, null);
        } else {
            return new ResultObj(Constant.SERVER_ERROR, Constant.RESET_ERROR, null);
        }
    }

    //重新给用户分配角色
    @RequestMapping("/reassignRoles")
    @RequiresPermissions("adminUser:assign")
    public ResultObj reassignRoles(@RequestParam("rIds") ArrayList<Integer> rIds,@RequestParam("uId")Integer userId){
        boolean flag =  adminUserService.reassignRoles(rIds,userId);
        if (flag) {
            return new ResultObj(Constant.OK, Constant.UPDATE_SUCCESS, null);
        } else {
            return new ResultObj(Constant.SERVER_ERROR, Constant.UPDATE_ERROR, null);
        }
    }

    //检查用户名是否唯一
    @PostMapping("checkUsernameUnique")
    public ResultObj checkUsernameUnique(@RequestParam("username")String username){
        int count =  adminUserService.checkUsernameUnique(username);
        if(count == 0){
            return new ResultObj(Constant.OK, Constant.USERNAME_UNIQUE,null);
        }else{
            return new ResultObj(Constant.SERVER_ERROR, Constant.USERNAME_NOT_UNIQUE,null);

        }
    }

}
