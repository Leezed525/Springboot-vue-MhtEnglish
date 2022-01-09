package com.lee.mht.system.service;

/**
 * @author FucXing
 * @date 2022/01/09 22:29
 **/
public interface RedisService {
    void deleteUserPermissionCache(Integer userId);

    void deleteRolePermissions(Integer roleId);
}
