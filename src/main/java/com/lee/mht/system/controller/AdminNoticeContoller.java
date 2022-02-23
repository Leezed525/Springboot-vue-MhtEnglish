package com.lee.mht.system.controller;

import com.github.pagehelper.PageInfo;
import com.lee.mht.system.common.Constant;
import com.lee.mht.system.common.ResultObj;
import com.lee.mht.system.entity.AdminLog;
import com.lee.mht.system.entity.AdminNotice;
import com.lee.mht.system.service.AdminNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author FucXing
 * @date 2022/02/22 21:20
 **/
@RestController
@RequestMapping("/admin/notice")
public class AdminNoticeContoller {

    @Autowired
    private AdminNoticeService adminNoticeService;

    //查询日志列表
    @RequestMapping("/getAllNotice")
    public ResultObj getAllNotice(@RequestParam(required = false, defaultValue = "", name = "authorUserName") String authorUserName,
                               @RequestParam(required = false, defaultValue = "", name = "title") String title,
                               @RequestParam(required = false, defaultValue = "", name = "type") String type,
                               @RequestParam(required = false, name = "publishBeginTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date publishBeginTime,
                               @RequestParam(required = false, name = "publishEndTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")  Date publishEndTime,
                               @RequestParam(required = false, name = "updateBeginTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date updateBeginTime,
                               @RequestParam(required = false, name = "updateEndTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")  Date updateEndTime,
                               @RequestParam(required = false, defaultValue = "5", name = "limit") Integer pageSize,
                               @RequestParam(required = false, defaultValue = "1", name = "page") Integer pageNum) {
        PageInfo<AdminNotice> pageInfo = adminNoticeService.getAllNotice(authorUserName, title, type, publishBeginTime, publishEndTime,updateBeginTime,updateEndTime, pageSize, pageNum);
        if (pageInfo != null) {
            return new ResultObj(Constant.OK, Constant.QUERY_SUCCESS, pageInfo);
        } else {
            return new ResultObj(Constant.SERVER_ERROR_CODE, Constant.QUERY_ERROR, null);
        }
    }

}
