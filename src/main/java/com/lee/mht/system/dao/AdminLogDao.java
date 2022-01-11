package com.lee.mht.system.dao;

import com.lee.mht.system.entity.AdminLog;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author FucXing
 * @date 2022/01/10 22:43
 **/
public interface AdminLogDao {
    //批量保存日志
    boolean batchSaveLogs(@Param("logs")List<AdminLog> adminLogs);

    List<AdminLog> getAllLog(@Param("operator")String operator,
                             @Param("action") String action,
                             @Param("result") String result,
                             @Param("beginTime") Date beginTime,
                             @Param("endTime") Date endTime);

    boolean deleteAdminLogByIds(@Param("ids") ArrayList<Integer> ids);
}
