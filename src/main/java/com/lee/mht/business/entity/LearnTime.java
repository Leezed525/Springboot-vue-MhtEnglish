package com.lee.mht.business.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

/**
 * @author FucXing
 * @date 2022/02/17 19:14
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LearnTime {
    private Integer uId;
    private Date date;
    private Integer time;
}
