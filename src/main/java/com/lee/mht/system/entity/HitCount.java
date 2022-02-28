package com.lee.mht.system.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

/**
 * @author FucXing
 * @date 2022/02/28 11:03
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HitCount {
    private Date createDate;
    private Integer count;
}
