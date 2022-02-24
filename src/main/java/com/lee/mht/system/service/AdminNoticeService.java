package com.lee.mht.system.service;

import com.github.pagehelper.PageInfo;
import com.lee.mht.system.entity.AdminLog;
import com.lee.mht.system.entity.AdminNotice;

import java.util.Date;

/**
 * @author FucXing
 * @date 2022/02/22 21:21
 **/
public interface AdminNoticeService {
    PageInfo<AdminNotice> getAllNotice(String authorUserName, String title, String type, Date publishBeginTime, Date publishEndTime, Date updateBeginTime, Date updateEndTime, Integer pageSize, Integer pageNum);

    void updateNotice(AdminNotice notice);

    void addNotice(AdminNotice notice);
}
