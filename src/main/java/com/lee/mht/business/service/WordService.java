package com.lee.mht.business.service;

import com.lee.mht.business.entity.Word;
import com.lee.mht.business.vo.WordOptionsVo;

import java.util.List;

/**
 * @author FucXing
 * @date 2022/02/01 15:06
 **/
public interface WordService {
    List<Word> RandomSelectWordByNumber(int userId, int number);

    List<WordOptionsVo> getWordOptions(int wordId);
}
