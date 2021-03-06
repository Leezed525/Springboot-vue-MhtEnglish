package com.lee.mht.business.service;

import com.github.pagehelper.PageInfo;
import com.lee.mht.business.entity.Word;
import com.lee.mht.business.vo.WordCountVo;
import com.lee.mht.business.vo.WordOptionsVo;

import java.util.List;

/**
 * @author FucXing
 * @date 2022/02/01 15:06
 **/
public interface WordService {
    List<Word> RandomSelectWordByNumber(int userId, int number);

    List<WordOptionsVo> getWordOptions(int wordId);

    void learnComplete(List<Word> words, int userId);

    int getCompleteWordCount(int userId);

    PageInfo<Word> getAllCompleteWord(String word, Integer pageSize, Integer pageNum, int userId);

    void forgetWord(Word word, int userId);

    List<Word> RandomSelectReviewWordByNumber(int userId, int number);

    void reviewComplete(int userId, int reviewCount);

    List<WordCountVo> getRecentWeekCompleteWordCount(int userId);

    int getSumWeekCompleteWordCount(int userId);

    List<WordCountVo> getRecentWeekReviewWordCount(int userId);

    int getSumWeekReviewCount(int userId);

    int getTodayCompleteWordCount(int userId);

    int getAllLearnTime(int userId);
}
