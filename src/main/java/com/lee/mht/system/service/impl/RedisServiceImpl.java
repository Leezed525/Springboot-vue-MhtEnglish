package com.lee.mht.system.service.impl;

import com.lee.mht.system.common.Constant;
import com.lee.mht.system.dao.AdminRoleDao;
import com.lee.mht.system.service.RedisService;
import com.lee.mht.system.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author FucXing
 * @date 2022/01/09 22:29
 **/

@Service
public class RedisServiceImpl implements RedisService {
    private String ShiroRedisPermissionskey = Constant.Redis_SHiRO_AUTHORIZATION_KEY;

    @Autowired(required = false)
    private AdminRoleDao adminRoleDao;

    @Autowired
    private RedisUtils redisUtils;


    @Override
    public void deleteUserPermissionCache(Integer userId) {
        String queryKey = ShiroRedisPermissionskey + userId;
        if(redisUtils.hasKey(queryKey)){
            redisUtils.del(queryKey);
        }
    }

    @Override
    public void deleteRolePermissions(Integer roleId) {
        //通过roleId获得所有的有这个角色的用户id
        List<Integer> userIds = adminRoleDao.getAllUsersByRoleId(roleId);
        for(int id : userIds){
            if(redisUtils.hasKey(ShiroRedisPermissionskey + id)){
                redisUtils.del(ShiroRedisPermissionskey + id);
            }
        }
    }
}
