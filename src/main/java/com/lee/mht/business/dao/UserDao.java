package com.lee.mht.business.dao;

import com.lee.mht.business.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserDao {
    Integer isNewUser(@Param("openId") String openId);

    Integer registerUser(@Param("user")User user);

    User getUserByOpenId(@Param("openId") String openId);

    User getUserById(@Param("id") int id);
}
