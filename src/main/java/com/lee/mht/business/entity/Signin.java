package com.lee.mht.business.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

/**
 * @author FucXing
 * @date 2022/02/12 12:42
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Signin {

    private Integer uId;

    private Date signInDate;

    private Integer days;
}
