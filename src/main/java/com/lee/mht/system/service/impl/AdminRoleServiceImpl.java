package com.lee.mht.system.service.impl;

import com.lee.mht.system.common.Constant;
import com.lee.mht.system.common.ResultObj;
import com.lee.mht.system.dao.AdminRoleDao;
import com.lee.mht.system.entity.AdminRole;
import com.lee.mht.system.service.AdminRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            return new ResultObj(Constant.OK,Constant.QUERY_SUCCESS,adminRoles);
        }catch (Exception e){
            return new ResultObj(Constant.ERROR,Constant.QUERY_ERROR,null);
        }
    }

    @Override
    public ResultObj getAllRolesByUserId(Integer userId) {
        try {
            List<AdminRole> adminRoles = adminRoleDao.getAllRolesByUserId(userId);
            return new ResultObj(Constant.OK,Constant.QUERY_SUCCESS,adminRoles);
        }catch (Exception e){
            return new ResultObj(Constant.ERROR,Constant.QUERY_ERROR,null);
        }
    }
}
