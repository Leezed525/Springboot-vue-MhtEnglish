package com.lee.mht.system.controller;

import com.github.pagehelper.PageInfo;
import com.lee.mht.business.entity.Word;
import com.lee.mht.system.annotation.MhtLog;
import com.lee.mht.system.common.Constant;
import com.lee.mht.system.common.ResultObj;
import com.lee.mht.system.entity.AdminUser;
import com.lee.mht.system.service.AdminWordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author FucXing
 * @date 2022/01/15 12:31
 **/
@Slf4j
@RestController
@RequestMapping("/admin/word")
public class AdminWordController {


    @Autowired
    private AdminWordService adminWordService;

    //通过查询条件获取所有单词
    @RequiresPermissions("word:query")
    @RequestMapping("/getAllAdminWord")
    @MhtLog(action="单词查询",type = Constant.LOG_TYPE_SYSTEM)
    public ResultObj getAllAdminWord(@RequestParam(required = false, defaultValue = "", name = "word") String word,
                                     @RequestParam(required = false, defaultValue = "", name = "part") String part,
                                     @RequestParam(required = false, defaultValue = "", name = "mean") String mean,
                                     @RequestParam(required = false, defaultValue = "5", name = "limit") Integer pageSize,
                                     @RequestParam(required = false, defaultValue = "1", name = "page") Integer pageNum) {
        PageInfo<Word> pageInfo = adminWordService.getAllAdminWord(word, part, mean, pageSize, pageNum);
        if (pageInfo != null) {
            return new ResultObj(Constant.OK, Constant.QUERY_SUCCESS, pageInfo);
        } else {
            return new ResultObj(Constant.SERVER_ERROR, Constant.QUERY_ERROR);
        }
    }



    //改
    @RequiresPermissions("word:update")
    @RequestMapping("/updateAdminWord")
    @MhtLog(action="单词编辑",type = Constant.LOG_TYPE_SYSTEM)
    public ResultObj updateAdminWord(@RequestBody Word word) {
        if(!verifyWord(word)){
            return new ResultObj(Constant.SERVER_ERROR, Constant.UPDATE_ERROR);
        }
        boolean flag = adminWordService.updateAdminWord(word);
        if (flag) {
            return new ResultObj(Constant.OK, Constant.UPDATE_SUCCESS);
        } else {
            return new ResultObj(Constant.SERVER_ERROR, Constant.UPDATE_ERROR);
        }
    }

    //增
    @RequiresPermissions("word:create")
    @RequestMapping("/addAdminWord")
    @MhtLog(action="单词添加",type = Constant.LOG_TYPE_SYSTEM)
    public ResultObj addAdminWord(@RequestBody Word word) {
        if(!verifyWord(word)){
            return new ResultObj(Constant.SERVER_ERROR, Constant.ADD_ERROR);
        }
        boolean flag = adminWordService.addAdminWord(word);
        if (flag) {
            return new ResultObj(Constant.OK, Constant.ADD_SUCCESS);
        } else {
            return new ResultObj(Constant.SERVER_ERROR, Constant.ADD_ERROR);
        }
    }

    //删
    @RequiresPermissions("word:delete")
    @RequestMapping("/deleteAdminWordByIds")
    @MhtLog(action="单词删除",type = Constant.LOG_TYPE_SYSTEM)
    public ResultObj deleteAdminWordByIds(@RequestBody ArrayList<Integer> ids) {
        boolean flag = adminWordService.deleteAdminWordByIds(ids);
        if (flag) {
            return new ResultObj(Constant.OK, Constant.DELETE_SUCCESS, null);
        } else {
            return new ResultObj(Constant.SERVER_ERROR, Constant.DELETE_ERROR, null);
        }
    }

    private boolean verifyWord(Word word) {
        if(!StringUtils.hasLength(word.getWord())){
            return false;
        }
        if(!StringUtils.hasLength(word.getPart())){
            return false;
        }
        if(!StringUtils.hasLength(word.getSymbols())){
            return false;
        }
        if(!StringUtils.hasLength(word.getMean())){
            return false;
        }
        if(!StringUtils.hasLength(word.getEx())){
            return false;
        }
        if(!StringUtils.hasLength(word.getTran())){
            return false;
        }
        return true;
    }
}
