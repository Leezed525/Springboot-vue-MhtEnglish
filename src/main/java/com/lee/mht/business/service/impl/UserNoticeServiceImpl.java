package com.lee.mht.business.service.impl;

import com.lee.mht.business.dao.UserNoticeDao;
import com.lee.mht.business.service.UserNoticeService;
import com.lee.mht.system.vo.NoticeVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author FucXing
 * @date 2022/02/27 14:17
 **/
@Service
@Slf4j
public class UserNoticeServiceImpl implements UserNoticeService {

    @Autowired(required = false)
    private UserNoticeDao userNoticeDao;

    @Override
    public List<NoticeVo> getUserNoticeVoListById(int userId) {
        return userNoticeDao.getUserNoticeVoListById(userId);
    }

    @Override
    public void confirmUserNotice(Integer noticeId, int userId) {
        userNoticeDao.confirmUserNotice(noticeId, userId);
    }
}
