package com.lee.mht.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author FucXing
 * @date 2021/12/28 17:33
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminRole  implements Serializable {
    private Integer id;
    private String roleName;
    private String comment;
    private Boolean available;

    @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss a")
    private Date createtime;
}
