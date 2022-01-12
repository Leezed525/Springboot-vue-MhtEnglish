package com.lee.mht.system.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lee.mht.system.common.Constant;
import com.lee.mht.system.common.ResultObj;
import com.lee.mht.system.dao.AdminRoleDao;
import com.lee.mht.system.dao.AdminUserDao;
import com.lee.mht.system.entity.AdminUser;
import com.lee.mht.system.service.AdminUserService;
import com.lee.mht.system.service.RedisService;
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

    @Autowired(required = false)
    private AdminRoleDao adminRoleDao;

    @Autowired
    private RedisService redisService;


    @Override
    public AdminUser getAdminUserInfoByUsername(String username) {
        return adminUserDao.getAdminUserByUsername(username);
    }

    @Override
    public PageInfo<AdminUser> getAllAdminUser(String username, String nickname, Integer role_id, Integer pageSize, Integer pageNum) {
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<AdminUser> adminUsers = adminUserDao.getAllAdminUser(username, nickname, role_id);
            return new PageInfo<>(adminUsers);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean updateAdminUser(AdminUser user) {
        try {
            return adminUserDao.updateAdminUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional
    public boolean deleteAdminUserByIds(List<Integer> ids) {
        try {
            for (Integer id : ids) {
                //删除授权缓存
                redisService.deleteUserPermissionCache(id);
                //删除认证缓存
                redisService.deleteUserLoginCache(id);
                //先删除该用户的角色
                adminRoleDao.deleteAllRolesByUserId(id);
            }
            return adminUserDao.deleteAdminUserByIds(ids);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean addAdminUser(AdminUser user) {
        String salt = PasswordUtils.getSalt();
        String password = PasswordUtils.encode("123456", salt);
        user.setSalt(salt);
        user.setPassword(password);
        try {
            return adminUserDao.addAdminUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //重置密码
    @Override
    public boolean restPassword(Integer id) {
        String salt = PasswordUtils.getSalt();
        String password = PasswordUtils.encode("123456", salt);
        try {
            return adminUserDao.restPassword(id, password, salt);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional
    public boolean reassignRoles(List<Integer> rIds, Integer userId) {
        try {
            //先删除所有该用户的角色
            adminRoleDao.deleteAllRolesByUserId(userId);
            if(rIds != null){
                //再添加角色关系
                adminRoleDao.addRolesByUserId(rIds, userId);
            }
            //删除该用户的授权缓存
            redisService.deleteUserPermissionCache(userId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int checkUsernameUnique(String username) {
        return adminUserDao.checkUsernameUnique(username);
    }


}
