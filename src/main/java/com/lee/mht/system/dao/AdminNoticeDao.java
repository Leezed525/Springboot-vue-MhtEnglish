package com.lee.mht.system.dao;

import com.lee.mht.system.entity.AdminNotice;
import com.lee.mht.system.vo.NoticeVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author FucXing
 * @date 2022/02/22 21:21
 **/
public interface AdminNoticeDao {
    List<AdminNotice> getAllNotice(@Param("username") String authorUserName,
                                   @Param("title") String title,
                                   @Param("type") String type,
                                   @Param("publishBeginTime") Date publishBeginTime,
                                   @Param("publishEndTime") Date publishEndTime,
                                   @Param("updateBeginTime") Date updateBeginTime,
                                   @Param("updateEndTime") Date updateEndTime);

    void updateNotice(@Param("notice") AdminNotice notice);

    void addNotice(@Param("notice") AdminNotice notice);

    /**
     * 删除公告
     * @param notice 公告类
     * @return bool
     */
    boolean deleteAdminNotice(@Param("notice") AdminNotice notice);

    void publishNotice(@Param("notice") AdminNotice notice);

    void cancelNotice(@Param("notice") AdminNotice notice);

    List<NoticeVo> getAdminNoticeById(@Param("uId") int userId);

    void deleteRelationToNotice(@Param("notice")AdminNotice notice);

    void confirmAdminNotice(@Param("nId")Integer noticeId, @Param("uId")int userId);
}
