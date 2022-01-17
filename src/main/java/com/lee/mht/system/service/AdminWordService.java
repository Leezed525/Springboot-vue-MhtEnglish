package com.lee.mht.system.service;

import com.github.pagehelper.PageInfo;
import com.lee.mht.business.entity.Word;

import java.util.ArrayList;
import java.util.List;

/**
 * @author FucXing
 * @date 2022/01/15 12:35
 **/
public interface AdminWordService {
    PageInfo<Word> getAllAdminWord(String word, String parts, String mean, Integer pageSize, Integer pageNum);

    boolean updateAdminWord(Word word);

    boolean addAdminWord(Word word);

    boolean deleteAdminWordByIds(ArrayList<Integer> ids);
}
