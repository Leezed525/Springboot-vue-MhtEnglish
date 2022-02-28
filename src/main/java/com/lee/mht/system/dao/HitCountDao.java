package com.lee.mht.system.dao;

import com.lee.mht.system.entity.HitCount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HitCountDao {
    List<HitCount> getRecentWeekHitCount();

    void saveHitCount(@Param("hitCounts") List<HitCount> hitCounts);
}
