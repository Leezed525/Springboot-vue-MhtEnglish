package com.lee.mht.business.controller;

import com.github.pagehelper.PageInfo;
import com.lee.mht.business.entity.Word;
import com.lee.mht.business.service.WordService;
import com.lee.mht.business.vo.WordCountVo;
import com.lee.mht.business.vo.WordOptionsVo;
import com.lee.mht.system.common.Constant;
import com.lee.mht.system.common.ResultObj;
import com.lee.mht.system.service.RedisService;
import com.lee.mht.system.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author FucXing
 * @date 2022/02/05 20:44
 **/
@RequestMapping("/word")
@RestController
@Slf4j
public class WordController {

    @Autowired
    private WordService wordService;

    @Autowired
    private RedisService redisService;

    /**
     * 获取学习的单词
     *
     * @param number  请求数
     * @param request request
     * @return List<Word>
     */
    @RequestMapping("/getWordsByNumber")
    public ResultObj getWordsByNumber(@RequestParam(name = "number", required = false, defaultValue = "10") int number,
                                      HttpServletRequest request) {
        if (number > 50 || number <= 0) {
            return new ResultObj(Constant.SERVER_ERROR_CODE, Constant.QUERY_ERROR);
        }
        try {
            int userId = JwtUtils.getId(request);
            List<Word> words = wordService.RandomSelectWordByNumber(userId, number);
            return new ResultObj(Constant.OK, Constant.QUERY_SUCCESS, words);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResultObj(Constant.SERVER_ERROR_CODE, Constant.QUERY_ERROR);
        }
    }

    /**
     * 获取单词选项
     *
     * @param wordId 单词id
     * @return WordOptionVo
     */
    @RequestMapping("/getWordOptions")
    public ResultObj getWordOptions(@RequestParam(name = "wordId") int wordId) {
        try {
            List<WordOptionsVo> options = wordService.getWordOptions(wordId);
            log.info(options.toString());
            return new ResultObj(Constant.OK, Constant.QUERY_SUCCESS, options);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResultObj(Constant.SERVER_ERROR_CODE, Constant.QUERY_ERROR);
        }
    }

    /**
     * @param words   完成单词列表
     * @param request request
     * @return ResultObj
     */
    @RequestMapping("/learnComplete")
    public ResultObj learnComplete(@RequestBody List<Word> words, HttpServletRequest request) {
        if (words.size() == 0) {
            return new ResultObj(Constant.SERVER_ERROR_CODE, Constant.ADD_ERROR);
        }
        try {
            int userId = JwtUtils.getId(request);
            wordService.learnComplete(words, userId);
            return new ResultObj(Constant.OK, Constant.LEARN_COMPLETE);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResultObj(Constant.SERVER_ERROR_CODE, Constant.LEARN_FAIL);
        }
    }

    /**
     * 获取用户学习完成的单词数量
     *
     * @param request request
     * @return
     */
    @RequestMapping("/getCompleteWordCount")
    public ResultObj getCompleteWordCount(HttpServletRequest request) {
        try {
            int userId = JwtUtils.getId(request);
            int count = wordService.getCompleteWordCount(userId);
            return new ResultObj(Constant.OK, Constant.QUERY_SUCCESS, count);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResultObj(Constant.SERVER_ERROR_CODE, Constant.QUERY_ERROR);
        }
    }

