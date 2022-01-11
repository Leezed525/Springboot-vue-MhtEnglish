package com.lee.mht.system.schedule;

import com.lee.mht.system.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author FucXing
 * @date 2022/01/10 22:36
 **/
@Component
@Slf4j
public class SaveLogSchedule {

    @Autowired
    private RedisService redisService;

    // cron表达式，秒 分 时 日 月 周
    @Scheduled(cron = "0 */10 * * * ?")
    public void saveLogFromRedisToMysql(){
        //定时从redis中获取log批量添加到mysql中
        redisService.saveLogFromRedisToMysql();
    }
}
