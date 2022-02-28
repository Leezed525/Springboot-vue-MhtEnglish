package com.lee.mht.business.schedule;

import com.lee.mht.business.service.BusinessService;
import com.lee.mht.system.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author FucXing
 * @date 2022/02/12 16:16
 **/
@Component
@Slf4j
public class BusinessSchedule {
    @Autowired
    private BusinessService businessService;

    @Autowired
    private RedisService redisService;


    // cron表达式，秒 分 时 日 月 周
    @Scheduled(cron = "0 0 1 * * ?")
    public void updateTimeForCetFromPath(){
        //定时读取文件更新四六级考试时间
        businessService.updateTimeForCetFromPath();
    }

    /**
     * 每天00：05把缓存中的学习时间保存
     * 存在一个bug：用户如果在00：00~00：05这个时间段查询学习时间，会缺少上一天的学习时间
     */
    @Scheduled(cron = "0 5 0 * * ?")
    public void saveLearnTimeToDatabase(){
        redisService.saveLearnTimeToDatabase();
    }
}
