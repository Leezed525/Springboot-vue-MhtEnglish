package com.lee.mht.system.exception;

/**
 * @author FucXing
 * @date 2022/01/04 16:13
 **/
public class SystemException extends RuntimeException {

    /**
     *  异常 code
     */
    private final int code;

    /**
     *  异常提示
     */
    public final String defaultMessage;

    public SystemException(int code, String defaultMessage) {
        super(defaultMessage);
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    public int getCode() {
        return code;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
