package com.lee.mht.system.common;

import com.lee.mht.system.common.ResultObj;

/**
 * @author FucXing
 * @date 2021/12/22 20:07
 **/
public class Constant {
    public static final int OK = 200;
    public static final int ERROR = -1;

    public static final String JWT_USER_NAME = "jwt-user-name-key";
    public static final String HEADER_TOKEN_KEY = "AccessToken";


    //业务常量
    public static final String LOGIN_SUCCESS = "登陆成功";
    public static final String USERNAME_PASSWORD_ERROR = "用户名或密码错误";
    public static final String ADMINUSER_NOT_AVAILABLE = "用户不可用";

    public static final String ADD_SUCCESS = "增加成功";
    public static final String ADD_ERROR = "增加失败";

    public static final String QUERY_SUCCESS = "查询成功";
    public static final String QUERY_ERROR = "查询失败";

    public static final String UPDATE_SUCCESS = "修改成功";
    public static final String UPDATE_ERROR = "修改失败";

    public static final String DELETE_SUCCESS = "删除成功";
    public static final String DELETE_ERROR = "删除失败";

    public static final String RESET_SUCCESS = "重置成功";
    public static final String RESET_ERROR = "重置失败";

}
