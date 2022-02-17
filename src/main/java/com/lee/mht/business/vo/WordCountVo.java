package com.lee.mht.business.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

/**
 * @author FucXing
 * @date 2022/02/17 13:44
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WordCountVo {
    private Integer count;
    private Date date;
}
