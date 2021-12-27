package com.lee.mht;

import com.lee.mht.system.entity.AdminUser;
import com.lee.mht.system.service.AdminUserService;
import com.lee.mht.system.service.SystemService;
import com.lee.mht.system.utils.PasswordEncoder;
import com.lee.mht.system.utils.PasswordUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootTest
class MhtApplicationTests {

    @Autowired
    AdminUserService adminUserService;

    @Autowired
    SystemService systemService;

    @Test
    void testUpdateAdminUser(){
        AdminUser user = new AdminUser();
        user.setId(15);
        user.setUsername("test13");
        user.setNickname("test13n");
        user.setRoleId(10);
        log.info(String.valueOf(adminUserService.updateAdminUser(user)));

    }

    @Test
    void testDeleteAdminUser(){
        List<Integer> ids = new ArrayList<>();
        ids.add(15);
        ids.add(14);

        log.info(String.valueOf(adminUserService.deleteAdminUserByIds(ids)));

    }
}
