package com.lee.mht.system.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lee.mht.system.dao.AdminLogDao;
import com.lee.mht.system.entity.AdminLog;
import com.lee.mht.system.entity.AdminPermission;
import com.lee.mht.system.service.AdminLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author FucXing
 * @date 2022/01/10 22:33
 **/
@Service
public class AdminLogServiceImpl implements AdminLogService {

    @Autowired(required = false)
    private AdminLogDao adminLogDao;

    @Override
    public PageInfo<AdminLog> getAllLog(String operator, String action, String result, Date beginTime, Date endTime, int pageSize, int pageNum) {
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<AdminLog> adminPermissions = adminLogDao.getAllLog(operator, action, result,beginTime,endTime);
            return new PageInfo<>(adminPermissions);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean deleteAdminLogByIds(ArrayList<Integer> ids) {
        return adminLogDao.deleteAdminLogByIds(ids);
    }
}
