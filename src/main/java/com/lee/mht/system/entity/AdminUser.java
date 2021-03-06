package com.lee.mht.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author FucXing
 * @date 2021/12/22 16:01
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUser implements Serializable {
    private Integer id;
    private String username;
    private String nickname;
    private String remark;
    private String password;
    private Integer roleId;
    private Boolean available;

    @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss a")
    private Date createtime;

    private String salt;
}
