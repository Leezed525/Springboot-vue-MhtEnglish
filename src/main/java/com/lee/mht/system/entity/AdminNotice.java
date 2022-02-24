package com.lee.mht.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author FucXing
 * @date 2022/02/22 21:18
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = "handler")
public class AdminNotice {
    private Integer id;
    private String title;
    private String content;

    //当前公告是否已被发布
    private Boolean available;
    private String type;

    private Integer authorId;

    private AdminUser author;


    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss", timezone = "GMT+8")
    private Date createtime;

    //发布时间
    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss", timezone = "GMT+8")
    private Date publishTime;

    //更新时间
    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
