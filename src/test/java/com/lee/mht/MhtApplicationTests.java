package com.lee.mht;

import com.lee.mht.system.dao.AdminUserDao;
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

    private final static Logger logger = LoggerFactory.getLogger(MhtApplicationTests.class);
    @Test
    void contextLoads() {
        logger.info("123");
        logger.info("123");
        logger.info("123");
        logger.info("123");
        logger.info("123");
        logger.info("123");
        logger.info("123");
        logger.info("123");
        logger.info("123");
        logger.info("123");
    }
    @Test
    void TestTokenUtils(){
        String username = "root";
        String token = TokenUtils.getToken(username,"123456");
        log.info(TokenUtils.getUsername(token));
    }
}
