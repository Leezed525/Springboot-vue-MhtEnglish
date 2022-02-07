package com.lee.mht.business.service.impl;

import com.lee.mht.business.dao.WordDao;
import com.lee.mht.business.entity.Word;
import com.lee.mht.business.service.WordService;
import com.lee.mht.business.vo.WordOptionsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author FucXing
 * @date 2022/02/01 15:07
 **/
@Service
public class WordServiceImpl implements WordService {

    @Autowired(required = false)
    private WordDao wordDao;

    @Override
    public List<Word> RandomSelectWordByNumber(int userId, int number) {
        return wordDao.RandomSelectWordByNumber(userId, number);
    }

    @Override
    public List<WordOptionsVo> getWordOptions(int wordId) {
        WordOptionsVo rightOption = wordDao.getRightOption(wordId);
        List<WordOptionsVo> list = wordDao.getErrorOption(wordId);
        list.add(rightOption);
        return list;
    }

    @Override
    public void learnComplete(List<Word> words, int userId) {
        wordDao.learnComplete(words, userId);
    }


}
