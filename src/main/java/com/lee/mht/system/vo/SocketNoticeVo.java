package com.lee.mht.system.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lee.mht.system.entity.AdminNotice;
import com.lee.mht.system.entity.AdminUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author FucXing
 * @date 2022/02/25 16:55
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocketNoticeVo {
    private String operation;

    private Integer id;
    private String title;
    private String content;
    private Integer authorId;

    private AdminUser author;

    //发布时间
    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss", timezone = "GMT+8")
    private Date publishTime;


    public SocketNoticeVo(String operation,AdminNotice notice) {
        this.operation = operation;
        this.author = notice.getAuthor();
        this.authorId = notice.getAuthorId();
        this.id = notice.getId();
        this.title = notice.getTitle();
        this.content = notice.getContent();
        this.publishTime = notice.getPublishTime();
    }
}
