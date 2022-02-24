package com.lee.mht.system.controller;

import com.github.pagehelper.PageInfo;
import com.lee.mht.system.annotation.MhtLog;
import com.lee.mht.system.common.Constant;
import com.lee.mht.system.common.ResultObj;
import com.lee.mht.system.entity.AdminNotice;
import com.lee.mht.system.service.AdminNoticeService;
import com.lee.mht.system.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author FucXing
 * @date 2022/02/22 21:20
 **/
@RestController
@RequestMapping("/admin/notice")
@Slf4j
public class AdminNoticeContoller {

    @Autowired
    private AdminNoticeService adminNoticeService;

    //查询公告列表
    @RequestMapping("/getAllNotice")
    public ResultObj getAllNotice(@RequestParam(required = false, defaultValue = "", name = "authorUserName") String authorUserName,
                                  @RequestParam(required = false, defaultValue = "", name = "title") String title,
                                  @RequestParam(required = false, defaultValue = "", name = "type") String type,
                                  @RequestParam(required = false, name = "publishBeginTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date publishBeginTime,
                                  @RequestParam(required = false, name = "publishEndTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date publishEndTime,
                                  @RequestParam(required = false, name = "updateBeginTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date updateBeginTime,
                                  @RequestParam(required = false, name = "updateEndTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date updateEndTime,
                                  @RequestParam(required = false, defaultValue = "5", name = "limit") Integer pageSize,
                                  @RequestParam(required = false, defaultValue = "1", name = "page") Integer pageNum) {
        PageInfo<AdminNotice> pageInfo = adminNoticeService.getAllNotice(authorUserName, title, type, publishBeginTime, publishEndTime, updateBeginTime, updateEndTime, pageSize, pageNum);
        if (pageInfo != null) {
            return new ResultObj(Constant.OK, Constant.QUERY_SUCCESS, pageInfo);
        } else {
            return new ResultObj(Constant.SERVER_ERROR_CODE, Constant.QUERY_ERROR, null);
        }
    }

    //更新公告
    @PostMapping("/updateNotice")
    public ResultObj updateNotice(@RequestBody AdminNotice notice) {
        try {
            log.info(notice.toString());
            adminNoticeService.updateNotice(notice);
            return new ResultObj(Constant.OK, Constant.UPDATE_SUCCESS);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResultObj(Constant.SERVER_ERROR_CODE, Constant.UPDATE_ERROR);
        }
    }

    @PostMapping("/addNotice")
    public ResultObj addNotice(@RequestBody AdminNotice notice, HttpServletRequest request) {
        try {
            int authorId = JwtUtils.getId(request);
            notice.setAuthorId(authorId);
            Date now = new Date();
            notice.setCreatetime(now);
            notice.setUpdateTime(now);
            notice.setAvailable(false);
            adminNoticeService.addNotice(notice);
            return new ResultObj(Constant.OK, Constant.ADD_SUCCESS);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResultObj(Constant.SERVER_ERROR_CODE, Constant.ADD_ERROR);
        }
    }

    //删
    @RequestMapping("/deleteAdminNoticeByIds")
    public ResultObj deleteAdminNoticeByIds(@RequestBody ArrayList<Integer> ids) {
        try {
            boolean flag = adminNoticeService.deleteAdminNoticeByIds(ids);
            if (flag) {
                return new ResultObj(Constant.OK, Constant.DELETE_SUCCESS);
            } else {
                return new ResultObj(Constant.SERVER_ERROR_CODE, Constant.DELETE_ERROR);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResultObj(Constant.SERVER_ERROR_CODE, Constant.DELETE_ERROR);
        }
    }
}
