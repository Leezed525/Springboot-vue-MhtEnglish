package com.lee.mht.business.controller;

import com.lee.mht.business.entity.User;
import com.lee.mht.business.service.BusinessService;
import com.lee.mht.system.common.Constant;
import com.lee.mht.system.common.ResultObj;
import com.lee.mht.system.shiro.JwtToken;
import com.lee.mht.system.utils.JacksonUtils;
import com.lee.mht.system.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author FucXing
 * @date 2022/02/01 15:04
 **/
@RestController
@RequestMapping("/business")
@Slf4j
public class BusinessController {


    @Autowired
    private BusinessService businessService;

    @RequestMapping("/login")
    public ResultObj login(@RequestBody User user) {
        try {
            String token = businessService.login(user);
            return new ResultObj(Constant.OK, Constant.LOGIN_SUCCESS, token);
        } catch (Exception e) {
            return new ResultObj(Constant.OK, Constant.LOGIN_ERROR);
        }
    }

    /**
     * 获取四六级考试时间
     * @return 考试时间字符串（YYYY-MM-dd）
     */
    @RequestMapping("/getTimeForCet")
    public ResultObj getTimeForCet(){
        try {
            return new ResultObj(Constant.OK, Constant.QUERY_SUCCESS, Constant.timeForCet);
        }catch (Exception e) {
            log.error(e.getMessage());
            return new ResultObj(Constant.SERVER_ERROR, Constant.QUERY_ERROR);
        }
    }
}
