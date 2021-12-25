package com.lee.mht;

import com.lee.mht.system.dao.AdminUserDao;
import com.lee.mht.system.entity.AdminUser;
import com.lee.mht.system.utils.PasswordEncoder;
import com.lee.mht.system.utils.PasswordUtils;
import com.lee.mht.system.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;

@Slf4j
@SpringBootTest
class MhtApplicationTests {

    @Autowired(required = false)
    AdminUserDao adminUserDao;

    @Test
    void getsalt(){
        log.info(PasswordUtils.getSalt());
    }

    @Test
    void getEncode(){
        String salt = "15bf0de2f2cb41eaad2d";
        String password = "123456";
        log.info(new PasswordEncoder(salt).encode(password));
    }

    @Test
    void matchPassword(){
        String salt = "15bf0de2f2cb41eaad2d";
        String password = "123456";
        String encPassword = "f0e3bf64472734614e10072df5996f8c";

        log.info(String.valueOf(PasswordUtils.matches(salt, password,encPassword)));
    }

    @Test
    void testloginUSer(){
        String username = "123";
        AdminUser user = adminUserDao.login(username);
        log.info(user.toString());
        String user_username = user.getUsername();
        log.info(String.valueOf(user_username == null));

    }
}
