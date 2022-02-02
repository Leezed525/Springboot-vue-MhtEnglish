package com.lee.mht.business.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author FucXing
 * @date 2022/02/01 20:18
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer id;

    private String openId;

    private String nickname;

    private String avatarUrl;

    private Integer gender;

    private String country;

    private String province;

    private String city;

    private Date createtime;
}
