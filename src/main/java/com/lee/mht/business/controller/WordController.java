package com.lee.mht.business.controller;

import com.lee.mht.business.entity.Word;
import com.lee.mht.business.service.WordService;
import com.lee.mht.business.vo.WordOptionsVo;
import com.lee.mht.system.common.Constant;
import com.lee.mht.system.common.ResultObj;
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

    /**
     *
     * @param number 请求数
     * @param request request
     * @return List<Word>
     */
    @RequestMapping("/getWordsByNumber")
    public ResultObj getWordsByNumber(@RequestParam(name = "number", required = false, defaultValue = "10") int number,
                                      HttpServletRequest request) {
        if(number > 50 || number <= 0){
            return new ResultObj(Constant.SERVER_ERROR,Constant.QUERY_ERROR);
        }
        try {
            String token = request.getHeader(Constant.HEADER_TOKEN_KEY);
            int userId = Integer.parseInt(JwtUtils.getId(token));
            List<Word> words = wordService.RandomSelectWordByNumber(userId, number);
            return new ResultObj(Constant.OK,Constant.QUERY_SUCCESS,words);
        }catch (Exception e) {
            return new ResultObj(Constant.SERVER_ERROR,Constant.QUERY_ERROR);
        }

    }

    /**
     *
     *
     * @param wordId 单词id
     * @return WordOptionVo
     */
    @RequestMapping("/getWordOptions")
    public ResultObj getWordOptions(@RequestParam(name = "wordId")int wordId){
        try {
            List<WordOptionsVo> options = wordService.getWordOptions(wordId);
            log.info(options.toString());
            return new ResultObj(Constant.OK,Constant.QUERY_SUCCESS,options);
        }catch (Exception e) {
            return new ResultObj(Constant.SERVER_ERROR,Constant.QUERY_ERROR);
        }
    }


    @RequestMapping("/learnComplete")
    public ResultObj learnComplete(@RequestBody List<Word> words,HttpServletRequest request){
        if(words.size() == 0){
            return new ResultObj(Constant.SERVER_ERROR, Constant.ADD_ERROR);
        }
        try {
            String token = request.getHeader(Constant.HEADER_TOKEN_KEY);
            int userId = Integer.parseInt(JwtUtils.getId(token));
            wordService.learnComplete(words, userId);
            return new ResultObj(Constant.OK,Constant.ADD_SUCCESS);
        }catch(Exception e) {
            return new ResultObj(Constant.SERVER_ERROR, Constant.ADD_ERROR);
        }
    }
}
