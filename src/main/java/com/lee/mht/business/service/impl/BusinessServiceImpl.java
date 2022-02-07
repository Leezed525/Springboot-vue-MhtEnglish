package com.lee.mht.business.service.impl;

import com.lee.mht.business.dao.UserDao;
import com.lee.mht.business.entity.User;
import com.lee.mht.business.service.BusinessService;
import com.lee.mht.system.common.Constant;
import com.lee.mht.system.service.RedisService;
import com.lee.mht.system.utils.JacksonUtils;
import com.lee.mht.system.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author FucXing
 * @date 2022/02/01 20:17
 **/
@Service
@Slf4j
public class BusinessServiceImpl implements BusinessService {
    @Value("${LeeMht.AppID}")
    private String APPID;

    @Value("${LeeMht.AppSecret}")
    private String APP_SECRET;

    @Autowired(required = false)
    private UserDao userDao;

    @Autowired
    private RedisService redisService;

    @Override
    @Transactional
    public String login(User user) {
        //判断是不是新用户
        //获取前端传过来的code(被我存放在openId的位置)
        String code = user.getOpenId();
        String openId = getOpenId(code);
        user.setOpenId(openId);
        int count = userDao.isNewUser(openId);
        if (count > 0) {//不是新用户
            //生成token传回前端
            user = userDao.getUserByOpenId(openId);
        } else {//是新用户
            //注册
            userDao.registerUser(user);
        }
        String accessToken = JwtUtils.generateMhtToken(String.valueOf(user.getId()), user.getNickname(), Constant.JWT_USER_TYPE_BUSINESS);
        redisService.setAdminUserLoginToken(user.getId(), accessToken,Constant.JWT_USER_TYPE_BUSINESS);
        return accessToken;
    }

    //通过前端的code获得openId
    private String getOpenId(String code) {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid={appid}&secret={secret}&js_code={code}&grant_type=authorization_code";
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("appid", APPID);
        requestMap.put("secret", APP_SECRET);
        requestMap.put("code", code);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class, requestMap);
        Map<String, Object> map = JacksonUtils.readValue(responseEntity.getBody(), Map.class);

        return String.valueOf(map.get("openid"));
    }
}