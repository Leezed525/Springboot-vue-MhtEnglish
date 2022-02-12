package com.lee.mht.business.controller;

import com.lee.mht.business.entity.Signin;
import com.lee.mht.business.service.SigninService;
import com.lee.mht.system.common.Constant;
import com.lee.mht.system.common.ResultObj;
import com.lee.mht.system.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author FucXing
 * @date 2022/02/12 13:09
 **/
@RestController
@RequestMapping("/signin")
@Slf4j
public class SigninController {

    @Autowired
    private SigninService signinService;

    /**
     * 在今天签到
     *
     * @param request request
     * @return resultObj
     */
    @RequestMapping("/signinToday")
    public ResultObj signinToday(HttpServletRequest request) {
        try {
            int userId = JwtUtils.getId(request);
            //判断今天有没有签到过
            boolean flag = signinService.isSigninToday(userId);
            if (!flag) {
                //没签到过
                signinService.signinToday(userId);
                return new ResultObj(Constant.OK, Constant.SIGNIN_SUCCESS);
            } else {
                //签到过了
                return new ResultObj(Constant.SERVER_ERROR, Constant.SIGNIN_DUPLICATE);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResultObj(Constant.SERVER_ERROR, Constant.SIGNIN_FAIL);
        }
    }

    /**
     * 获取签到天数，如果今天签到过就返回今天的签到天数，不然就返回昨天的
     *
     * @param request
     * @return
     */
    @RequestMapping("/getSigninDays")
    public ResultObj getSigninDays(HttpServletRequest request) {
        try {
            int userId = JwtUtils.getId(request);
            int days = signinService.getSigninDays(userId);
            return new ResultObj(Constant.OK, Constant.QUERY_SUCCESS, days);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResultObj(Constant.SERVER_ERROR, Constant.QUERY_ERROR);
        }
    }

    /**
     * 获取当前签到状态
     *
     * @param request request
     * @return 今天已签到返回true，未签到返回false
     */
    @RequestMapping("/getTodaySignInStatus")
    public ResultObj getTodaySignInStatus(HttpServletRequest request) {
        try {
            int userId = JwtUtils.getId(request);
            boolean flag = signinService.isSigninToday(userId);
            return new ResultObj(Constant.OK, Constant.QUERY_SUCCESS, flag);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResultObj(Constant.SERVER_ERROR, Constant.QUERY_ERROR);
        }
    }

    @RequestMapping("/getSignInfo")
    public ResultObj getSignInfo(HttpServletRequest request) {
        try {
            int userId = JwtUtils.getId(request);
            List<Signin> list = signinService.getSigninList(userId);
            return new ResultObj(Constant.OK, Constant.QUERY_SUCCESS, list);
        }catch (Exception e) {
            log.error(e.getMessage());
            return new ResultObj(Constant.SERVER_ERROR, Constant.QUERY_ERROR);
        }
    }
}
