package com.lee.mht.system.controller;

import com.github.pagehelper.PageInfo;
import com.lee.mht.system.annotation.MhtLog;
import com.lee.mht.system.common.Constant;
import com.lee.mht.system.common.ResultObj;
import com.lee.mht.system.entity.AdminLog;
import com.lee.mht.system.service.AdminLogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author FucXing
 * @date 2022/01/10 22:32
 **/
@RestController
@RequestMapping("/admin/log")
public class AdminLogController {


    @Autowired
    private AdminLogService adminLogService;


    //查询日志列表
    @RequestMapping("/getAllLog")
    @RequiresPermissions("adminLog:query")
    @MhtLog(action = "查询日志",type = Constant.LOG_TYPE_SYSTEM)
    public ResultObj getAllLog(@RequestParam(required = false, defaultValue = "", name = "operator") String operator,
                               @RequestParam(required = false, defaultValue = "", name = "action") String action,
                               @RequestParam(required = false, defaultValue = "", name = "result") String result,
                               @RequestParam(required = false, name = "beginTime") @DateTimeFormat(pattern = "yyyy-MM-dd kk:mm:ss") Date beginTime,
                               @RequestParam(required = false, name = "endTime") @DateTimeFormat(pattern = "yyyy-MM-dd kk:mm:ss")  Date endTime,
                               @RequestParam(required = false, defaultValue = "5", name = "limit") String pageSize,
                               @RequestParam(required = false, defaultValue = "1", name = "page") String pageNum) {
        PageInfo<AdminLog> pageInfo = adminLogService.getAllLog(operator, action, result, beginTime, endTime, Integer.parseInt(pageSize), Integer.parseInt(pageNum));
        if (pageInfo != null) {
            return new ResultObj(Constant.OK, Constant.QUERY_SUCCESS, pageInfo);
        } else {
            return new ResultObj(Constant.SERVER_ERROR_CODE, Constant.QUERY_ERROR, null);
        }
    }

    //删
    @RequestMapping("/deleteAdminLogByIds")
    @RequiresPermissions("adminLog:delete")
    @MhtLog(action = "删除日志",type = Constant.LOG_TYPE_SYSTEM)
    public ResultObj deleteAdminLogByIds(@RequestBody ArrayList<Integer> ids) {
        boolean flag = adminLogService.deleteAdminLogByIds(ids);
        if (flag) {
            return new ResultObj(Constant.OK, Constant.DELETE_SUCCESS, null);
        } else {
            return new ResultObj(Constant.SERVER_ERROR_CODE, Constant.DELETE_ERROR, null);
        }
    }

}
