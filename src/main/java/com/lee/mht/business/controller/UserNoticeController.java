package com.lee.mht.business.controller;

import com.lee.mht.business.service.UserNoticeService;
import com.lee.mht.system.common.Constant;
import com.lee.mht.system.common.ResultObj;
import com.lee.mht.system.entity.AdminNotice;
import com.lee.mht.system.utils.JwtUtils;
import com.lee.mht.system.vo.NoticeVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author FucXing
 * @date 2022/02/27 14:15
 **/
@RestController
@Slf4j
@RequestMapping("/notice")
public class UserNoticeController {

    @Autowired
    private UserNoticeService userNoticeService;

    @RequestMapping("/getUserNoticeVoListById")
    public ResultObj getUserNoticeVoListById(HttpServletRequest request) {
        try {
            int userId = JwtUtils.getId(request);
            List<NoticeVo> noticeVoList = userNoticeService.getUserNoticeVoListById(userId);
            return new ResultObj(Constant.OK, Constant.QUERY_SUCCESS, noticeVoList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResultObj(Constant.SERVER_ERROR_CODE, Constant.QUERY_ERROR);
        }
    }

    @RequestMapping("/confirmNotice")
    public ResultObj confirmNotice(@RequestBody AdminNotice notice, HttpServletRequest request) {
        try {
            int userId = JwtUtils.getId(request);
            userNoticeService.confirmUserNotice(notice.getId(), userId);
            return new ResultObj(Constant.OK, Constant.CONFIRM_SUCCESS);
        }catch (Exception e) {
            log.error(e.getMessage());
            return new ResultObj(Constant.SERVER_ERROR_CODE, Constant.CONFIRM_ERROR);
        }
    }
}
