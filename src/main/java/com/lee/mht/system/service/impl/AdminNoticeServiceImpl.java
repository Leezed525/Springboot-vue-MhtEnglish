package com.lee.mht.system.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lee.mht.system.common.Constant;
import com.lee.mht.system.dao.AdminNoticeDao;
import com.lee.mht.system.entity.AdminLog;
import com.lee.mht.system.entity.AdminNotice;
import com.lee.mht.system.service.AdminNoticeService;
import com.lee.mht.system.socketController.AdminNoticeSocket;
import com.lee.mht.system.utils.JacksonUtils;
import com.lee.mht.system.vo.NoticeVo;
import com.lee.mht.system.vo.SocketNoticeVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    @Override
    public boolean deleteAdminNoticeByIds(ArrayList<Integer> ids) {
        return adminNoticeDao.deleteAdminNoticeByIds(ids);
    }

    @Override
    public void publishNotice(AdminNotice notice) {
        notice.setPublishTime(new Date());
        //数据库更新状态
        adminNoticeDao.publishNotice(notice);
        //推送给所有在线的用户
        SocketNoticeVo msg = new SocketNoticeVo("publish", notice);
        String noticeType = notice.getType();
        //如果是类型是系统用户公告
        if(Constant.NOTICE_TYPE_ALL.equals(noticeType) || Constant.NOTICE_TYPE_ADMIN_USER.equals(noticeType)){
            AdminNoticeSocket.sendMessageToAll(JacksonUtils.toJson(msg));
        }
    }

    @Override
    @Transactional
    public void cancelNotice(AdminNotice notice) {
        //数据库更新状态
        adminNoticeDao.cancelNotice(notice);
        //删除所有该公告的阅读状态
        adminNoticeDao.deleteRelationToNotice(notice);
        //推送给所有在线的用户
        SocketNoticeVo msg = new SocketNoticeVo("cancel", notice);
        String noticeType = notice.getType();
        //如果是类型是系统用户公告
        if(Constant.NOTICE_TYPE_ALL.equals(noticeType) || Constant.NOTICE_TYPE_ADMIN_USER.equals(noticeType)){
            AdminNoticeSocket.sendMessageToAll(JacksonUtils.toJson(msg));
        }
    }

    @Override
    public List<NoticeVo> getAdminNoticeById(int userId) {
        return adminNoticeDao.getAdminNoticeById(userId);
    }

    @Override
    public void confirmAdminNotice(Integer noticeId, int userId) {
        adminNoticeDao.confirmAdminNotice(noticeId, userId);
    }


}
