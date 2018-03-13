package com.mmall.common;

/**
 * Created by jay on 2018/3/11.
 */
//common包下都是工具类 将一些常量写在这个类下
public enum  ResponseCode {
    SUCCESS(0,"success"),
    ERROR(0,"error"),
    NEED_LOGIN(10,"need_login"),
    ILLEGAL_ARGUMENT(2,"illegal_argument");

    private final int code;
    private final String desc;
    ResponseCode(int code,String desc){
       this.code=code;
        this.desc=desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
