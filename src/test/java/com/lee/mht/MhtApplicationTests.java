package com.lee.mht;

import com.lee.mht.system.common.Constant;
import com.lee.mht.system.entity.AdminUser;
import com.lee.mht.system.service.AdminUserService;
import com.lee.mht.system.service.SystemService;
import com.lee.mht.system.utils.JwtUtils;
import com.lee.mht.system.utils.PasswordEncoder;
import com.lee.mht.system.utils.PasswordUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Test
    void TestJWTUtils(){
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaXNzIjoiTGVlWmVkIiwiand0LXVzZXItbmFtZS1rZXkiOiJyb290IiwiZXhwIjoxNjQwNjIyNTU5LCJpYXQiOjE2NDA2MjIyNTl9.Okkbtl9UJJeekOmbXnJoAeaSrzJ3YzOJNkdxNDFUmsE";
        log.info(String.valueOf(JwtUtils.getClaimsFromToken(token)));


        //Map<String, Object> claims = new HashMap<>();
        //
        //claims.put(Constant.JWT_USER_NAME, "test");
        //String accessToken = JwtUtils.getAccessToken(String.valueOf(user_id), claims);
    }

}
