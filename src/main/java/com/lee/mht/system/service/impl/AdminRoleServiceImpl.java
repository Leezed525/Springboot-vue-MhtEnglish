package com.lee.mht.system.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lee.mht.system.dao.AdminPermissionDao;
import com.lee.mht.system.dao.AdminRoleDao;
import com.lee.mht.system.entity.AdminRole;
import com.lee.mht.system.service.AdminRoleService;
import com.lee.mht.system.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    @Autowired(required = false)
    private AdminPermissionDao adminPermissionDao;

    @Autowired
    private RedisService redisService;


    @Override
    public List<AdminRole> getAllRoles() {
        try {
            return adminRoleDao.getAllRoles();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<AdminRole> getAllRolesByUserId(Integer userId) {
        try {
            return adminRoleDao.getAllRolesByUserId(userId);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public PageInfo<AdminRole> getAllAdminRole(String roleName, String comment, int pageSize, int pageNum) {
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<AdminRole> adminRoles = adminRoleDao.getAllAdminRole(roleName, comment);
            return new PageInfo<>(adminRoles);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean updateAdminRole(AdminRole role) {
        try {
            return adminRoleDao.updateAdminRole(role);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int checkRolenameUnique(String rolename) {
        return adminRoleDao.checkRolenameUnique(rolename);
    }

    @Override
    public boolean addAdminRole(AdminRole role) {
        try {
            return adminRoleDao.addAdminRole(role);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional
    public boolean deleteAdminRoleByIds(ArrayList<Integer> ids) {
        try {
            //??????role???user ,role???permission?????????
            for (Integer id : ids) {
                adminPermissionDao.deleteAllPermissionByRoleId(id);
                adminRoleDao.deleteRoleRelationToUser(id);
            }
            //??????????????????role????????????????????????
            for (Integer id : ids) {
                redisService.deleteRolePermissionsCache(id);
            }
            return adminRoleDao.deleteAdminRoleByIds(ids);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional
    public boolean reassignPermissions(ArrayList<Integer> pIds, Integer roleId) {
        try {
            //?????????????????????????????????
            adminPermissionDao.deleteAllPermissionByRoleId(roleId);
            if (pIds != null) {
                //??????type??????menu?????????permissionId
                List<Integer> permissionids = adminPermissionDao.getIdsNotMenu(pIds);
                if (permissionids != null) {
                    //?????????????????????
                    adminPermissionDao.addPermissionRelationToRoleByRoleId((ArrayList<Integer>) permissionids, roleId);
                }
            }
            //???????????????????????????????????????????????????
            redisService.deleteRolePermissionsCache(roleId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
