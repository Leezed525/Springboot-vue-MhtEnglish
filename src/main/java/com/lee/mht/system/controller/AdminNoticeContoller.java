package com.lee.mht.system.controller;

import com.github.pagehelper.PageInfo;
import com.lee.mht.system.annotation.MhtLog;
import com.lee.mht.system.common.Constant;
import com.lee.mht.system.common.ResultObj;
import com.lee.mht.system.entity.AdminNotice;
import com.lee.mht.system.service.AdminNoticeService;
import com.lee.mht.system.utils.JwtUtils;
import com.lee.mht.system.vo.NoticeVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    @RequiresPermissions("notice:query")
    @MhtLog(action = "公告查询",type = Constant.LOG_TYPE_SYSTEM)
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
    @RequiresPermissions("notice:update")
    @MhtLog(action = "公告更新",type = Constant.LOG_TYPE_SYSTEM)
    public ResultObj updateNotice(@RequestBody AdminNotice notice) {
        try {
            adminNoticeService.updateNotice(notice);
            return new ResultObj(Constant.OK, Constant.UPDATE_SUCCESS);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResultObj(Constant.SERVER_ERROR_CODE, Constant.UPDATE_ERROR);
        }
    }

    /**
     * 新增公告
     *
     * @param notice  公告
     * @param request request
     * @return ResultObj
     */
    @PostMapping("/addNotice")
    @RequiresPermissions("notice:add")
    @MhtLog(action = "公告添加",type = Constant.LOG_TYPE_SYSTEM)
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

    /**
     * 删除公告
     *
     * @param notice 要删除的公告
     * @return ResultObj
     */
    @RequestMapping("/deleteAdminNotice")
    @RequiresPermissions("notice:delete")
    @MhtLog(action = "公告删除",type = Constant.LOG_TYPE_SYSTEM)
    public ResultObj deleteAdminNotice(@RequestBody AdminNotice notice) {
        try {
            boolean flag = adminNoticeService.deleteAdminNoticeByIds(notice);
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

    @RequestMapping("/publishNotice")
    @RequiresPermissions("notice:publish")
    @MhtLog(action = "公告发布",type = Constant.LOG_TYPE_SYSTEM)
    public ResultObj publishNotice(@RequestBody AdminNotice notice) {
        if (notice.getAvailable()) {
            return new ResultObj(Constant.SERVER_ERROR_CODE, Constant.NOTICE_IS_PUBLISHED);
        }
        try {
            adminNoticeService.publishNotice(notice);
            return new ResultObj(Constant.OK, Constant.PUBLISH_SUCCESS);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResultObj(Constant.SERVER_ERROR_CODE, Constant.PUBLISH_ERROR);
        }
    }

    @RequestMapping("/cancelNotice")
    @RequiresPermissions("notice:cancel")
    @MhtLog(action = "公告撤销",type = Constant.LOG_TYPE_SYSTEM)
    public ResultObj cancelNotice(@RequestBody AdminNotice notice) {
        if (!notice.getAvailable()) {
            return new ResultObj(Constant.SERVER_ERROR_CODE, Constant.NOTICE_ISNOT_PUBLISHED);
        }
        try {
            adminNoticeService.cancelNotice(notice);
            return new ResultObj(Constant.OK, Constant.CANCEL_SUCCESS);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResultObj(Constant.SERVER_ERROR_CODE, Constant.CANCEL_ERROR);
        }
    }

    @RequestMapping("/getAdminNoticeById")
    @MhtLog(action = "公告查询",type = Constant.LOG_TYPE_SYSTEM)
    public ResultObj getAdminNoticeById(HttpServletRequest request) {
        try {
            int userId = JwtUtils.getId(request);
            List<NoticeVo> noticeVoList = adminNoticeService.getAdminNoticeById(userId);
            return new ResultObj(Constant.OK, Constant.QUERY_SUCCESS, noticeVoList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResultObj(Constant.SERVER_ERROR_CODE, Constant.QUERY_ERROR);
        }
    }

    @RequestMapping("/confirmNotice")
    @MhtLog(action = "公告确认",type = Constant.LOG_TYPE_SYSTEM)
    public ResultObj confirmNotice(@RequestBody AdminNotice notice, HttpServletRequest request) {
        try {
            int userId = JwtUtils.getId(request);
            adminNoticeService.confirmAdminNotice(notice.getId(), userId);
            return new ResultObj(Constant.OK, Constant.CONFIRM_SUCCESS);
        }catch (Exception e) {
            log.error(e.getMessage());
            return new ResultObj(Constant.SERVER_ERROR_CODE, Constant.CONFIRM_ERROR);
        }
    }
}
