package com.lee.mht.business.dao;

import com.lee.mht.system.vo.NoticeVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserNoticeDao {
    List<NoticeVo> getUserNoticeVoListById(@Param("uId") int userId);

    void confirmUserNotice(@Param("nId") Integer noticeId,
                           @Param("uId") int userId);
}