    /**
     * 获取所有学习完成的单词
     *
     * @param word     模糊查询单词
     * @param pageSize 查询数量
     * @param pageNum  查询页数
     * @param request  request
     * @return List<word>
     */
    @RequestMapping("/getAllCompleteWord")
    public ResultObj getAllCompleteWord(@RequestParam(required = false, defaultValue = "", name = "word") String word,
                                        @RequestParam(required = false, defaultValue = "10", name = "limit") Integer pageSize,
                                        @RequestParam(required = false, defaultValue = "1", name = "page") Integer pageNum,
                                        HttpServletRequest request) {
        try {
            int userId = JwtUtils.getId(request);
            PageInfo<Word> pageInfo = wordService.getAllCompleteWord(word, pageSize, pageNum, userId);
            if (pageInfo != null) {
                return new ResultObj(Constant.OK, Constant.QUERY_SUCCESS, pageInfo);
            } else {
                return new ResultObj(Constant.SERVER_ERROR_CODE, Constant.QUERY_ERROR);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResultObj(Constant.SERVER_ERROR_CODE, Constant.QUERY_ERROR);
        }
    }

    /**
     * 遗忘单词
     *
     * @param word    word实体类
     * @param request request
     * @return 无关紧要
     */
    @RequestMapping("/forgetWord")
    public ResultObj forgetWord(@RequestBody Word word, HttpServletRequest request) {
        try {
            int userId = JwtUtils.getId(request);
            wordService.forgetWord(word, userId);
            return new ResultObj(Constant.OK, Constant.DELETE_SUCCESS);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResultObj(Constant.SERVER_ERROR_CODE, Constant.DELETE_ERROR);
        }
    }

    /**
     * 获取复习单词
     *
     * @param number  查询数量默认10
     * @param request reqyest
     * @return List<word>
     */
    @RequestMapping("/getReviewWordsByNumber")
    public ResultObj getReviewWordsByNumber(@RequestParam(name = "number", required = false, defaultValue = "10") int number,
                                            HttpServletRequest request) {
        if (number > 50 || number <= 0) {
            return new ResultObj(Constant.SERVER_ERROR_CODE, Constant.QUERY_ERROR);
        }
        try {
            int userId = JwtUtils.getId(request);
            List<Word> words = wordService.RandomSelectReviewWordByNumber(userId, number);
            return new ResultObj(Constant.OK, Constant.QUERY_SUCCESS, words);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResultObj(Constant.SERVER_ERROR_CODE, Constant.QUERY_ERROR);
        }
    }

    /**
     * @param reviewCount 复习个数
     * @param request     request
     * @return resultObj
     */
    @RequestMapping("/reviewComplete")
    public ResultObj reviewComplete(@RequestParam(name = "reviewCount") int reviewCount,
                                    HttpServletRequest request) {
        try {
            int userId = JwtUtils.getId(request);
            wordService.reviewComplete(userId, reviewCount);
            return new ResultObj(Constant.OK, Constant.LEARN_COMPLETE);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResultObj(Constant.SERVER_ERROR_CODE, Constant.LEARN_FAIL);
        }
    }

    /**
     * 获取最近7天学习完成单词数
     *
     * @param request request
     * @return List<WordCountVo>
     */
    @RequestMapping("/getRecentWeekCompleteWordCount")
    public ResultObj getRecentWeekCompleteWordCount(HttpServletRequest request) {
        try {
            int userId = JwtUtils.getId(request);
            List<WordCountVo> list = wordService.getRecentWeekCompleteWordCount(userId);
            return new ResultObj(Constant.OK, Constant.QUERY_SUCCESS, list);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResultObj(Constant.SERVER_ERROR_CODE, Constant.QUERY_ERROR);
        }
    }

    /**
     * 获取最近7天单词学习总数
     *
     * @param request request
     * @return sum
     */
    @RequestMapping("/getSumWeekCompleteWordCount")
    public ResultObj getSumWeekCompleteWordCount(HttpServletRequest request) {
        try {
            int userId = JwtUtils.getId(request);
            int sum = wordService.getSumWeekCompleteWordCount(userId);
            return new ResultObj(Constant.OK, Constant.QUERY_SUCCESS, sum);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResultObj(Constant.SERVER_ERROR_CODE, Constant.QUERY_ERROR);
        }
    }

    /**
     * 获取最近七天复习单词数
     *
     * @param request request
     * @return List<WordCountVo>
     */
    @RequestMapping("/getRecentWeekReviewWordCount")
    public ResultObj getRecentWeekReviewWordCount(HttpServletRequest request) {
        try {
            int userId = JwtUtils.getId(request);
            List<WordCountVo> list = wordService.getRecentWeekReviewWordCount(userId);
            return new ResultObj(Constant.OK, Constant.QUERY_SUCCESS, list);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResultObj(Constant.SERVER_ERROR_CODE, Constant.QUERY_ERROR);
        }
    }

    /**
     * 获取最近七天复习单词总数
     *
     * @param request request
     * @return sum
     */
    @RequestMapping("/getSumWeekReviewCount")
    public ResultObj getSumWeekReviewCount(HttpServletRequest request) {
        try {
            int userId = JwtUtils.getId(request);
            int sum = wordService.getSumWeekReviewCount(userId);
            return new ResultObj(Constant.OK, Constant.QUERY_SUCCESS, sum);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResultObj(Constant.SERVER_ERROR_CODE, Constant.QUERY_ERROR);
        }
    }

    /**
     * 获取今天学习的单词数
     *
     * @param request request
     * @return sum
     */
    @RequestMapping("/getTodayCompleteWordCount")
    public ResultObj getTodayCompleteWordCount(HttpServletRequest request) {
        try {
            int userId = JwtUtils.getId(request);
            int count = wordService.getTodayCompleteWordCount(userId);
            return new ResultObj(Constant.OK, Constant.QUERY_SUCCESS, count);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResultObj(Constant.SERVER_ERROR_CODE, Constant.QUERY_ERROR);
        }
    }

    @RequestMapping("/setLearnTimeToday")
    public ResultObj setLearnTimeToday(@RequestParam("time") int time,
                                       HttpServletRequest request) {
        try {
            int userId = JwtUtils.getId(request);
            redisService.setLearnTimeToday(time, userId);
            return new ResultObj(Constant.OK, Constant.ADD_SUCCESS);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResultObj(Constant.SERVER_ERROR_CODE, Constant.ADD_ERROR);
        }

    }

    @RequestMapping("/getLearnTimeToday")
    public ResultObj getLearnTimeToday(HttpServletRequest request) {

        try {
            int userId = JwtUtils.getId(request);
            int time = redisService.getLearnTimeToday(userId);
            return new ResultObj(Constant.OK, Constant.QUERY_SUCCESS, time);
        }catch (Exception e) {
            log.error(e.getMessage());
            return new ResultObj(Constant.SERVER_ERROR_CODE, Constant.QUERY_ERROR);
        }
    }

    @RequestMapping("/getAllLearnTime")
    public ResultObj getAllLearnTime(HttpServletRequest request) {
        try {
            int userId = JwtUtils.getId(request);
            int dayTime = redisService.getLearnTimeToday(userId);
            int allTime = wordService.getAllLearnTime(userId);
            return new ResultObj(Constant.OK, Constant.QUERY_SUCCESS,dayTime + allTime);
        }catch (Exception e) {
            log.error(e.getMessage());
            return new ResultObj(Constant.SERVER_ERROR_CODE, Constant.QUERY_ERROR);
        }
    }
}
