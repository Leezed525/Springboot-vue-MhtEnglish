package com.lee.mht.business.dao;

import com.lee.mht.business.entity.Signin;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author FucXing
 * @date 2022/02/12 13:09
 **/
public interface SigninDao {
    int getTodaySignDays(@Param("uId") int userId);

    int getYesterdaySignDays(@Param("uId") int userId);

    void signinToday(@Param("uId") int userId,
                     @Param("days") int days);

    List<Signin> getSigninList(@Param("uId") int userId);
}
