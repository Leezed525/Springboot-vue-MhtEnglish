package com.lee.mht.system.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author FucXing
 * @date 2021/12/28 23:46
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminPermission implements Serializable {
    private Integer id;
    private Integer pid;
    private String title;
    private String reqUrl;
    private String percode;
    private Boolean available;
    private String type;
}
