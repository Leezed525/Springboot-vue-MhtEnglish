package com.lee.mht.business.dao;

import com.lee.mht.business.entity.Word;
import com.lee.mht.business.vo.WordOptionsVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WordDao {
    List<Word> RandomSelectWordByNumber(@Param("userId") int userId,
                                        @Param("number") int number);

    WordOptionsVo getRightOption(@Param("id") int wordId);

    List<WordOptionsVo> getErrorOption(@Param("id") int wordId);
}
