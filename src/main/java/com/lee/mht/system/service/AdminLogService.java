package com.lee.mht.system.service;

import com.github.pagehelper.PageInfo;
import com.lee.mht.system.entity.AdminLog;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author FucXing
 * @date 2022/01/10 22:33
 **/
public interface AdminLogService {
    PageInfo<AdminLog> getAllLog(String operator, String action, String result, Date beginTime, Date endTime, int pageSize, int pageNum);

    boolean deleteAdminLogByIds(ArrayList<Integer> ids);
}
