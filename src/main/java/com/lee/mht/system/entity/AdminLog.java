package com.lee.mht.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author FucXing
 * @date 2022/01/10 11:12
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminLog {
    private Integer id;

    private String type;

    private String operator;

    private Integer operatorId;

    private String ip;

    private String action;

    private String method;

    private String data;

    private String result;

    @JsonFormat(pattern="yyyy-MM-dd kk:mm:ss",timezone = "GMT+8")
    private Date createtime;

    //耗时
    private String timeConsuming;
}
