package com.lee.mht.system.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author FucXing
 * @date 2022/01/10 11:26
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MhtLog {

    //用户操作了什么
    String action();

    String type();
}
