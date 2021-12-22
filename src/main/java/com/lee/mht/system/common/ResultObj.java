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

    public static final ResultObj LOGIN_SUCCESS = new ResultObj(Constants.OK,"登陆成功");
    public static final ResultObj LOGIN_ERROR = new ResultObj(Constants.ERROR,"登陆失败");
    public static final ResultObj LOGIN_FAIL = new ResultObj(Constants.ERROR,"用户名或密码错误");
}
