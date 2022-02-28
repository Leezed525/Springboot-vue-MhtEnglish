package com.lee.mht.system.dao;

import com.lee.mht.business.entity.Word;
import com.lee.mht.business.vo.WordCountVo;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

/**
 * @author FucXing
 * @date 2022/01/15 12:35
 **/
public interface AdminWordDao {
    List<Word> getAllAdminUser(@Param("word")String word,@Param("part") String part,@Param("mean") String mean);

    boolean updateAdminWord(@Param("word")Word word);

    boolean addAdminWord(@Param("word")Word word);

    boolean deleteAdminWordByIds(@Param("ids")ArrayList<Integer> ids);

    int getWordCount();

    List<WordCountVo> getAllRecentWeekWordsLearnCount();
}
