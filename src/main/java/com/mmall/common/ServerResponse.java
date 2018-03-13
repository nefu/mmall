package com.mmall.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * Created by jay on 2018/3/11.
 */
//一个为了传到前端的封装类
    //这个注解表示 对返回的json 如果属性为null 则不返回 或者说如果是null的对象 key也会消失
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> implements Serializable{
     private int status;
     private String msg;
     private T data;
     private ServerResponse(int status){
        this.status=status;
     }
     private ServerResponse(int status,T data){
        this.status=status;
         this.data=data;
     }
    private ServerResponse(int status,String msg,T data){
        this.status=status;
        this.msg=msg;
        this.data=data;
    }
    private ServerResponse(int status,String msg){
        this.status=status;
        this.msg=msg;
    }
    //判断是否是成功的
    //这个注解表示序列化之后 不会出现咋json里面
    @JsonIgnore
    public boolean isSuccess(){
        return this.status==ResponseCode.SUCCESS.getCode();
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }
   //成功的反馈
    public static <T> ServerResponse<T> createBySuccess(){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }
    public static <T> ServerResponse<T> createBySuccessMsg(String msg){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg);
    }
    public static <T> ServerResponse<T> createBySuccess(T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),data);
    }
    public static <T> ServerResponse<T> createBySuccess(String msg,T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg,data);
    }
    //失败的反馈
    public static <T> ServerResponse<T> createByError(){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode());
    }
    public static <T> ServerResponse<T> createByErrorMsg(String msg){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),msg);
    }
    public static <T> ServerResponse<T> createByError(T data){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),data);
    }
    public static <T> ServerResponse<T> createByError(String msg,T data){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),msg,data);
    }
}
