package com.lee.mht.system.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lee.mht.system.common.Constant;
import com.lee.mht.system.common.ResultObj;
import com.lee.mht.system.dao.AdminUserDao;
import com.lee.mht.system.entity.AdminUser;
import com.lee.mht.system.service.AdminUserService;
import com.lee.mht.system.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
            return new ResultObj(Constant.OK, Constant.QUERY_SUCCESS, pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultObj(Constant.ERROR, Constant.QUERY_ERROR, null);
        }
    }

    @Override
    public ResultObj updateAdminUser(AdminUser user) {
        try {
            boolean flag = adminUserDao.updateAdminUser(user);
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
    @Transactional
    public ResultObj deleteAdminUserByIds(List<Integer> ids) {
        try {
            boolean flag = adminUserDao.deleteAdminUserByIds(ids);
            if (flag) {
                return new ResultObj(Constant.OK, Constant.DELETE_SUCCESS, null);
            } else {
                return new ResultObj(Constant.ERROR, Constant.DELETE_ERROR, null);
            }
        } catch (Exception e) {
            return new ResultObj(Constant.ERROR, Constant.DELETE_ERROR, null);
        }
    }

    @Override
    public ResultObj addAdminUser(AdminUser user) {
        String salt = PasswordUtils.getSalt();
        String password = PasswordUtils.encode(user.getPassword(), salt);
        user.setSalt(salt);
        user.setPassword(password);
        try {
            boolean flag = adminUserDao.addAdminUser(user);
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

    //重置密码
    @Override
    public ResultObj restPassword(Integer id) {
        String salt = PasswordUtils.getSalt();
        String password = PasswordUtils.encode("123456", salt);
        try {
            boolean flag = adminUserDao.restPassword(id, password, salt);
            if (flag) {
                return new ResultObj(Constant.OK, Constant.RESET_SUCCESS, null);
            } else {
                return new ResultObj(Constant.ERROR, Constant.RESET_ERROR, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultObj(Constant.ERROR, Constant.RESET_ERROR, null);
        }
    }


}
