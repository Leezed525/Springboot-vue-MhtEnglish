package com.lee.mht;

import com.lee.mht.system.common.Constant;
import com.lee.mht.system.entity.AdminLog;
import com.lee.mht.system.entity.AdminUser;
import com.lee.mht.system.service.AdminUserService;
import com.lee.mht.system.service.RedisService;
import com.lee.mht.system.service.SystemService;
import com.lee.mht.system.utils.*;
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

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    RedisService redisService;

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


    @Test
    void redisTest(){
        redisUtils.set("hello",new TreeNode(123,"测试"));
        TreeNode treenode = ((TreeNode) redisUtils.get("hello"));
        log.info(treenode.getLabel());
    }

    @Test
    void getLogFromRedisTest(){
        List<Object> logs = redisUtils.lGet(Constant.REDIS_MHT_LOG_KEY,0,-1);
        for(Object object:logs){
            AdminLog adminLog = (AdminLog) object;
            log.info(adminLog.toString());
        }
    }

    @Test
    void testsavelog(){
        redisService.saveLogFromRedisToMysql();
    }


}
