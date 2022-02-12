package com.lee.mht.business.service.impl;

import com.lee.mht.business.dao.SigninDao;
import com.lee.mht.business.entity.Signin;
import com.lee.mht.business.service.SigninService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author FucXing
 * @date 2022/02/12 13:09
 **/
@Service
public class SigninServiceImpl implements SigninService {

    @Autowired(required = false)
    private SigninDao signinDao;

    /**
     * 判断今天有没有签到过
     * @param userId 用户id
     * @return 今天签到过了返回true,没签到返回false
     */
    @Override
    public boolean isSigninToday(int userId) {
        int day = signinDao.getTodaySignDays(userId);
        return day > 0;
    }

    /**
     * 在今天签到
     * @param userId 用户id
     */
    @Override
    public void signinToday(int userId) {
        //获取昨天的签到总天数
        int days = signinDao.getYesterdaySignDays(userId);
        //签到
        signinDao.signinToday(userId, days + 1);
    }

    /**
     * 获取签到天数，如果今天签到过就返回今天的签到天数，不然就返回昨天的
     * @param userId 用户id
     * @return 签到天数
     */
    @Override
    public int getSigninDays(int userId) {
        if(isSigninToday(userId)){
            //如果今天签到过，返回今天的签到天数
            return signinDao.getTodaySignDays(userId);
        }else{
            //今天没签到过，返回昨天的
            return signinDao.getYesterdaySignDays(userId);
        }
    }

    @Override
    public List<Signin> getSigninList(int userId) {
        return signinDao.getSigninList(userId);
    }
}
