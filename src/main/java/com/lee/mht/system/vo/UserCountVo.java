package com.lee.mht.system.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

/**
 * @author FucXing
 * @date 2022/02/28 21:31
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCountVo {
    private Date date;
    private Integer count;
}
