package com.lee.mht.business.dao;

import com.lee.mht.business.entity.LearnTime;
import com.lee.mht.business.entity.Word;
import com.lee.mht.business.vo.WordCountVo;
import com.lee.mht.business.vo.WordOptionsVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WordDao {
    List<Word> RandomSelectWordByNumber(@Param("uId") int userId,
                                        @Param("number") int number);

    WordOptionsVo getRightOption(@Param("id") int wordId);

    List<WordOptionsVo> getErrorOption(@Param("id") int wordId);

    void learnComplete(@Param("words") List<Word> words,
                       @Param("uId") int userId);

    int getCompleteWordCount(@Param("uId") int userId);

    List<Word> getAllCompleteWord(@Param("word") String word,
                                  @Param("uId") int userId);

    void deleteCompleteWordByUserIdAndWordId(@Param("wId") int wordId,
                                             @Param("uId") int userId);

    List<Word> RandomSelectReviewWordByNumber(@Param("uId") int userId,
                                              @Param("number") int number);

    int getTodayReviewCount(@Param("uId") int userId);

    void createTodayReviewWord(@Param("uId") int userId,
                               @Param("count") int reviewCount);

    void updateTodayReviewWord(@Param("uId") int userId, @Param("count") int reviewCount);

    List<WordCountVo> getRecentWeekCompleteWordCount(@Param("uId") int userId);

    int getSumWeekCompleteWordCount(@Param("uId") int userId);

    List<WordCountVo> getRecentWeekReviewWordCount(@Param("uId") int userId);

    int getSumWeekReviewCount(@Param("uId") int userId);

    int getTodayCompleteWordCount(@Param("uId") int userId);

    int getAllLearnTime(@Param("uId") int userId);

    void saveLearnTime(@Param("learnTimes")List<LearnTime> learnTimeList);
}
