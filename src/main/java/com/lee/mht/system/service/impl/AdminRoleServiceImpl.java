package com.lee.mht.system.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lee.mht.system.common.Constant;
import com.lee.mht.system.common.ResultObj;
import com.lee.mht.system.dao.AdminRoleDao;
import com.lee.mht.system.entity.AdminRole;
import com.lee.mht.system.service.AdminRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author FucXing
 * @date 2021/12/28 17:39
 **/

@Service
public class AdminRoleServiceImpl implements AdminRoleService {

    @Autowired(required = false)
    AdminRoleDao adminRoleDao;

    @Override
    public ResultObj getAllRoles() {
        try {
            List<AdminRole> adminRoles = adminRoleDao.getAllRoles();
            return new ResultObj(Constant.OK, Constant.QUERY_SUCCESS, adminRoles);
        } catch (Exception e) {
            return new ResultObj(Constant.ERROR, Constant.QUERY_ERROR, null);
        }
    }

    @Override
    public ResultObj getAllRolesByUserId(Integer userId) {
        try {
            List<AdminRole> adminRoles = adminRoleDao.getAllRolesByUserId(userId);
            return new ResultObj(Constant.OK, Constant.QUERY_SUCCESS, adminRoles);
        } catch (Exception e) {
            return new ResultObj(Constant.ERROR, Constant.QUERY_ERROR, null);
        }
    }

    @Override
    public ResultObj getAllAdminRole(String roleName, String comment, int pageSize, int pageNum) {
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<AdminRole> adminRoles = adminRoleDao.getAllAdminRole(roleName, comment);
            PageInfo<AdminRole> pageInfo = new PageInfo<>(adminRoles);
            return new ResultObj(Constant.OK, Constant.QUERY_SUCCESS, pageInfo);
        } catch (Exception e) {
            return new ResultObj(Constant.ERROR, Constant.QUERY_ERROR, null);
        }
    }

    @Override
    public ResultObj updateAdminRole(AdminRole role) {
        try {
            boolean flag = adminRoleDao.updateAdminRole(role);
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
    public ResultObj checkRolenameUnique(String rolename) {
        int count = adminRoleDao.checkRolenameUnique(rolename);
        if(count == 0){
            return new ResultObj(Constant.OK, Constant.ROLENAME_UNIQUE,null);
        }else{
            return new ResultObj(Constant.ERROR, Constant.ROLENAME_NOT_UNIQUE,null);

        }
    }

    @Override
    public ResultObj addAdminRole(AdminRole role) {
        try {
            boolean flag = adminRoleDao.addAdminRole(role);
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
    public ResultObj deleteAdminRoleByIds(ArrayList<Integer> ids) {
        try {
            boolean flag = adminRoleDao.deleteAdminRoleByIds(ids);
            if (flag) {
                return new ResultObj(Constant.OK, Constant.DELETE_SUCCESS, null);
            } else {
                return new ResultObj(Constant.ERROR, Constant.DELETE_ERROR, null);
            }
        } catch (Exception e) {
            return new ResultObj(Constant.ERROR, Constant.DELETE_ERROR, null);
        }
    }
}
