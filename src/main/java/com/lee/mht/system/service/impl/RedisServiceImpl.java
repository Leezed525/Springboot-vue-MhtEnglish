package com.lee.mht.system.service.impl;

import com.lee.mht.system.common.Constant;
import com.lee.mht.system.dao.AdminLogDao;
import com.lee.mht.system.dao.AdminRoleDao;
import com.lee.mht.system.entity.AdminLog;
import com.lee.mht.system.service.RedisService;
import com.lee.mht.system.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author FucXing
 * @date 2022/01/09 22:29
 **/

@Service
@Slf4j
public class RedisServiceImpl implements RedisService {
    private final String ShiroRedisPermissionskey = Constant.Redis_SHiRO_AUTHORIZATION_KEY;

    private final String MhtLogKey = Constant.REDIS_MHT_LOG_KEY;

    private final String TokenKey = Constant.REDIS_TOKEN_KEY;

    private final Long TokenExpire = Constant.TOKEN_EXPIRE_TIME;

    @Autowired(required = false)
    private AdminRoleDao adminRoleDao;

    @Autowired(required = false)
    private AdminLogDao adminLogDao;

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
    public void deleteRolePermissionsCache(Integer roleId) {
        //通过roleId获得所有的有这个角色的用户id
        List<Integer> userIds = adminRoleDao.getAllUsersByRoleId(roleId);
        for(int id : userIds){
            if(redisUtils.hasKey(ShiroRedisPermissionskey + id)){
                redisUtils.del(ShiroRedisPermissionskey + id);
            }
        }
    }


    @Override
    public void setAdminUserLoginToken(int user_id, String accessToken) {
        redisUtils.set(TokenKey + user_id, accessToken,TokenExpire);
    }

    @Override
    public void pushMhtLogList(AdminLog adminLog) {
        redisUtils.lPush(MhtLogKey,adminLog);
    }

    @Override
    @Transactional
    public void saveLogFromRedisToMysql() {
        if(redisUtils.hasKey(MhtLogKey)){
            List<Object> logs = redisUtils.lGet(MhtLogKey,0,-1);
            List<AdminLog> adminLogs = new ArrayList<>();
            for(Object object:logs){
                AdminLog adminLog = (AdminLog) object;
                adminLogs.add(adminLog);
            }
            boolean flag = adminLogDao.batchSaveLogs(adminLogs);
            if(flag){
                redisUtils.del(MhtLogKey);
            }
        }
    }
}
