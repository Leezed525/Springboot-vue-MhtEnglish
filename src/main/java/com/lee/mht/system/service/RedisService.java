package com.lee.mht.system.service;

import com.lee.mht.system.entity.AdminLog;

/**
 * @author FucXing
 * @date 2022/01/09 22:29
 **/
public interface RedisService {
    //给用户分配角色时清除该用户的授权缓存
    void deleteUserPermissionCache(Integer userId);

    //给角色分配权限是清除拥有该角色的用户授权缓存
    void deleteRolePermissionsCache(Integer roleId);

    void pushMhtLogList(AdminLog adminLog);

    void setAdminUserLoginToken(int user_id, String accessToken,String tokenType);

    void saveLogFromRedisToMysql();

    void deleteUserLoginCache( Integer id);

    void setLearnTimeToday(Integer time, int userId);

    int getLearnTimeToday(int userId);

    void saveLearnTimeToDatabase();
}
