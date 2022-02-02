package com.lee.mht.business.service.impl;

import com.lee.mht.business.dao.UserDao;
import com.lee.mht.business.entity.User;
import com.lee.mht.business.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author FucXing
 * @date 2022/02/01 15:07
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired(required = false)
    private UserDao userDao;


    @Override
    public User getUserInfoById(int id) {
        return userDao.getUserById(id);
    }
}
