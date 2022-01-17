package com.lee.mht.system.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lee.mht.business.entity.Word;
import com.lee.mht.system.dao.AdminWordDao;
import com.lee.mht.system.service.AdminWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author FucXing
 * @date 2022/01/15 12:35
 **/
@Service
public class AdminWordServiceImpl implements AdminWordService {

    @Autowired(required = false)
    private AdminWordDao adminWordDao;

    @Override
    public PageInfo<Word> getAllAdminWord(String word, String part, String mean, Integer pageSize, Integer pageNum) {
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<Word> adminWords = adminWordDao.getAllAdminUser(word, part, mean);
            return new PageInfo<Word>(adminWords);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean updateAdminWord(Word word) {
        try {
            return adminWordDao.updateAdminWord(word);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean addAdminWord(Word word) {
        try {
            return adminWordDao.addAdminWord(word);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional
    public boolean deleteAdminWordByIds(ArrayList<Integer> ids) {
        try {
            return adminWordDao.deleteAdminWordByIds(ids);
        } catch (Exception e) {
            return false;
        }
    }
}
