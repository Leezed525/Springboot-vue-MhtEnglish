package com.lee.mht.system.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lee.mht.system.annotation.CostTime;
import com.lee.mht.system.dao.AdminPermissionDao;
import com.lee.mht.system.dao.AdminRoleDao;
import com.lee.mht.system.entity.AdminPermission;
import com.lee.mht.system.entity.AdminRole;
import com.lee.mht.system.service.AdminPermissionService;
import com.lee.mht.system.service.RedisService;
import com.lee.mht.system.utils.TreeNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author FucXing
 * @date 2022/01/01 00:53
 **/

@Slf4j
@Service
@CacheConfig(cacheNames = "AdminPermission")
public class AdminPermissionServiceImpl implements AdminPermissionService {

    @Autowired(required = false)
    private AdminPermissionDao adminPermissionDao;

    @Autowired(required = false)
    private AdminRoleDao adminRoleDao;

    @Autowired
    private RedisService redisService;

    @Override
    public PageInfo<AdminPermission> getAllAdminPermission(String title, String percode, Integer pId, int pageSize, int pageNum) {
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<AdminPermission> adminPermissions = adminPermissionDao.getAllAdminPermission(title, percode, pId);
            return new PageInfo<>(adminPermissions);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<AdminPermission> getAllMenu() {
        try {
            return adminPermissionDao.getAllMenu();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean updateAdminPermission(AdminPermission permission) {
        return adminPermissionDao.updateAdminPermission(permission);
    }

    @Override
    public boolean addAdminPermission(AdminPermission permission) {
        return adminPermissionDao.addAdminPermission(permission);
    }

    @Override
    public int checkPermissionnameUnique(String title) {
        return adminPermissionDao.checkPermissionnameUnique(title);

    }

    @Override
    @Transactional
    public boolean deleteAdminPermissionByIds(ArrayList<Integer> ids) {
        try {
            List<Integer> RoleIds = new ArrayList<>();
            //删除拥有该权限的用户的授权缓存
            for(Integer id : ids) {
                RoleIds = adminRoleDao.getRoleIdsRelationToPermissionById(id);
                for (Integer roleId : RoleIds) {
                    redisService.deleteRolePermissionsCache(roleId);
                }
            }
            adminPermissionDao.deletePermissionRealtionToRole(ids);
            adminPermissionDao.deleteAdminPermissionByIds(ids);
            return true;
        }catch (Exception e ){
            e.printStackTrace();
            return false;
        }
    }


    //获取权限树
    @Override
    public List<TreeNode> getPermissionTree() {
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
            return pids;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<AdminPermission> getPermissionByRoleId(Integer roleId) {
        try {
            return adminPermissionDao.getAllPermissionsByRoleId(roleId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @CostTime
    public List<String> getAllPermissionByUserId(int user_id) {
        //获取该用户所有角色
        List<AdminRole> roles = adminRoleDao.getAllRolesByUserId(user_id);

        return adminPermissionDao.getAllPermissionPercodesByRoleIds(roles);
    }
}
