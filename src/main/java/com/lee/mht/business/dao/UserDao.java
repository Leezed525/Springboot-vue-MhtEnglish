package com.lee.mht.business.dao;

import com.lee.mht.business.entity.User;
import com.lee.mht.system.vo.UserCountVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDao {
    Integer isNewUser(@Param("openId") String openId);

    Integer registerUser(@Param("user")User user);

    User getUserByOpenId(@Param("openId") String openId);

    User getUserById(@Param("id") int id);

    void updateUserInfo(@Param("user") User user);

    List<UserCountVo> getRecentWeekNewUserCount();

    List<UserCountVo> getRecentWeekActiveUserCount();

    List<UserCountVo> getRecentWeekAllUserCount();
}
