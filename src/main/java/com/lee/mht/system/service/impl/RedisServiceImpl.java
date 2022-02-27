package com.lee.mht.system.service.impl;

import com.lee.mht.business.dao.WordDao;
import com.lee.mht.business.entity.LearnTime;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

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

    private final String AdminTokenKey = Constant.REDIS_ADMIN_USER_TOKEN_KEY;

    private final String BusinessTokenKey = Constant.REDIS_BUSINESS_USER_TOKEN_KEY;

    private final Long TokenExpire = Constant.TOKEN_EXPIRE_TIME;

    private final String logLockKey = Constant.MHT_LOCK_HEAD_KEY + Constant.MHT_LOG_LOCK_KEY;


    @Autowired(required = false)
    private AdminRoleDao adminRoleDao;

    @Autowired(required = false)
    private AdminLogDao adminLogDao;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired(required = false)
    private WordDao wordDao;

    @Override
    //删除用户授权缓存
    public void deleteUserPermissionCache(Integer userId) {
        String queryKey = authorizationKey + userId;
        if (redisUtils.hasKey(queryKey)) {
            redisUtils.del(queryKey);
        }
    }

    @Override
    //通过角色id去删除拥有这个角色的用户缓存
    public void deleteRolePermissionsCache(Integer roleId) {
        //通过roleId获得所有的有这个角色的用户id
        List<Integer> userIds = adminRoleDao.getAllUsersByRoleId(roleId);
        for (int id : userIds) {
            deleteUserPermissionCache(id);
        }
    }

    @Override
    //删除用户认证缓存
    public void deleteUserLoginCache(Integer id) {
        String queryKey = authenticationKey + id;
        if (redisUtils.hasKey(queryKey)) {
            redisUtils.del(queryKey);
        }
    }


    @Override
    //添加用户登录token
    public void setAdminUserLoginToken(int user_id, String accessToken, String tokenType) {
        if (tokenType.equals(Constant.JWT_USER_TYPE_ADMIN)) {
            redisUtils.set(AdminTokenKey + user_id, accessToken, TokenExpire);
        } else {
            redisUtils.set(BusinessTokenKey + user_id, accessToken, TokenExpire);
        }
    }

    @Override
    @Async("asyncServiceExecutor")
    public void pushMhtLogList(AdminLog adminLog) {
        while (true) {
            //如果有锁就循环
            if (redisUtils.hasKey(logLockKey) && (redisUtils.get(logLockKey)).equals(1)) {
                log.info("存入log等待释放锁中");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                break;
            }
        }
        redisUtils.lPush(MhtLogKey, adminLog);
    }

    @Override
    @Transactional
    public void saveLogFromRedisToMysql() {
        //如果有日志被缓存
        if (redisUtils.hasKey(MhtLogKey)) {
            redisUtils.set(logLockKey, 1,10);//创建锁
            List<Object> logs = redisUtils.lGet(MhtLogKey, 0, -1);
            List<AdminLog> adminLogs = new ArrayList<>();
            for (Object object : logs) {
                AdminLog adminLog = (AdminLog) object;
                adminLogs.add(adminLog);
            }
            try{
                boolean flag = adminLogDao.batchSaveLogs(adminLogs);
                if (flag) {//保存成功删除锁
                    redisUtils.del(MhtLogKey);
                }
            }catch (Exception e) {
                log.error("日志保存失败");
                log.error(e.getMessage());
            }
            //释放锁
            redisUtils.decr(logLockKey, 1);
        }
    }


    @Override
    public void setLearnTimeToday(Integer time, int userId) {
        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        String key = Constant.LEARN_TIME_HEAD + sqlDate.toString() + ":" + userId;
        if (redisUtils.hasKey(key)) {
            LearnTime learnTime = (LearnTime) redisUtils.get(key);
            int tmp = learnTime.getTime();
            tmp += time;
            learnTime.setTime(tmp);
            redisUtils.set(key, learnTime);
        } else {
            LearnTime learnTime;
            learnTime = new LearnTime(userId, sqlDate, time);
            redisUtils.set(key, learnTime);
        }
    }

    @Override
    public int getLearnTimeToday(int userId) {
        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        String key = Constant.LEARN_TIME_HEAD + sqlDate.toString() + ":" + userId;
        if(redisUtils.hasKey(key)){
            LearnTime learnTime = (LearnTime) redisUtils.get(key);
            return learnTime.getTime();
        }else{
            return 0;
        }
    }

    @Override
    @Transactional
    public void saveLearnTimeToDatabase() {
        Set<String> keys = redisUtils.scan(Constant.LEARN_TIME_HEAD+"*",100);
        Date utilDate=new Date();
        DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        String dateString = format.format(utilDate);
        List<LearnTime> learnTimeList = new ArrayList<LearnTime>();
        List<String> deleteKeyList = new ArrayList<String>();
        for(String key :keys) {
            if(!key.contains(dateString)){
                LearnTime tmp = (LearnTime) redisUtils.get(key);
                learnTimeList.add(tmp);
                deleteKeyList.add(key);
            }
        }
        if(learnTimeList.size() > 0){
            try {
                wordDao.saveLearnTime(learnTimeList);
                redisUtils.del(deleteKeyList);
            }catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    @Override
    public boolean isLimitExceeded(int userId) {
        String key = Constant.MHT_REQUEST_LIMIT_KEY + userId;
        long count = redisUtils.incr(key,1);
        if(count == 1){
            redisUtils.expire(key, Constant.MHT_REQUEST_LIMIT_TIME);
        }else if(count > Constant.MHT_REQUEST_LIMIT_COUNT){
            return true;
        }
        return false;
    }

}
