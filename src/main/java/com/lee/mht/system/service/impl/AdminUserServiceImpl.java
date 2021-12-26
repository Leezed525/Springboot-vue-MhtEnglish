package com.lee.mht.system.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lee.mht.system.common.Constant;
import com.lee.mht.system.common.ResultObj;
import com.lee.mht.system.dao.AdminUserDao;
import com.lee.mht.system.entity.AdminUser;
import com.lee.mht.system.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author FucXing
 * @date 2021/12/22 22:53
 **/

@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired(required = false)
    private AdminUserDao adminUserDao;


    @Override
    public AdminUser getAdminUserInfoByUsername(String username) {
        return adminUserDao.getAdminUserByUsername(username);
    }

    @Override
    public ResultObj getAllAdminUser(String username, String nickname, Integer role_id, Integer pageSize, Integer pageNum) {
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<AdminUser> adminUsers = adminUserDao.getAllAdminUser(username, nickname, role_id);
            PageInfo<AdminUser> pageInfo = new PageInfo<>(adminUsers);
            return new ResultObj(Constant.OK,Constant.QUERY_SUCCESS,pageInfo);
        }catch(Exception e){
            e.printStackTrace();
            return new ResultObj(Constant.ERROR,Constant.QUERY_ERROR,null);
        }
    }

}
