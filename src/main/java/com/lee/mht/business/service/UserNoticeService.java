package com.lee.mht.business.service;

import com.lee.mht.system.vo.NoticeVo;

import java.util.List;

public interface UserNoticeService {
    List<NoticeVo> getUserNoticeVoListById(int userId);

    void confirmUserNotice(Integer noticeId, int userId);
}
