package com.chrisjia.mall.common;

import com.chrisjia.mall.exception.ExceptionEnum;

public class ApiRestResponse<T> {
    private String code;
    private String msg;
    private T data;

    private final static String SUCCESS_CODE = "20000";
    private final static String SUCCESS_MSG = "success";
    private final static String FAIL_CODE = "40000";
    private final static String FAIL_MSG = "failed";

    public ApiRestResponse(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    public ApiRestResponse(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public ApiRestResponse() {
        this(SUCCESS_CODE, SUCCESS_MSG);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> ApiRestResponse<T> success(){
        return new ApiRestResponse<>();
    }
    public static <T> ApiRestResponse<T> success(T data){
        ApiRestResponse<T> res = new ApiRestResponse<T>();
        res.setData(data);
        return res;
    }

    public static <T>ApiRestResponse<T> fail(String code, String msg){
        return new ApiRestResponse<>(code, msg);
    }
    public static <T> ApiRestResponse<T> fail(ExceptionEnum ex){
        return new ApiRestResponse<>(ex.getCode(), ex.getMsg());
    }

}
