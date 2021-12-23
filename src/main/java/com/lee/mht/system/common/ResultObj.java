package com.lee.mht.system.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author FucXing
 * @date 2021/12/22 20:05
 **/


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultObj {

    private Integer code;
    private String msg;
    private Object data;
}
