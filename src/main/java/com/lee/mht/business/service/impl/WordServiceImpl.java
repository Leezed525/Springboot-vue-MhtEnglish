package com.lee.mht.business.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lee.mht.business.dao.WordDao;
import com.lee.mht.business.entity.Word;
import com.lee.mht.business.service.WordService;
import com.lee.mht.business.vo.WordCountVo;
import com.lee.mht.business.vo.WordOptionsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author FucXing
 * @date 2022/02/01 15:07
 **/
@Service
@Slf4j
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

    @Override
    public int getCompleteWordCount(int userId) {
        return wordDao.getCompleteWordCount(userId);
    }

    @Override
    public PageInfo<Word> getAllCompleteWord(String word, Integer pageSize, Integer pageNum, int userId) {
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<Word> adminWords = wordDao.getAllCompleteWord(word,userId);
            return new PageInfo<>(adminWords);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void forgetWord(Word word, int userId) {
        wordDao.deleteCompleteWordByUserIdAndWordId(word.getId(),userId);
    }

    @Override
    public List<Word> RandomSelectReviewWordByNumber(int userId, int number) {
        return wordDao.RandomSelectReviewWordByNumber(userId, number);
    }

    @Override
    public void reviewComplete(int userId, int reviewCount) {
        int lastCount = wordDao.getTodayReviewCount(userId);
        if(lastCount == 0){
            log.info(String.valueOf(userId));
            log.info(String.valueOf(reviewCount));
            wordDao.createTodayReviewWord(userId,reviewCount);
        }else{
            reviewCount += lastCount;
            log.info(String.valueOf(userId));
            log.info(String.valueOf(reviewCount));
            wordDao.updateTodayReviewWord(userId,reviewCount);
        }
    }

    @Override
    public List<WordCountVo> getRecentWeekCompleteWordCount(int userId) {
        return wordDao.getRecentWeekCompleteWordCount(userId);
    }

    @Override
    public int getSumWeekCompleteWordCount(int userId) {
        return wordDao.getSumWeekCompleteWordCount(userId);
    }

    @Override
    public List<WordCountVo> getRecentWeekReviewWordCount(int userId) {
        return wordDao.getRecentWeekReviewWordCount(userId);
    }

    @Override
    public int getSumWeekReviewCount(int userId) {
        return wordDao.getSumWeekReviewCount(userId);
    }


}
