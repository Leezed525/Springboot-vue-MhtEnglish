package com.lee.mht.system.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lee.mht.system.common.Constant;
import com.lee.mht.system.common.ResultObj;
import com.lee.mht.system.dao.AdminPermissionDao;
import com.lee.mht.system.entity.AdminPermission;
import com.lee.mht.system.service.AdminPermissionService;
import com.lee.mht.system.utils.TreeNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author FucXing
 * @date 2022/01/01 00:53
 **/

@Slf4j
@Service
public class AdminPermissionServiceImpl implements AdminPermissionService {

    @Autowired(required = false)
    private AdminPermissionDao adminPermissionDao;

    @Override
    public ResultObj getAllAdminPermission(String title, String percode, Integer pId, int pageSize, int pageNum) {
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<AdminPermission> adminPermissions = adminPermissionDao.getAllAdminPermission(title, percode, pId);
            PageInfo<AdminPermission> pageInfo = new PageInfo<>(adminPermissions);
            return new ResultObj(Constant.OK, Constant.QUERY_SUCCESS, pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultObj(Constant.ERROR, Constant.QUERY_ERROR, null);
        }
    }

    @Override
    public ResultObj getAllMenu() {
        try {
            List<AdminPermission> adminMenus = adminPermissionDao.getAllMenu();
            return new ResultObj(Constant.OK, Constant.QUERY_SUCCESS, adminMenus);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultObj(Constant.ERROR, Constant.QUERY_ERROR, null);
        }
    }

    @Override
    public ResultObj updateAdminPermission(AdminPermission permission) {
        try {
            boolean flag = adminPermissionDao.updateAdminPermission(permission);
            if (flag) {
                return new ResultObj(Constant.OK, Constant.UPDATE_SUCCESS, null);
            } else {
                return new ResultObj(Constant.ERROR, Constant.UPDATE_ERROR, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultObj(Constant.ERROR, Constant.UPDATE_ERROR, null);
        }
    }

    @Override
    public ResultObj addAdminPermission(AdminPermission permission) {
        try {
            boolean flag = adminPermissionDao.addAdminPermission(permission);
            if (flag) {
                return new ResultObj(Constant.OK, Constant.ADD_SUCCESS, null);
            } else {
                return new ResultObj(Constant.ERROR, Constant.ADD_ERROR, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultObj(Constant.ERROR, Constant.ADD_ERROR, null);
        }
    }

    @Override
    public ResultObj checkPermissionnameUnique(String title) {
        int count = adminPermissionDao.checkPermissionnameUnique(title);
        if (count == 0) {
            return new ResultObj(Constant.OK, Constant.USERNAME_UNIQUE, null);
        } else {
            return new ResultObj(Constant.ERROR, Constant.USERNAME_NOT_UNIQUE, null);

        }
    }

    @Override
    public ResultObj deleteAdminPermissionByIds(ArrayList<Integer> ids) {
        try {
            boolean flag = adminPermissionDao.deleteAdminPermissionByIds(ids);
            log.info(String.valueOf(flag));
            if (flag) {
                return new ResultObj(Constant.OK, Constant.DELETE_SUCCESS, null);
            } else {
                return new ResultObj(Constant.ERROR, Constant.DELETE_ERROR, null);
            }
        } catch (Exception e) {
            return new ResultObj(Constant.ERROR, Constant.DELETE_ERROR, null);
        }
    }


    //获取权限树
    @Override
    public ResultObj getPermissionTree() {
        try {
            List<TreeNode> pids = adminPermissionDao.getPids();
            List<AdminPermission> permissions = adminPermissionDao.getAllAdminPermission(null, null, null);
            for (TreeNode node : pids) {
                List<TreeNode> childrens = new ArrayList<>();
                for (AdminPermission children : permissions) {
                    if (Objects.equals(children.getPid(), node.getId())) {
                        childrens.add(new TreeNode(children.getId(), children.getTitle()));
                    }
                }
                node.setChildren(childrens);
            }
            return new ResultObj(Constant.OK, Constant.QUERY_SUCCESS, pids);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultObj(Constant.ERROR, Constant.QUERY_ERROR, null);
        }
    }

    @Override
    public ResultObj getPermissionByRoleId(Integer roleId) {
        try {
            List<AdminPermission> permissions = adminPermissionDao.getAllPermissionsByRoleId(roleId);
            return new ResultObj(Constant.OK, Constant.QUERY_SUCCESS, permissions);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultObj(Constant.ERROR, Constant.QUERY_ERROR, null);
        }
    }

}
