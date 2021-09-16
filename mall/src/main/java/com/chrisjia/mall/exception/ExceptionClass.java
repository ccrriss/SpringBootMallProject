package com.chrisjia.mall.exception;

public class ExceptionClass extends Exception{
    private String code;
    private String msg;

    public ExceptionClass(String code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public ExceptionClass(ExceptionEnum ex) {
        this.code = ex.getCode();
        this.msg = ex.getMsg();
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
}
