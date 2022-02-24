package com.lee.mht.system.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lee.mht.system.dao.AdminNoticeDao;
import com.lee.mht.system.entity.AdminLog;
import com.lee.mht.system.entity.AdminNotice;
import com.lee.mht.system.service.AdminNoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author FucXing
 * @date 2022/02/22 21:21
 **/
@Service
@Slf4j
public class AdminNoticeServiceImpl implements AdminNoticeService {
    @Autowired(required = false)
    private AdminNoticeDao adminNoticeDao;

    @Override
    public PageInfo<AdminNotice> getAllNotice(String authorUserName, String title, String type, Date publishBeginTime, Date publishEndTime, Date updateBeginTime, Date updateEndTime, Integer pageSize, Integer pageNum) {
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<AdminNotice> adminNotices = adminNoticeDao.getAllNotice(authorUserName, title, type,publishBeginTime, publishEndTime,updateBeginTime,updateEndTime);
            return new PageInfo<>(adminNotices);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void updateNotice(AdminNotice notice) {
        //更新信息
        adminNoticeDao.updateNotice(notice);
    }

    @Override
    public void addNotice(AdminNotice notice) {
        adminNoticeDao.addNotice(notice);
    }
}
