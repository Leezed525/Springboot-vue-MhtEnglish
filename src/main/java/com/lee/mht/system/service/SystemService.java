package com.lee.mht.system.service;

import com.lee.mht.system.common.ResultObj;
import com.lee.mht.system.entity.HitCount;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * @author Leezed525
 */
public interface SystemService {
    ResultObj login(String username, String password, HttpServletRequest request);

    int getWordCount();

    List<HitCount> getRecentWeekHitCount();
}
