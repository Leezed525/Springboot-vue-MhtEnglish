package com.lee.mht.system.service.impl;

import com.lee.mht.system.common.Constant;
import com.lee.mht.system.dao.AdminLogDao;
import com.lee.mht.system.dao.AdminRoleDao;
import com.lee.mht.system.entity.AdminLog;
import com.lee.mht.system.service.RedisService;
import com.lee.mht.system.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
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
    private final String authorizationKey = Constant.Redis_SHiRO_AUTHORIZATION_KEY;
    private final String authenticationKey = Constant.Redis_SHiRO_AUTHENTICATION_KEY;

    private final String MhtLogKey = Constant.REDIS_MHT_LOG_KEY;

    private final String TokenKey = Constant.REDIS_TOKEN_KEY;

    private final Long TokenExpire = Constant.TOKEN_EXPIRE_TIME;

    private final String logLockKey = Constant.MHT_LOCK_HEAD_KEY + Constant.MHT_LOG_LOCK_KEY;


    @Autowired(required = false)
    private AdminRoleDao adminRoleDao;

    @Autowired(required = false)
    private AdminLogDao adminLogDao;

    @Autowired
    private RedisUtils redisUtils;


    @Override
    //删除用户授权缓存
    public void deleteUserPermissionCache(Integer userId) {
        String queryKey = authorizationKey + userId;
        if(redisUtils.hasKey(queryKey)){
            redisUtils.del(queryKey);
        }
    }

    @Override
    //通过角色id去删除拥有这个角色的用户缓存
    public void deleteRolePermissionsCache(Integer roleId) {
        //通过roleId获得所有的有这个角色的用户id
        List<Integer> userIds = adminRoleDao.getAllUsersByRoleId(roleId);
        for(int id : userIds){
            deleteUserPermissionCache(id);
        }
    }

    @Override
    //删除用户认证缓存
    public void deleteUserLoginCache(Integer id) {
        String queryKey = authenticationKey + id;
        if(redisUtils.hasKey(queryKey)){
            redisUtils.del(queryKey);
        }
    }


    @Override
    //添加用户登录token
    public void setAdminUserLoginToken(int user_id, String accessToken) {
        redisUtils.set(TokenKey + user_id, accessToken,TokenExpire);
    }

    @Override
    @Async("asyncServiceExecutor")
    public void pushMhtLogList(AdminLog adminLog) {
        while (true){
            //如果有锁就循环
            if(redisUtils.hasKey(logLockKey) && (redisUtils.get(logLockKey)).equals(1)){
                log.info("存入log等待释放锁中");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else{
                break;
            }
        }
        redisUtils.lPush(MhtLogKey,adminLog);
    }

    @Override
    @Transactional
    public void saveLogFromRedisToMysql() {
        if(redisUtils.hasKey(MhtLogKey)){
            if(redisUtils.hasKey(logLockKey)){//有锁加一
                redisUtils.incr(logLockKey,1);
            }
            else{//没锁创造锁
                redisUtils.set(logLockKey,1);
            }
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
            //释放锁
            redisUtils.decr(logLockKey,1);
        }
    }


}
