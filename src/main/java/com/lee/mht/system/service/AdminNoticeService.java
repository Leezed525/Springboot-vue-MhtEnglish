package com.lee.mht.system.service;

import com.github.pagehelper.PageInfo;
import com.lee.mht.system.entity.AdminNotice;
import com.lee.mht.system.vo.NoticeVo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author FucXing
 * @date 2022/02/22 21:21
 **/
public interface AdminNoticeService {
    PageInfo<AdminNotice> getAllNotice(String authorUserName, String title, String type, Date publishBeginTime, Date publishEndTime, Date updateBeginTime, Date updateEndTime, Integer pageSize, Integer pageNum);

    void updateNotice(AdminNotice notice);

    void addNotice(AdminNotice notice);

    boolean deleteAdminNoticeByIds(ArrayList<Integer> ids);

    void publishNotice(AdminNotice notice);

    void cancelNotice(AdminNotice notice);

    List<NoticeVo> getAdminNoticeById(int userId);

    void confirmAdminNotice(Integer noticeId, int userId);
}
