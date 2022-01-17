package com.lee.mht.business.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author FucXing
 * @date 2022/01/13 16:20
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Word {
    private Integer id;

    private String word;

    //音标
    private String symbols;

    //词性
    private String part;

    //单词释义
    private String mean;

    //例句
    private String ex;

    //例句翻译
    private String tran;

    @JsonFormat(pattern="yyyy-MM-dd kk:mm:ss")
    private Date createtime;
}
