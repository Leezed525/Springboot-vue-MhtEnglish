package com.lee.mht.system.dao;

import com.lee.mht.system.entity.AdminLog;
import com.lee.mht.system.entity.AdminNotice;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author FucXing
 * @date 2022/02/22 21:21
 **/
public interface AdminNoticeDao {
    List<AdminNotice> getAllNotice(@Param("username")String authorUserName,
                                   @Param("title") String title,
                                   @Param("type") String type,
                                   @Param("publishBeginTime") Date publishBeginTime,
                                   @Param("publishEndTime") Date publishEndTime,
                                   @Param("updateBeginTime") Date updateBeginTime,
                                   @Param("updateEndTime") Date updateEndTime);
}
