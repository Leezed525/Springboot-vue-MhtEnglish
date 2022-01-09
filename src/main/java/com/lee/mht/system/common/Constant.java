package com.lee.mht.system.common;

import com.lee.mht.system.common.ResultObj;
import com.lee.mht.system.exception.SystemException;

/**
 * @author FucXing
 * @date 2021/12/22 20:07
 **/
public class Constant {
    public static final int OK = 200;
    public static final int SERVER_ERROR = -1;
    public static final int TOKEN_ERROR = 403;
    public static final int PERMISSION_ERROR = 401;


    //shiro 相关

    //jwtToken中获取username的key
    public static final String JWT_USER_NAME = "jwt-user-name-key";
    //jwtToken中获取所有权限的的key
    public static final String JWT_PERMISSIONS_KEY = "jwt-permissions-key";
    //jwtToken中签发时间的key
    public static final String JWT_ISSUANCE_TIME = "jwt-issuance-time";

    public static final String IDENTIFY_CACHE_KEY = "shiro-cache:com.lee.mht.shiro.LeeRealm.authorizationCache:";



    public static final String HEADER_TOKEN_KEY = "AccessToken";
    public static final String REDIS_TOKEN_KEY = "AccessToken_USERID_";

    //Token过期时间
    public static final long TOKEN_EXPIRE_TIME = 60*60*12L;


    public static final String NOT_PERMISSION = "您没有操作权限";
    public static final String ILLEAGEL_TOKEN = "TOKEN非法,请勿篡改token！";
    public static final String TOKEN_EXPIRED  = "登陆已过期";

    public static final String SHIRO_AUTHENTICATION_ERROR = "用户认证异常";



    //业务常量
    public static final String LOGIN_SUCCESS = "登陆成功";
    public static final String ALLOW_ACCESS = "允许访问";

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

    public static final String USERNAME_UNIQUE = "用户名唯一";
    public static final String USERNAME_NOT_UNIQUE = "用户名不唯一";

    public static final String ROLENAME_UNIQUE = "角色名唯一";
    public static final String ROLENAME_NOT_UNIQUE = "角色名不唯一";
}
